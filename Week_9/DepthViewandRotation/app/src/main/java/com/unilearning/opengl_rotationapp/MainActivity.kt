package com.unilearning.opengl_rotationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

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
        findViewById<Button>(R.id.cwRotation).setOnClickListener {
            glView.camera.rotate(-10f)
        }
        findViewById<Button>(R.id.acwRotation).setOnClickListener {
            glView.camera.rotate(10f)
        }
        findViewById<Button>(R.id.forwardMovementInAngle).setOnClickListener {
            //Toast.makeText(this, "Move forward functionality not implemented yet", Toast.LENGTH_LONG).show()
            var radian = (glView.camera.rotation * (Math.PI/180))
            var negativeDZ = Math.cos(radian).toFloat()
            var negativeDX = Math.sin(radian).toFloat()
            glView.camera.translate(-negativeDX, 0f, -negativeDZ)
        }
        findViewById<Button>(R.id.backwardMovementInAngle).setOnClickListener {
            //Toast.makeText(this, "Move backward functionality not implemented yet", Toast.LENGTH_LONG).show()
            var radian = (glView.camera.rotation * (Math.PI/180))
            var negDZ = Math.cos(radian).toFloat()
            var negDX = Math.sin(radian).toFloat()
            glView.camera.translate(negDX, 0f, negDZ)
        }
    }
}