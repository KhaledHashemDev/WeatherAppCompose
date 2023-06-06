package com.khaled.weather.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.khaled.weather.utils.TempUnit
import com.khaled.weather.data.repository.WeatherDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
*
* Composable function that displays a temperature unit selection UI.
* @param weatherSelectionState MutableState holding the selected temperature unit.
* @param weatherDataStore The data store used to update the preferred temperature measure.
*/
@Composable
fun TempMeasureSelection(weatherSelectionState: MutableState<TempUnit>, weatherDataStore: WeatherDataStore) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        fun updateTempMeasure(tempUnit: TempUnit) {
            weatherSelectionState.value = tempUnit
            CoroutineScope(Dispatchers.IO).launch {
                weatherDataStore.upToDatePreferredTempUnit(tempUnit)
            }
        }

        ClickableText(
            text = AnnotatedString(text = TempUnit.Celcius.symbol.toString()),
            onClick = {
                updateTempMeasure(TempUnit.Celcius)
            },
        )
        Spacer(modifier = Modifier.width(10.dp))

        ClickableText(
            text = AnnotatedString(text = TempUnit.Fahrenheit.symbol.toString()),
            onClick = {
                updateTempMeasure(TempUnit.Fahrenheit)
            },
        )
    }
}
