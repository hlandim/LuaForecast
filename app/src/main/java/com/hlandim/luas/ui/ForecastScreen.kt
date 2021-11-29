package com.hlandim.luas.ui

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import com.hlandim.luas.R
import com.hlandim.luas.data.RepositoryImp
import com.hlandim.luas.data.remote.ForecastService
import com.hlandim.luas.data.remote.RemoteDataSource
import com.hlandim.luas.model.Direction
import com.hlandim.luas.model.StopInfo
import com.hlandim.luas.model.Tram
import com.hlandim.luas.ui.MockViewModel.Companion.STOP_INFO
import com.hlandim.luas.ui.theme.LuasAppTheme
import com.hlandim.luas.utils.NetworkResult
import com.hlandim.luas.viewmodel.ForecastViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import java.time.Clock

@ExperimentalFoundationApi
@Composable
fun ForecastScreen(forecastViewModel: ForecastViewModel) {

    LuasAppTheme {
        Scaffold(
            topBar = { MyTopBar(forecastViewModel) },
            content = { ForecastListContent(forecastViewModel) }
        )
    }
}

@Composable
fun MyTopBar(forecastViewModel: ForecastViewModel) {

    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = { forecastViewModel.fetchData() }) {
                Icon(
                    painterResource(R.drawable.refresh),
                    stringResource(id = R.string.refresh)
                )
            }
        }
    )
}


@ExperimentalFoundationApi
@Composable
fun ForecastListContent(forecastViewModel: ForecastViewModel) {
    val networkResult by forecastViewModel.response.observeAsState()

    networkResult?.let { result ->
        when (result) {
            is NetworkResult.Success -> {
                if (result.data == null) {
                    NoResult()
                } else {
                    ForecastList(result.data)
                }
            }
            is NetworkResult.Error -> {
                if (result.message != null) {
                    ErrorMsg(msg = result.message)
                } else {
                    ErrorMsg(msg = stringResource(id = R.string.error))
                }
            }
            is NetworkResult.Loading -> {
                LoadingView()
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun ForecastList(stopInfo: StopInfo) {
    if (stopInfo.directions != null) {
        LazyColumn(modifier = Modifier
            .padding(all = 10.dp)
            .semantics {
                contentDescription = "Forecast List"
            }) {
            stickyHeader {
                MainHeader(stopInfo)
            }
            stopInfo.directions?.let { directions ->
                val direction = directions[0]
                direction.trams?.let { trams ->
                    itemsIndexed(trams) { _, tram ->
                        ForecastItem(tram)
                    }
                }

            }
        }
    } else {
        NoResult()
    }

}


@Composable
private fun MainHeader(stopInfo: StopInfo) {
    stopInfo.directions?.let {
        it[0].let { direction ->
            direction.name?.let { directionName ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .semantics {
                            contentDescription = "Stop title"
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {
                    stopInfo.stop?.let { stopName ->
                        Text(
                            text = "$stopName - "
                        )
                        Text(
                            text = directionName
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ForecastItem(tram: Tram) {
    Surface(
        shape = MaterialTheme.shapes.large,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tram.destination?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(end = 10.dp)
                )

            }
            tram.dueMin?.let {
                val text = if (it == "DUE") it else "${it}min"
                Text(text = text)
            }


        }
    }
}

@Composable
private fun NoResult() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        text = stringResource(id = R.string.no_result_found),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ErrorMsg(msg: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        text = msg,
        color = Color.Red,
        textAlign = TextAlign.Center
    )
}

@ExperimentalFoundationApi
@Preview(
    name = "Forecast View",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
private fun PreviewForecastView(
    forecastViewModel: ForecastViewModel = MockViewModel(Application()).composeViewModel
) {
    LuasAppTheme {
        Scaffold(
            topBar = { MyTopBar(forecastViewModel) },
            content = { ForecastList(STOP_INFO) }
//        content = { NoResult() }
//        content = { LoadingView() }
        )
    }

}

class MockViewModel(application: Application) : AndroidViewModel(application), ForecastService {
    val composeViewModel = ForecastViewModel(
        Application(),
        RepositoryImp(RemoteDataSource(this)),
        Clock.systemDefaultZone(),
        Dispatchers.Main
    )

    companion object {
        val STOP_INFO = StopInfo(
            "Stillorgan",
            mutableListOf(
                Direction(
                    "Inbound",
                    mutableListOf(
                        Tram(
                            destination = "Parnell",
                            dueMin = "11"
                        ),
                        Tram(
                            destination = "Parnell",
                            dueMin = "11"
                        ),
                        Tram(
                            destination = "Parnell",
                            dueMin = "11"
                        )
                    )
                )
            )
        )
    }

    override suspend fun getStopForecast(stop: String): Response<StopInfo> {
        return Response.success(STOP_INFO)
    }

}
