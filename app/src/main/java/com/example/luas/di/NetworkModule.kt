package com.example.luas.di

import android.content.Context
import com.example.luas.BuildConfig
import com.example.luas.data.remote.ForecastService
import com.example.luas.utils.Constants.Companion.BASE_URL
import com.example.luas.utils.Utils.hasInternetConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        connectivityInterceptor: ConnectivityInterceptor
    ): OkHttpClient {
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(connectivityInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideConvertFactory(): SimpleXmlConverterFactory = SimpleXmlConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        xmlParser: SimpleXmlConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(xmlParser)
            .build()
    }

    @Singleton
    @Provides
    fun provideForecastService(retrofit: Retrofit): ForecastService =
        retrofit.create(ForecastService::class.java)

    @Singleton
    @Provides
    fun providerViewModelDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    @Singleton
    @Provides
    fun provideConnectivityInterceptor(@ApplicationContext appContext: Context): ConnectivityInterceptor =
        ConnectivityInterceptor(appContext)

    class ConnectivityInterceptor(private val context: Context) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            if (!hasInternetConnection(context)) {
                throw NoConnectivityException()
            }

            val builder: Request.Builder = chain.request().newBuilder()
            return chain.proceed(builder.build())
        }

    }

    class NoConnectivityException : IOException() {
        companion object {
            private const val ERROR_MSG = "No connectivity"
        }

        override val message: String
            get() = ERROR_MSG

        override fun getLocalizedMessage(): String {
            return ERROR_MSG
        }
    }
}