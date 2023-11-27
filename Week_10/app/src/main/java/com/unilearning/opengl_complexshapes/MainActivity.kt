package com.unilearning.opengl_complexshapes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import freemap.openglwrapper.Camera

class MainActivity : AppCompatActivity() {
    private var radian: Double = 0.0
    private var negativeDz : Float = 0.0f
    private var negativeDx : Float = 0.0f
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
            /*Toast.makeText(this, "Move forward functionality not implemented yet", Toast.LENGTH_LONG).show()
            radian = (glView.camera.rotation * (Math.PI/180))
            negativeDz = Math.cos(radian).toFloat()
            negativeDx = Math.sin(radian).toFloat()
            glView.camera.translate(-negativeDx, 0f, -negativeDz)*/

            glView.camera.moveCamera(-1)
        }
        findViewById<Button>(R.id.backwardMovementInAngle).setOnClickListener {
            /*Toast.makeText(this, "Move backward functionality not implemented yet", Toast.LENGTH_LONG).show()
            radian = (glView.camera.rotation * (Math.PI/180))
            negativeDz = Math.cos(radian).toFloat()
            negativeDx = Math.sin(radian).toFloat()
            glView.camera.translate(negativeDx, 0f, negativeDz)*/

            glView.camera.moveCamera(1)
        }
    }
    private fun Camera.moveCamera(mvValue: Int){
        // extension function --> Not sure how to write
        radian = this.rotation * (Math.PI/180)
        negativeDz = mvValue * (Math.cos(radian).toFloat())
        negativeDx = mvValue * (Math.sin(radian).toFloat())
        this.translate(negativeDx, 0f, negativeDz)
    }
}