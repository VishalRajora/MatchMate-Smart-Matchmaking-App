package com.example.matchmate.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matchmate.modelclass.MatchMate
import com.example.matchmate.modelclass.MateMatchResult
import com.example.matchmate.repo.MatchMateRepo
import com.example.matchmate.util.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchMateViewModel @Inject constructor(private val repo: MatchMateRepo) : ViewModel() {

    private val TAG: String = "VishalCCC"

    val matchMateFlow: StateFlow<NetworkState<MatchMate>> = repo.matchMateFlow

    val exception = CoroutineExceptionHandler { _, exception ->
        Log.i(TAG, "CoroutineExceptionHandler: ${exception.message}")
    }

    init {
        getMatchMates()
    }

    fun getMatchMates() {
        viewModelScope.launch(SupervisorJob() + Dispatchers.IO + exception) {
            repo.getMatchMates()
        }
    }

    fun updateRoomData(result: MateMatchResult) {
        repo.updateRoomData(result)
    }
}