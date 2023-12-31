package com.unilearning.depthviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creating a reference to the GLView class so that the camera can be manipulated
        val glView = findViewById<OpenGLView>(R.id.glview)

        findViewById<Button>(R.id.minusX).setOnClickListener {
            glView.camera.translate(-1f, 0f, 0f)
        }
        findViewById<Button>(R.id.plusX).setOnClickListener {
            glView.camera.translate(1f, 0f, 0f)
        }
        findViewById<Button>(R.id.minusY).setOnClickListener {
            glView.camera.translate(0f, -1f, 0f)
        }
        findViewById<Button>(R.id.plusY).setOnClickListener {
            glView.camera.translate(0f, 1f, 0f)
        }
        findViewById<Button>(R.id.minusZ).setOnClickListener {
            glView.camera.translate(0f, 0f, -1f)
        }
        findViewById<Button>(R.id.plusZ).setOnClickListener {
            glView.camera.translate(0f, 0f, 1f)
        }
    }
}