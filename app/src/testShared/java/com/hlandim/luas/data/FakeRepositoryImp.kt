package com.hlandim.luas.data

import com.hlandim.luas.data.MockForecastResponse.Companion.MARLBOROUGH
import com.hlandim.luas.data.MockForecastResponse.Companion.STILLORGAN
import com.hlandim.luas.model.StopInfo
import com.hlandim.luas.utils.NetworkResult
import org.xmlpull.v1.XmlPullParser
import retrofit2.Response
import javax.inject.Inject

class FakeRepositoryImp @Inject constructor() : Repository {
    override suspend fun getStopForecast(stop: String): NetworkResult<StopInfo> {
        return when (stop.lowercase()) {
            "mar" -> NetworkResult.Success(MARLBOROUGH)
            "sti" -> NetworkResult.Success(STILLORGAN)
            else -> NetworkResult.Error("Wrong stop name")
        }

    }
}