package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.roundToInt

data class UserLocationInfo(
    val latitude: Double,
    val longitude: Double,
    val provider: String = "GPS",
    val areaName: String = "Bengaluru",
    val isRealGps: Boolean = false
)

object LocationRequestHelper {

    // Default reference point: Whitefield / Central Bengaluru
    val DEFAULT_BENGALURU_LOCATION = UserLocationInfo(
        latitude = 12.9850,
        longitude = 77.7314,
        provider = "Bengaluru Central",
        areaName = "Whitefield / Bengaluru",
        isRealGps = false
    )

    fun hasLocationPermission(context: Context): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(
        context: Context,
        onSuccess: (UserLocationInfo) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            onError("Location permission not granted")
            return
        }

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onSuccess(
                        UserLocationInfo(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            provider = location.provider ?: "FusedLocationProvider",
                            areaName = resolveAreaName(location.latitude, location.longitude),
                            isRealGps = true
                        )
                    )
                } else {
                    // Try last known location as fallback
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                        if (lastLoc != null) {
                            onSuccess(
                                UserLocationInfo(
                                    latitude = lastLoc.latitude,
                                    longitude = lastLoc.longitude,
                                    provider = "LastKnownLocation",
                                    areaName = resolveAreaName(lastLoc.latitude, lastLoc.longitude),
                                    isRealGps = true
                                )
                            )
                        } else {
                            onSuccess(DEFAULT_BENGALURU_LOCATION)
                        }
                    }.addOnFailureListener {
                        onSuccess(DEFAULT_BENGALURU_LOCATION)
                    }
                }
            }.addOnFailureListener { e ->
                onError("Failed to fetch location: ${e.localizedMessage}")
            }
        } catch (e: SecurityException) {
            onError("Security Exception: ${e.localizedMessage}")
        } catch (e: Exception) {
            onError("Unexpected error: ${e.localizedMessage}")
        }
    }

    /**
     * Calculates distance between user and site coordinates in kilometers
     */
    fun calculateDistanceKm(
        userLat: Double,
        userLng: Double,
        siteLat: Double,
        siteLng: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(userLat, userLng, siteLat, siteLng, results)
        val distanceInMeters = results[0]
        val distanceKm = distanceInMeters / 1000f
        return (distanceKm * 10f).roundToInt() / 10f
    }

    private fun resolveAreaName(lat: Double, lng: Double): String {
        // Approximate Bengaluru district cluster bounds
        return when {
            lat in 12.96..13.01 && lng in 77.70..77.78 -> "Whitefield & Kadugodi"
            lat in 12.82..12.88 && lng in 77.63..77.69 -> "Electronic City"
            lat in 12.91..12.95 && lng in 77.66..77.70 -> "Bellandur & ORR"
            lat in 12.92..12.96 && lng in 77.70..77.75 -> "Varthur & Panathur"
            lat in 13.01..13.07 && lng in 77.57..77.63 -> "Hebbal & Yelahanka"
            lat in 12.95..12.99 && lng in 77.56..77.62 -> "Bengaluru Central"
            else -> "Bengaluru Metro Area"
        }
    }
}
