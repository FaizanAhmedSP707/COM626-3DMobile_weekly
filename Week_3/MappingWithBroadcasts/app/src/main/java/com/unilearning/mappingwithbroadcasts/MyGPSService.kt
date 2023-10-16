package com.unilearning.mappingwithbroadcasts

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast

class MyGPSService: Service(), LocationListener {

    var mgr: LocationManager? = null

    // Need this for storing the new location when the user's location changes
    val latLon = LatLon()
    var checkPermission = false

    // The following code is related to binding the service to the main activity
    inner class GPSServiceBinder(val GPS_Service: MyGPSService): android.os.Binder()

    // Start handler
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkPermission = true
        // Do something here, e.g. start GPS, or music playing, etc.
        startGps()

        return START_STICKY // --> Important if you want the service to be restarted
    }

    /*
    bind handler - not needed in many cases but defined as an abstract method
    in Service, therefore must be overridden
    */
    override fun onBind(intent: Intent?): IBinder? {
        //return null // This is temporary, but needs to be changed so that the service can be bound to the Activity
        return GPSServiceBinder(this)
    }

    fun startGps() {
        mgr = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(checkPermission == true) {
            mgr?.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0f, this)
        }

    }

    fun stopGps() {
        // Remove all updates from the location listener; this is done when the service is stopped.
        mgr?.removeUpdates(this)
    }

    override fun onLocationChanged(newLoc: Location) {
        latLon.lat = newLoc.latitude
        latLon.lon = newLoc.longitude

        // Send the above data in a broadcast instead
        val broadcast = Intent().apply {
            action = "sendLocationCoords"
            putExtra("com.unilearning.mappingwithbroadcasts.latitude", newLoc.latitude)
            putExtra("com.unilearning.mappingwithbroadcasts.longitude", newLoc.longitude)
        }
        sendBroadcast(broadcast)
    }

    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this, "Provider is disabled", Toast.LENGTH_LONG).show()
    }

    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this, "Provider has been enabled", Toast.LENGTH_LONG).show()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }
}