package com.unilearning.depthviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val glView = OpenGLView(this)
        setContentView(glView) // making the GLVew the main content view of our activity
    }
}