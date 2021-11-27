package com.example.luas.data

import com.example.luas.data.remote.RemoteDataSource
import com.example.luas.model.BaseApiResponse
import com.example.luas.model.StopInfo
import com.example.luas.utils.NetworkResult
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getStopForecast(stop: String): NetworkResult<StopInfo> {
        return safeApiCall { remoteDataSource.getStopForecast(stop) }

    }
}