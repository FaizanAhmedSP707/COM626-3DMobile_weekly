package com.unilearning.depthviewapp
import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import freemap.openglwrapper.GLMatrix
import freemap.openglwrapper.GPUInterface
import freemap.openglwrapper.OpenGLUtils
import java.io.IOException
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// Our GLSurfaceView for rendering the 3D world.

class OpenGLView(ctx: Context): GLSurfaceView(ctx), GLSurfaceView.Renderer {

    init {
        setEGLContextClientVersion(2) // use GL ES version 2
        setRenderer(this)
    }

    val gpu = GPUInterface("default shader")

    var fbuf: FloatBuffer? = null

    val blueCol = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f) // Global constant for drawing an opaque blue shape
    val yellowCol = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f) // Global constant for drawing an opaque yellow shape

    // Create a variable to hold the view matrix
    val viewMatrix = GLMatrix()

    // Create a variable to hold the projection matrix
    val projectionMatrix = GLMatrix()

    // Setup code to run when the OpenGL view is first created
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Sets the background colour
        GLES20.glClearColor(0f,0f,0f, 1f) // alpha decides the transparency

        // Enable depth testing, to prevent seeing objects that are further away
        GLES20.glClearDepthf(1.0f) // Can be something else, best to leave it as it is
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        try {
            val success = gpu.loadShaders(context.assets, "vertex.glsl", "fragment.glsl")
            if(!success){
                Log.d("opengl01Load", gpu.lastShaderError)
            }
            // If the loading is successful, then initialise the FloatBuffer with X,Y and Z coordinates
            // in order to make a flat shape appear on the screen.
            fbuf = OpenGLUtils.makeFloatBuffer(
                floatArrayOf(
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    0f, 1f, 0f,
                    0f, 0f, 0f,
                    -1f, 0f, 0f,
                    0f, -1f, 0f
                )
            )
            // Selects this shader program
            gpu.select()
        } catch(e: IOException) {
            // This code involves loading files, so we need to handle the appropriate exception
            Log.d("opengl01Load", e.stackTraceToString())
        }
    }

    /*
    Draws the current frame
    Is called multiple times per second
    Your actual screen drawing goes in here
    */
    override fun onDrawFrame(gl: GL10?) {
        // Clears any previous settings from the previous frame
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Create a reference to the attribute variable aVertex
        val ref_aVertex = gpu.getAttribLocation("aVertex")

        // getting a handle on the uniform variable for our shape, which is colour in this case
        val ref_uColour = gpu.getUniformLocation("uColour")

        // Run the below code only if the buffer is not null
        fbuf?.apply {

            // Sending data to the shader
            gpu.setUniform4FloatArray(ref_uColour, blueCol)

            // Telling Android OpenGL ES 2.0 what format our data is in, and what shader variable will
            // receive the data
            gpu.specifyBufferedDataFormat(ref_aVertex, this, 0)

            // For the stride value, either 0 or 12 will work. Stride refers to the number
            // of bytes separating one vertex's data from the other

            /*
            * Finally, we draw the triangle. This works by passing each vertex in the selected buffer to
            * the shader in turn, loading each vertex into the currently-selected shader variable.
            *
            * This example can be modified to draw 6 triangles by setting firstVertex to 0 and the
            * nVertices to 6. The method will know to treat each set of three vertices as a triangle.
            * Alternatively, if our float array of vertices had 6 vertices specified, we could choose
            * to draw only the second triangle by providing 3,3 to the gpu call below.
            * */
            gpu.drawBufferedTriangles(0, 3) // The first triangle is to be done in blue

            // Now the second triangle is to be drawn in yellow
            gpu.setUniform4FloatArray(ref_uColour, yellowCol)
            gpu.drawBufferedTriangles(3, 3)
        }
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