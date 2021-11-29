package com.hlandim.luas

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hlandim.luas.data.FakeRepositoryImp
import com.hlandim.luas.data.Repository
import com.hlandim.luas.data.remote.ForecastService
import com.hlandim.luas.data.remote.RemoteDataSource
import com.hlandim.luas.model.StopInfo
import com.hlandim.luas.util.MainCoroutineRule
import com.hlandim.luas.viewmodel.ForecastViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
class ForecastViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var application: Application

    private lateinit var clock: Clock

    @MockK
    lateinit var forecastService: ForecastService

    private lateinit var repository: Repository

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        remoteDataSource = RemoteDataSource(forecastService)
        repository = FakeRepositoryImp()

        clock = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When I request the forecast on the morning, Then I should see trams forecast from MARLBOROUGH LUAS stop towards Outbound`() =
        runBlockingTest {
            // Given it is 3AM
            val fixedClock = Clock.fixed(
                LocalTime.of(3, 0).atDate(LocalDate.MAX).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
            )
            every { clock.instant() } returns fixedClock.instant()
            every { clock.zone } returns fixedClock.zone

            // When requesting forecast
            val viewModel =
                ForecastViewModel(
                    application,
                    repository,
                    clock,
                    testDispatcher
                )
            viewModel.fetchData()
            val forecasts = viewModel.response.value?.data

            // The Marlborough's Outbound forecast should not be empty
            assertNotNull(forecasts)
            forecasts?.let { forecast ->
                assertEquals("Marlborough", forecast.stop)
                checkNotEmptyForecast(forecast, "Outbound")
            }
        }


    @Test
    fun `When I request the forecast on the afternoon or at night, Then I should see trams forecast from STILLORGAN LUAS stop towards Inbound`() =
        runBlockingTest {

            // Given it is 1PM
            val fixedClock = Clock.fixed(
                LocalTime.of(13, 0).atDate(LocalDate.MAX).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault()
            )
            every { clock.instant() } returns fixedClock.instant()
            every { clock.zone } returns fixedClock.zone

            // When requesting forecast
            val viewModel =
                ForecastViewModel(
                    application,
                    repository,
                    clock,
                    testDispatcher
                )
            viewModel.fetchData()
            val forecasts = viewModel.response.value?.data

            // The Stillorgan's Inbound forecast should not be empty
            assertNotNull(forecasts)
            forecasts?.let { forecast ->
                assertEquals("Stillorgan", forecast.stop)
                checkNotEmptyForecast(forecast, "Inbound")
            }
        }

    private fun checkNotEmptyForecast(info: StopInfo, direction: String) {
        assertNotNull(info.directions)
        info.directions?.let { directions ->
            assertTrue(directions.size == 1)
            // Get the required direction Outbound/Inbound
            val requiredDirection = directions[0]
            assertNotNull(requiredDirection)
            // Check if it is the correct direction
            assertEquals(direction, requiredDirection.name)
            // Get trams for the required direction
            assertNotNull(requiredDirection.trams)
            requiredDirection.trams?.let { trams ->
                assertTrue(trams.size > 0)
                // Check if it has at least one valid tram
                assertNotEquals("", trams[0].dueMin)
            }

        }

    }
}