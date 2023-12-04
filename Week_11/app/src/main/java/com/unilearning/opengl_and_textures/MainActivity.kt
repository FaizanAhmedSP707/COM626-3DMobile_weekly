package com.unilearning.opengl_and_textures

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import freemap.openglwrapper.Camera

class MainActivity : AppCompatActivity() {
    // An image capture object that we will need for when the image is captured
    private var permissionGranted: Boolean? = null
    private var surfaceTexture: SurfaceTexture? = null
    lateinit var glView: OpenGLView

    private var radian: Double = 0.0
    private var negativeDz : Float = 0.0f
    private var negativeDx : Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = OpenGLView(this) {
            Log.d("gltestLog", "Starting camera")
            surfaceTexture = it // SurfaceTexture sent from the OpenGLView to here as the "it" parameter
            requestPermissions()
        }
        setContentView(glView)

        /*
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
        */
    }
    private fun Camera.moveCamera(mvValue: Int){
        // extension function --> Not sure how to write
        radian = this.rotation * (Math.PI/180)
        negativeDz = mvValue * (Math.cos(radian).toFloat())
        negativeDx = mvValue * (Math.sin(radian).toFloat())
        this.translate(negativeDx, 0f, negativeDz)
    }

    private fun requestPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        } else {
            // If the permission was already granted before, set the boolean variable to true
            permissionGranted = true
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Set the permissionGranted variable to true
            permissionGranted = true
            startCamera()
        } else {
            AlertDialog.Builder(this).setPositiveButton("OK", null)
                .setMessage("Camera permission denied").show()
            permissionGranted = false
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {

                    val surfaceProvider: (SurfaceRequest) -> Unit = { request ->
                        val resolution = request.resolution
                        surfaceTexture?.apply{
                            setDefaultBufferSize(resolution.width, resolution.height)
                            val surface = Surface(this)
                            request.provideSurface(
                                surface,
                                ContextCompat.getMainExecutor(this@MainActivity.baseContext))
                            { }
                        }
                    }
                    it.setSurfaceProvider(surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                } catch (e: Exception) {
                    Log.d("CAMERAX1", e.stackTraceToString())
                }
            }, ContextCompat.getMainExecutor(this)
        )
    }
}