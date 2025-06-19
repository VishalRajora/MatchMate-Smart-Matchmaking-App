package com.example.matchmate.repo

import com.example.matchmate.modelclass.MatchMate
import com.example.matchmate.modelclass.MateMatchResult
import com.example.matchmate.modelclass.MateMatchResultRoom
import com.example.matchmate.retrofit.ApiInterface
import com.example.matchmate.room.UserDao
import com.example.matchmate.util.MatchScoringUtil
import com.example.matchmate.util.NetworkState
import com.example.matchmate.util.retry
import com.example.matchmate.util.simulateFlaky
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchMateRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userDao: UserDao,
) {

    private val _matchMateFlow =
        MutableStateFlow<NetworkState<MatchMate>>(NetworkState.Init())
    val matchMateFlow = _matchMateFlow.asStateFlow()


    suspend fun getMatchMates(results: Int = 10) {
        _matchMateFlow.value = NetworkState.Loading()

        val response = retry(
            times = 3,
            block = {
                simulateFlaky {
                    apiInterface.getUsers(results)
                }
            },
            blockError = {
                _matchMateFlow.value = NetworkState.Error("Failed to fetch match mates")
                null
            }
        )

        if (response?.isSuccessful == true && response.body() != null) {
            val body = response.body()!!

            val updatedResults = body.results.map {
                val matchScore = MatchScoringUtil.computeMatchScore(
                    user = it,
                    currentUserAge = 28,
                    currentUserCity = "Mumbai"
                )
                it.copy(uuid = it.login.uuid, matchScore = matchScore)
            }

            val updatedResultsRoom = updatedResults.map {
                MateMatchResultRoom(
                    uuid = it.login.uuid,
                    cell = it.cell,
                    gender = it.gender,
                    email = it.email,
                    phone = it.phone,
                    nat = it.nat,
                    status = it.status,
                    matchScore = it.matchScore
                )
            }

            CoroutineScope(Dispatchers.IO).launch {
                userDao.insertAll(updatedResultsRoom)
            }
            _matchMateFlow.value = NetworkState.Success(body.copy(results = updatedResults))

        } else {
            _matchMateFlow.value = NetworkState.Error(
                "Error fetching match mates: ${response?.errorBody()?.string()}"
            )
        }
    }

    fun updateRoomData(result: MateMatchResult) {
        CoroutineScope(Dispatchers.IO).launch {
            val roomData = MateMatchResultRoom(
                uuid = result.uuid,
                cell = result.cell,
                email = result.email,
                gender = result.gender,
                nat = result.nat,
                phone = result.phone,
                status = result.status,
                matchScore = result.matchScore ?: 0
            )
            userDao.updateUser(roomData)
        }
    }
}