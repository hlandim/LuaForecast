package com.hlandim.luas.data

import com.hlandim.luas.data.remote.RemoteDataSource
import com.hlandim.luas.model.StopInfo
import com.hlandim.luas.utils.NetworkResult
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