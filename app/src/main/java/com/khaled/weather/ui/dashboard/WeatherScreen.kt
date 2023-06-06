package com.khaled.weather.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.khaled.weather.data.model.GeoLocationModel
import com.khaled.weather.data.model.StoredPreferences
import com.khaled.weather.data.model.CityWeatherModel
import com.khaled.weather.data.remote.ApiDetails
import com.khaled.weather.data.repository.WeatherDataStore
import com.khaled.weather.utils.*
import java.util.*

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun WeatherScreen(
    weatherDataStore: WeatherDataStore,
) {
    val viewModel = hiltViewModel<WeatherViewModel>()
    val weatherFetch: CityWeatherModel by viewModel.weatherDetails.collectAsState(initial = CityWeatherModel())
    val currentLocationFetch: CityWeatherModel by viewModel.currentLocationDetails.collectAsState(initial = CityWeatherModel())
    val registerError: String by viewModel.reportError.collectAsState(initial = "")

    // Fetch the weather stored preferences
    val storedPreferences = weatherDataStore.getStoredPreferences().collectAsState(initial = StoredPreferences())
    val weatherSelectionState = remember { mutableStateOf(TempUnit.Celcius) }

    // Check for errors and display them if applicable
    displayError(registerError, LocalContext.current) { viewModel.clearError() }

    // Load the last stored location and get the temperature as per the last selected temp unit
    storedPreferences.value.lastStoredLocation?.let { lastLocation -> viewModel.fetchWeatherDetails(lastLocation, {}) }
    weatherSelectionState.value = storedPreferences.value.tempUnit
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    searchState.value = TextFieldValue(storedPreferences.value.lastStoredLocation.orEmpty())

    val loadWeatherForSearch: (String) -> Unit =
        { searchLocation ->
            viewModel.fetchWeatherDetails(searchLocation) { search -> weatherDataStore.upToDateSearchedLocation(search) }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        val locationGeoLocationModel = remember { mutableStateOf(GeoLocationModel()) }
        viewModel.fetchWeatherDetails(locationGeoLocationModel.value)

        LocationPermission(locationGeoLocationModel = locationGeoLocationModel)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
        ) {
            SearchText(searchState, loadWeatherForSearch)
        }

        // Place the temperature unit selection
        Row {
            TempMeasureSelection(weatherSelectionState = weatherSelectionState, weatherDataStore)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Display the details of the location search
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (weatherFetch.name!!.isNotBlank()) {
                WeatherContent(weatherFetch, weatherSelectionState)
            }
        }

        // Display Current location if access is available
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (currentLocationFetch.name!!.isNotBlank()) {
                WeatherContent(currentLocationFetch, weatherSelectionState)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeatherContent(
    weatherDetails: CityWeatherModel,
    weatherSelectionState: MutableState<TempUnit>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        val tempMeasure = weatherSelectionState.value

        Row(
            modifier = Modifier.padding(start = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Row {
                    Text(
                        text = weatherDetails.name?.let { "$it (${weatherDetails.sys!!.country})" }.orEmpty(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray,
                    )
                }
                Row {
                    Column {
                        Text(
                            text = temperatureConverter(weatherDetails.main!!.temp, tempMeasure),
                            fontSize = 38.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray,
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Timezone: ")
                                }
                                append(timeZoneString(weatherDetails.timezone!!))
                            },
                            color = Color.DarkGray,
                        )

                        val tempStat =
                            "${temperatureConverter(weatherDetails.main.tempMax, tempMeasure)}/${temperatureConverter(
                                weatherDetails.main.tempMin,
                                tempMeasure,
                            )}"
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Temperature: ")
                                }
                                append(tempStat)
                            },
                            color = Color.DarkGray,
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Feels like: ")
                                }
                                append(temperatureConverter(weatherDetails.main.feelsLike, tempMeasure))
                            },
                            color = Color.DarkGray,
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Air Pressure:")
                                }
                                append(" ${weatherDetails.main.pressure} mb")
                            },
                            color = Color.DarkGray,
                        )

                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Humidity:")
                                }
                                append(" ${weatherDetails.main.humidity}%")
                            },
                            color = Color.DarkGray,
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Visibility:")
                                }
                                append(" ${(weatherDetails.visibility!! / 1000)} km")
                            },
                            color = Color.DarkGray,
                        )

                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Sunrise:")
                                }
                                append(" ${formattedDisplayTime(weatherDetails.sys!!.sunrise!!.toLong())}")
                            },
                            color = Color.DarkGray,
                        )

                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Sunset:")
                                }
                                append(" ${formattedDisplayTime(weatherDetails.sys?.sunset!!.toLong())}")
                            },
                            color = Color.DarkGray,
                        )

                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Wind Speed: ")
                                }
                                append("${(weatherDetails.wind!!.speed)} km in ${weatherDetails.wind.deg}")
                            },
                            color = Color.DarkGray,
                        )
                    }
                    Column {
                        val listState = rememberLazyListState()
                        LazyRow(state = listState) {
                            itemsIndexed(weatherDetails.weather ?: ArrayList()) { _, item ->
                                item?.let {
                                    AsyncImage(
                                        model = ApiDetails.iconUrl(it.icon!!),
                                        contentDescription = it.description,
                                        modifier = Modifier
                                            .size(170.dp),
                                    )
                                }
                            }
                        }
                    }
                }
                Row {
                    Column {
                        Spacer(modifier = Modifier.height(35.dp))
                    }
                }
            }
        }
    }
}
