package com.example.luas.ui

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.luas.R
import com.example.luas.model.Direction
import com.example.luas.model.StopInfo
import com.example.luas.model.Tram
import com.example.luas.ui.theme.LuasAppTheme
import com.example.luas.utils.NetworkResult
import com.example.luas.viewmodel.ForecastViewModel

@ExperimentalFoundationApi
@Composable
fun ForecastView(forecastViewModel: ForecastViewModel) {
    val networkResult by forecastViewModel.response.observeAsState()
    LuasAppTheme {
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
}

@ExperimentalFoundationApi
@Composable
private fun ForecastList(stopInfo: StopInfo) {
    if (stopInfo.directions != null) {
        LazyColumn {
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
                        .padding(5.dp),
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
    Surface(elevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            tram.destination?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(end = 10.dp)
                )

            }
            tram.dueMin?.let {
                val text = if (it == "DUE") "" else "${it}min"
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
private fun PreviewForecastView() {
    ForecastList(
        StopInfo(
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
    )
}
