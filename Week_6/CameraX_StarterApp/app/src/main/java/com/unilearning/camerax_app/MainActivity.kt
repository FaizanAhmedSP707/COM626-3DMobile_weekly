package com.unilearning.camerax_app

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    // An image capture object that we will need for when the image is captured
    var imageCapture : ImageCapture? = null
    private var permissionGranted: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pictureTakeBtn = findViewById<Button>(R.id.takePicBtn)

        // Function call to request permissions
        requestPermissions()

        // Check if Camera permission granted and start the camera
        if(permissionGranted!!) {
            startCamera()
        } else {
            // Tell the user that the app can't carry out its' intended function
            AlertDialog.Builder(this).setPositiveButton("OK", null)
                .setMessage("You need to grant permissions for the app to work. Please close the app and try again.").show()
        }

        // Prevent a possible exception when the user clicks on the button
        if(permissionGranted!!) {
            pictureTakeBtn.setOnClickListener {
                Toast.makeText(this, "Functionality not implemented yet", Toast.LENGTH_LONG).show()
            }
        } else {
            AlertDialog.Builder(this).setPositiveButton("OK", null)
                .setMessage("You cannot take a picture at the moment as you haven't\ngranted permission to use the camera").show()
        }
    }
    private fun requestPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        } else {
            // If the permission was already granted before, set the boolean variable to true
            permissionGranted = true
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
        } else {
            AlertDialog.Builder(this).setPositiveButton("OK", null)
                .setMessage("Camera permission denied").show()
            permissionGranted = false
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
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                // Do something for picture storage
                val file = "${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/test.jpg"
                val outputOptions: ImageCapture.OutputFileOptions =
                    ImageCapture.OutputFileOptions.Builder(File(file)).build()

                // What should be done when an attempt is made to store the captured image
                val imageSavedCallBack1 = object: ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        AlertDialog.Builder(this@MainActivity).setPositiveButton("OK", null)
                            .setMessage("Saved successfully").show()
                    }

                    override fun onError(e: ImageCaptureException) {
                        AlertDialog.Builder(this@MainActivity).setPositiveButton("OK", null)
                            .setMessage("Error: ${e.message}").show()
                    }
                }

                // Finally we do the following
                this.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(this@MainActivity),
                    imageSavedCallBack1
                )
            } else {
                // If SDK_INT is 10 or higher, use the MediaStore API
                val nameOfImage = "captured_image"
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nameOfImage)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX1")
                }

                // Setting options for the output image file
                val outputOptions = ImageCapture.OutputFileOptions.Builder(
                    contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()

                val imageSavedCallBack2 = object: ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        AlertDialog.Builder(this@MainActivity).setPositiveButton("OK", null)
                            .setMessage("Saved successfully").show()
                    }

                    override fun onError(e: ImageCaptureException) {
                        AlertDialog.Builder(this@MainActivity).setPositiveButton("OK", null)
                            .setMessage("Error: ${e.message}").show()
                    }
                }

                this.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(this@MainActivity),
                    imageSavedCallBack2
                )
            }
        }
    }
}