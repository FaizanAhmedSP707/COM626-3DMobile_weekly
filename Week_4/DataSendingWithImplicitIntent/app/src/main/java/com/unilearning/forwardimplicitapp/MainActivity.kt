package com.unilearning.forwardimplicitapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call the function that sends the data after accessing the button
        val userLatData = findViewById<EditText>(R.id.userLatitudeEntry)
        val userLonData = findViewById<EditText>(R.id.userLongitudeEntry)
        val mapStyleSpinner = findViewById<Spinner>(R.id.spinnerForMapStyleSelection)
        val sendBtn = findViewById<Button>(R.id.sendBtn)

        sendBtn.setOnClickListener {
            if(userLatData?.text.toString().trim() == ""){
                userLatData?.error = "Should not be empty!"
            } else if(userLonData?.text.toString().trim() == "") {
                userLonData?.error = "Should not be empty!"
            } else {
                val latCoord = userLatData.text.toString().toDouble()
                val lonCoord = userLonData.text.toString().toDouble()
                val spinSelectOpt = mapStyleSpinner.selectedItem.toString()

                sendBackUserChoices(latCoord, lonCoord, spinSelectOpt)
            }
        }
    }

    private fun sendBackUserChoices(lat_val: Double, lon_val: Double, spOption: String) {
        val intent = Intent("ACTION_DISPLAY_MAP")
        val bundle = bundleOf("com.unilearning.forwardimplicitapp.LAT" to lat_val,
                                    "com.unilearning.forwardimplicitapp.LON" to lon_val,
                                    "com.unilearning.forwardimplicitapp.MAPSPIN" to spOption)
        intent.putExtras(bundle)
        if(intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No activity to handle this intent", Toast.LENGTH_LONG).show()
        }
    }
}