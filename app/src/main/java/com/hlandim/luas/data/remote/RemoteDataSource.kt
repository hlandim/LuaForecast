package com.hlandim.luas.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val forecastService: ForecastService) {
    suspend fun getStopForecast(stop: String) = forecastService.getStopForecast(stop)
}