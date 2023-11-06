package com.unilearning.opengl_app1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import freemap.openglwrapper.GPUInterface
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// Our GLSurfaceView for rendering the 3D world.

class OpenGLView(ctx: Context): GLSurfaceView(ctx), GLSurfaceView.Renderer {

    init {
        setEGLContextClientVersion(2) // use GL ES version 2
        setRenderer(this)
    }

    val gpu = GPUInterface("default shader")

    // Setup code to run when the OpenGL view is first created
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Sets the background colour
        GLES20.glClearColor(0f,0f,0f, 1f) // alpha decides the transparency

        // Enable depth testing, to prevent seeing objects that are further away
        GLES20.glClearDepthf(1.0f) // Can be something else, best to leave it as it is
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    /*
    Draws the current frame
    Is called multiple times per second
    Your actual screen drawing goes in here
    */
    override fun onDrawFrame(gl: GL10?) {
        // Clears any previous settings from the previous frame
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }
    /*
    Is called whenever the resolution changes
    On a mobile device this will occur when the device is rotated.
    */
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Setting up the view port
        GLES20.glViewport(0, 0, width, height)
    }
}