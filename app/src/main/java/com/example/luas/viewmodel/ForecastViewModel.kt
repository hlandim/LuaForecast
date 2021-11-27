package com.example.luas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.luas.data.Repository
import com.example.luas.model.StopInfo
import com.example.luas.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
    private val clock: Clock,
    private val dispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<StopInfo>> = MutableLiveData()
    private lateinit var selectedStop: TramStop
    private lateinit var selectedDirection: TramDirection
    val response: LiveData<NetworkResult<StopInfo>> = _response

    init {
        selectStop()
        selectDirection()

        fetchData()

    }

    private fun fetchData() {
        viewModelScope.launch(dispatcher) {
            _response.value = repository.getStopForecast(selectedStop.name)
        }
    }

    private fun selectStop() {
        val currentTime: LocalTime = LocalTime.now(clock)

        // check if current time is between 00:00AM - 12:00PM
        val isInTheRange: Boolean =
            currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(LocalTime.NOON)

        selectedStop = if (isInTheRange) {
            TramStop.MAR
        } else {
            TramStop.STI
        }
    }

    private fun selectDirection() {
        selectedDirection = if (TramStop.MAR == selectedStop) {
            TramDirection.OUTBOUND
        } else {
            TramDirection.INBOUND
        }
    }

}

enum class TramStop {
    MAR,
    STI
}

enum class TramDirection {
    OUTBOUND,
    INBOUND
}