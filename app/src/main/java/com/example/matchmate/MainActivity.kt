package com.example.matchmate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchmate.adapter.MateMatchResultAdapter
import com.example.matchmate.databinding.ActivityMainBinding
import com.example.matchmate.modelclass.MatchMate
import com.example.matchmate.modelclass.MatchStatus
import com.example.matchmate.modelclass.MateMatchResult
import com.example.matchmate.util.NetworkState
import com.example.matchmate.viewModels.MatchMateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MateMatchResultAdapter.OnUserActionListener {

    private val viewModel: MatchMateViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { MateMatchResultAdapter(this) }
    private var mMatchMate = emptyList<MateMatchResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()
        setupRecyclerView()
        observeMatchMates()
        setupRetry()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupRetry() {
        binding.errorTv.setOnClickListener {
            viewModel.getMatchMates()
        }
    }

    private fun observeMatchMates() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.matchMateFlow.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    private fun renderState(state: NetworkState<*>) {
        with(binding) {
            when (state) {
                is NetworkState.Loading -> {
                    pb.isVisible = true
                    errorTv.isVisible = false
                }

                is NetworkState.Error -> {
                    pb.isVisible = false
                    errorTv.isVisible = true
                    errorTv.text = "${state.message}, click to retry"
                }

                is NetworkState.Success<*> -> {
                    pb.isVisible = false
                    errorTv.isVisible = false

                    val results = (state.data as? MatchMate)?.results.orEmpty()
                    mMatchMate = results
                    adapter.submitList(results)
                }

                is NetworkState.Init -> Unit
            }
        }
    }

    override fun onUserReject(user: MateMatchResult) {
        handleMatchStatus(user, MatchStatus.DECLINED)
    }

    override fun onUserChecked(user: MateMatchResult) {
        handleMatchStatus(user, MatchStatus.ACCEPTED)
    }

    private fun handleMatchStatus(user: MateMatchResult, status: MatchStatus) {
        val updatedUser = user.copy(status = status)
        viewModel.updateRoomData(updatedUser)
        val newMatchMate = mMatchMate.map {
            if (it.id == user.id) updatedUser else it
        }
        mMatchMate = newMatchMate
        adapter.submitList(newMatchMate)
    }
}
