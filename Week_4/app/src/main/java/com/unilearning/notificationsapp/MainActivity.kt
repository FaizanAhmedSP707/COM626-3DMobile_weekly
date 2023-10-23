package com.unilearning.notificationsapp

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : AppCompatActivity() {
    var notifyID = 0
    var permissionsGranted = false
    var service: MyGPSService? = null

    val gpsViewModel: LocationViewModel by viewModels()

    /*  This code isn't needed here for this exercise as we're using broadcasts
    // Add your service as an attribute of the main activity (nullable)
    val serviceConn = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as MyGPSService.GPSServiceBinder).GPS_Service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
    */
    lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Configuration.getInstance()
            .load(this, PreferenceManager.getDefaultSharedPreferences(this))

        val map1 : MapView = findViewById(R.id.map1)
        map1.controller?.setZoom(14.0)
        map1.controller?.setCenter(GeoPoint(51.05, -0.72))
        requestPermissions()

        val filter = IntentFilter().apply {
            addAction("sendLocationCoords")
        }
        receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    "sendLocationCoords" -> {
                        val newLatitude = intent.getDoubleExtra("com.unilearning.mappingwithbroadcasts.latitude", 0.0)
                        val newLongitude = intent.getDoubleExtra("com.unilearning.mappingwithbroadcasts.longitude", 0.0)
                        gpsViewModel.latlon = LatLon(newLatitude, newLongitude)

                        /*
                        Have a broadcast intent declared here which will send an intent to stop the GPS when the button on the
                        notification is tapped on.
                        */
                        val broadcast_stopGPS = Intent().apply {
                            action = "StopSignal"
                        }
                        val piStopGpsUpdates = PendingIntent.getBroadcast(this@MainActivity, notifyID, broadcast_stopGPS, PendingIntent.FLAG_UPDATE_CURRENT)

                        // Creating a channel ID for this notification as we're targeting API Level 26+
                        val channelID = "LOCATION_CHANNEL"

                        // Guarding the code for channels by checking if we're running at least Oreo (API Level26)
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val channel = NotificationChannel(channelID, "Location notifications", NotificationManager.IMPORTANCE_DEFAULT)
                            val nMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            nMgr.createNotificationChannel(channel)

                            // Creating a notification for the new latlon received from the broadcast
                            val notification = Notification.Builder(this@MainActivity, channelID)
                                .setContentTitle("Location changed")
                                .setContentText("New latitude: ${newLatitude}, New Longitude: $newLongitude")
                                .setSmallIcon(R.drawable.note)
                                .addAction(Notification.Action.Builder(R.drawable.node, "Stop GPS", piStopGpsUpdates).build()) // For adding the button to the notification which will launch our pending intent
                                .build()

                            // To actually show the notification we need the notification manager
                            nMgr.notify(notifyID, notification)
                            // Increment the local class variable to make assigning IDs easier
                            notifyID++
                        }
                    }
                }
            }
        }

        //registerReceiver(receiver, filter)
        ContextCompat.registerReceiver(this, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        // The above code is a much more secure way of registering broadcasts for the app!

        // Button handling for the buttons: This one is to start the GPS
        findViewById<Button>(R.id.btnStartGps).setOnClickListener {
            //service?.startGps()

            // Here we send an intent instead for the service to receive and start the GPS service
            val gpsStartBroadcast = Intent().apply {
                action = "StartSignal"
            }
            sendBroadcast(gpsStartBroadcast)
        }

        // This one is to query the service for location updates and update our ViewModel with the new data
        findViewById<Button>(R.id.btnGetGps).setOnClickListener {
            service?.latLon?.apply {
                gpsViewModel.latlon = this
            }
        }

        // This one is for stopping the service
        findViewById<Button>(R.id.btnStopGps).setOnClickListener {
            //service?.stopGps()

            // Here we send a different intent to the service to stop the GPS
            val gpsStopBroadcast = Intent().apply {
                action = "StopSignal"
            }
            sendBroadcast(gpsStopBroadcast)
        }

        // Applying the observer to the LiveData present in our ViewModel so that the map can be updated
        /*
        The second argument to the observer of the LiveData is a lambda function, so it has been
        written outside of the parentheses.
        */
        gpsViewModel.liveLatLon.observe(this) {map1.controller?.setCenter(GeoPoint(it.lat, it.lon))}
    }

    fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, LOCATION_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            permissionsGranted = true
            initService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = true
            initService()
        } else {
            AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage("GPS permission denied").show()
        }
    }
    fun initService() {
        // Start the service here...
        val startIntent = Intent(this, MyGPSService::class.java)
        startService(startIntent)

        /*
        // This code here is for binding the service to the activity
        val bindIntent = Intent(this, MyGPSService::class.java)
        bindService(bindIntent, serviceConn, Context.BIND_AUTO_CREATE)

        For this exercise, we're not binding the service to the main activity,
        we will be using Broadcasts to send information between different components
        that can listen to these broadcasts and perform actions based on it.
        This allows our components to be loosely coupled, which is good programming
        practice. For reference, components may belong to the same application, or to
        completely different applications.

        */
    }

    override fun onDestroy() {
        super.onDestroy()
        //unbindService(serviceConn)
        unregisterReceiver(receiver)
    }
}