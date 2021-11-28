package com.hlandim.luas.data.remote

import com.hlandim.luas.model.StopInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {
    @GET("get.ashx?action=forecast&encrypt=false")
    suspend fun getStopForecast(
        @Query("stop") stop: String
    ): Response<StopInfo>
}