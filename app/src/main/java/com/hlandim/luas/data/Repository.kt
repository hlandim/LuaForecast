package com.hlandim.luas.data

import com.hlandim.luas.model.StopInfo
import com.hlandim.luas.utils.NetworkResult

interface Repository {

    suspend fun getStopForecast(stop: String): NetworkResult<StopInfo>
}