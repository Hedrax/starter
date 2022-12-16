package com.udacity.project4.locationreminders.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

class GeofenceUtils(context: Context?):ContextWrapper(context) {
    private var pendingIntent: PendingIntent? = null

    fun buildGeofence(ID: String, latLng: LatLng, rad: Float, type: Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, rad)
            .setRequestId(ID)
            .setTransitionTypes(type)
            .setLoiteringDelay(6000)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getPendingIntent(): PendingIntent? {
        if (pendingIntent == null){
            val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
            pendingIntent = PendingIntent
                .getBroadcast(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return pendingIntent
    }

    fun getGeofencingRequest(geofence: Geofence?): GeofencingRequest {
        return GeofencingRequest
            .Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }

}