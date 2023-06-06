package com.khaled.weather.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

enum class TempUnit(val symbol: String) {
    Celcius("°C"), Fahrenheit("°F")
}

/**
*
* Converts a temperature value from Kelvin to the specified temperature unit.
* @param temperature The temperature value in Kelvin. If null, a default value of 273.15 Kelvin is used.
* @param tempUnit The target temperature unit for conversion.
* @return The converted temperature value as a formatted string, including the degree symbol and temperature unit symbol.
 */
fun temperatureConverter(temperature: Double?, tempUnit: TempUnit): String {
    val baseKelvin = 273.15
    val finalTemp = temperature ?: baseKelvin
    return when (tempUnit) {
        TempUnit.Celcius -> finalTemp - baseKelvin
        TempUnit.Fahrenheit -> (finalTemp - baseKelvin) * (9 / 5) + 32
    }.roundToInt().toString() + " " + tempUnit.symbol
}

/**
*
* Converts a timestamp to a formatted display time string.
* @param time The timestamp in seconds.
* @return The formatted display time string in the format "dd-MMM-yyyy HH:mm:ss".
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formattedDisplayTime(time: Long): String =
    LocalDateTime.ofInstant(
        Instant.ofEpochMilli(time * 1000),
        TimeZone
            .getDefault().toZoneId(),
    ).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"))

/**
*
* Applies a time zone offset to generate a formatted time zone string.
* @param timeZone The time zone offset in seconds.
* @return The formatted time zone string in a specific format.
 */
fun timeZoneString(timeZone: Int): String {
    val fromGmt = timeZone.toDouble() / (60 * 60)
    return "${if (fromGmt > 0) "GMT +" else "GMT -"} ${Math.abs(fromGmt)}"
}

/**
 *
 * Displays an error message in an alert dialog.
 * @param reportError The error message to be displayed.
 * @param context The context in which the error dialog should be shown.
 * @param onOK Callback function to be executed when the OK button is clicked.
 */
fun displayError(reportError: String, context: Context, onOK: () -> Unit) {
    if (reportError.isNotEmpty()) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Alert")
            .setMessage(reportError)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
        onOK()
    }
}
