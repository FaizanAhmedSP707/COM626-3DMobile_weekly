package com.unilearning.camerax_app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    // An image capture object that we will need for when the image is captured
    var imageCapture : ImageCapture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            // Do something for picture storage
        } else {
            //
        }
    }

    private fun startCamera() {
        val lifecycleOwner = this
        val previewCam = findViewById<PreviewView>(R.id.previewCamView)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        imageCapture = ImageCapture.Builder().build()
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    // previewCam is a UI object representing the preview, so we use
                    // findViewByID() to use it
                    it.setSurfaceProvider(previewCam.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
                } catch (e: Exception) {
                    Log.d("CAMERAX1", e.stackTraceToString())
                }
            }, ContextCompat.getMainExecutor(this)
        )
    }

    private fun takePicture() {
        imageCapture?.apply {

        }
    }
}