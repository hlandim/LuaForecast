package com.hlandim.luas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hlandim.luas.data.Repository
import com.hlandim.luas.model.StopInfo
import com.hlandim.luas.utils.NetworkResult
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

    fun fetchData() {
        _response.value = NetworkResult.Loading()
        viewModelScope.launch(dispatcher) {
            val result = repository.getStopForecast(selectedStop.name)
            result.data?.let {
                it.directions?.let { directions ->
                    directions.retainAll { direction ->
                        direction.name?.lowercase()
                            .equals(selectedDirection.name.lowercase())
                    }
                }
            }
            _response.postValue(result)
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