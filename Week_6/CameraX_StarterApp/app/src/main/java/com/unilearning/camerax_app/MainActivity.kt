package com.unilearning.camerax_app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            // Do something for picture storage
        } else {
            //
        }
    }
}