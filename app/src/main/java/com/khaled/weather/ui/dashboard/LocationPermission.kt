package com.khaled.weather.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.khaled.weather.data.model.GeoLocationModel
import com.khaled.weather.utils.displayError

@Composable
fun LocationPermission(locationGeoLocationModel: MutableState<GeoLocationModel>) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionGranted = remember { mutableStateOf(false) }

    // Initialize the launcher with the call back to find out if the permission is granted or denied
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        locationPermissionGranted.value = isGranted
        if (!isGranted) {
            displayError("Location Permission Denied", context) {}
        }
    }

    // Launch the permission request if the location permission is not available
    if (!locationPermissionGranted.value) {
        SideEffect {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } else {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    locationGeoLocationModel.value = GeoLocationModel(location.latitude, location.longitude)
                }
        }
    }
}
