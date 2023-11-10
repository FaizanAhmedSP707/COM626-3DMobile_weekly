package com.unilearning.sensorstarter

import android.content.Context
import android.hardware.SensorEventListener
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    var accel: Sensor? = null
    var magField: Sensor? = null

    // The following three variables will be used in the 'smoothing' calculations.
    var prevX: Float = 0.0f
    var prevY: Float = 0.0f
    var prevZ: Float = 0.0f

    // The following three variables will be used to hold the 'smoothed-out' values from
    // the accelerometer
    var smoothX: Float = 0.0f
    var smoothY: Float = 0.0f
    var smoothZ: Float = 0.0f

    // An array to hold the current acceleration and magnetic field sensor values
    var accelValues = FloatArray(3)
    // Another array to hold the magnetic field values:
    var magFieldValues = FloatArray(3)

    val orientationMatrix = FloatArray(16) // Will be changed from it's original state to a different state eventually
    var orientations = FloatArray(3)

    val tvAzimuth = findViewById<TextView>(R.id.AzimuthTextView)
    val tvPitch = findViewById<TextView>(R.id.PitchTextView)
    val tvRoll = findViewById<TextView>(R.id.RollTextView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensorMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magField = sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) // Getting the magnetic field sensor
        // The magnetic field sensor gives measurements in µT (micro Tesla; µ = 10^-6)
        sensorMgr.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI)
        sensorMgr.registerListener(this, magField, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Leave blank, normally we don't do much here unless we have to do something
    }

    override fun onSensorChanged(ev: SensorEvent) {
        // Testing which sensor has been detected and then doing something with it
        if(ev.sensor == accel) {
            // handle the accelerometer sensor
            accelValues = ev.values.copyOf()
            val tvX = findViewById<TextView>(R.id.xAxisAccelValue)
            val tvY = findViewById<TextView>(R.id.yAxisAccelValue)
            val tvZ = findViewById<TextView>(R.id.zAxisAccelValue)

            /*
            * Code for smoothing out the values so that the values remain consistent instead of rapidly changing is:
            * stored value = (raw value*k) + (previous value * (1-k))
            *
            * Typically k is a small value, e.g. 0.05 or 0.075
            * */

            if(tvX.text.toString() == "" && tvY.text.toString() == "" && tvZ.text.toString() == ""){
                /*
                If all the three text views are empty, then get the values from the accel sensor,
                convert them to string data, and store the current values so that they can be used
                to smooth out the next values that are received from the accel sensor
                */
                tvX.text = accelValues[0].toString()
                tvY.text = accelValues[1].toString()
                tvZ.text = accelValues[2].toString()

                // Storage of the current values of x,y and z to smooth out the next values that are obtained
                prevX = accelValues[0]
                prevY = accelValues[1]
                prevZ = accelValues[2]

            } else {
                // Use the previously stored values of x,y & z to display the new values
                smoothX = (prevX*0.05 + (prevX * (1-0.05))).toFloat()
                smoothY = (prevY*0.05 + (prevY * (1-0.05))).toFloat()
                smoothZ = (prevZ*0.05 + (prevZ * (1-0.05))).toFloat()

                tvX.text = smoothX.toString()
                tvY.text = smoothY.toString()
                tvZ.text = smoothZ.toString()

                // Finally set the three current float values to the 'prev..' variables
                prevX = accelValues[0]
                prevY = accelValues[1]
                prevZ = accelValues[2]
            }
            // NOTE: The code above needs a bit of refactoring as it doesn't seem to work as expected

        } else if(ev.sensor == magField){
            // handle the magnetic field sensor

            // Copy the current values into the magnetic field value array
            magFieldValues = ev.values.copyOf()
        }

        // Calculate the rotation matrix
        SensorManager.getRotationMatrix(orientationMatrix, null, magFieldValues, accelValues)

        // Get the orientations
        SensorManager.getOrientation(orientationMatrix, orientations)

        // Display the orientations in the 3 other text views
        tvAzimuth.text = orientations[0].toString()
        tvPitch.text = orientations[1].toString()
        tvRoll.text = orientations[2].toString()
    }
}