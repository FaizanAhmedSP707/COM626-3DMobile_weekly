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

    // An array to hold the current acceleration and magnetic field sensor values
    var accelValues = FloatArray(3)
    // Another array to hold the magnetic field values:
    var magFieldValues = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensorMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magField = sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) // Getting the magnetic field sensor
        // The magnetic field sensor gives measurements in µT (micro Tesla; µ = 10^-6)
        sensorMgr.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI)
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
            var prevX = accelValues[0]
            var prevY = accelValues[1]
            var prevZ = accelValues[2]

            var smoothX = prevX*0.05 + (prevX * (1-0.05))
            var smoothY = prevY*0.05 + (prevY * (1-0.05))
            var smoothZ = prevZ*0.05 + (prevZ * (1-0.05))

            // Setting the values of the float array to each of the 3 text views
            tvX.text = smoothX.toString()
            tvY.text = smoothY.toString()
            tvZ.text = smoothZ.toString()
            // NOTE: The code above needs a bit of refactoring as it doesn't seem to work as expected

        } else if(ev.sensor == magField){
            TODO("Hasn't been implemented yet, will do in the near future")
        }
    }
}