package com.udacity.project4.locationreminders.geofence

import android.content.Context
import android.content.ContextWrapper
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.model.LatLng

class GeofenceUtils(context: Context?) {
    //TODO - pendingIntent - handleRequest

    fun buildGeofence(ID: String, latLng: LatLng, rad: Float, type: Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, rad)
            .setRequestId(ID)
            .setTransitionTypes(type)
            .setLoiteringDelay(6000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

}