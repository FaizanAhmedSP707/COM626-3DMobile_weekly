package com.unilearning.opengl_complexshapes

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import freemap.openglwrapper.Camera
import freemap.openglwrapper.GLMatrix
import freemap.openglwrapper.GPUInterface
import freemap.openglwrapper.OpenGLUtils
import java.io.IOException
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// Our GLSurfaceView for rendering the 3D world.

class OpenGLView(ctx: Context, aSet: AttributeSet): GLSurfaceView(ctx, aSet), GLSurfaceView.Renderer {

    init {
        setEGLContextClientVersion(2) // use GL ES version 2
        setRenderer(this)
    }

    val gpu = GPUInterface("default shader")

    var fbuf: FloatBuffer? = null

    private var cube1: Cube? = null
    private var cube2: Cube? = null

    private var squarebuf: FloatBuffer? = null
    private var indexbuf: ShortBuffer? = null

    val blueCol = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f) // Global constant for drawing an opaque blue shape
    val yellowCol = floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f) // Global constant for drawing an opaque yellow shape
    private val greenCol = floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f)
    val camera = Camera(0f,0f,0f)

    // Create a variable to hold the view matrix
    val viewMatrix = GLMatrix()

    // Create a variable to hold the projection matrix
    val projectionMatrix = GLMatrix()

    var buffersInitialised = false

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
            // Create 2 different cubes at slightly different coordinates that will be drawn in our OpenGLView
            cube1 = Cube(3f, 0f, 0f)
            cube2 = Cube(-3f, 0f, 0f)

            // If the loading is successful, then initialise the FloatBuffer with X,Y and Z coordinates
            // in order to make a flat shape appear on the screen.
            fbuf = OpenGLUtils.makeFloatBuffer(
                floatArrayOf(
                    0f, 0f, -3f,
                    1f, 0f, -3f,
                    0.5f, 1f, -3f,
                    -0.5f, 0f, -6f,
                    0.5f, 0f, -6f,
                    0f, 1f, -6f
                )
            )
            // initialising the buffer for drawing our square
            squarebuf = OpenGLUtils.makeFloatBuffer(
                floatArrayOf(
                    0f, 0f, -2f,
                    1f, 0f, -2f,
                    1f, 1f, -2f,
                    0f, 1f, -2f
                )
            )

            // Making an index buffer to point to the vertices that need to be drawn for our square
            indexbuf = OpenGLUtils.makeShortBuffer(
                shortArrayOf(
                    0, 1, 2, 3, 2, 0
                )
            )

            buffersInitialised = squarebuf != null && indexbuf != null && fbuf != null
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

        // Run the below code only if the buffer is not null
        if(buffersInitialised) {

            /*
            * TODO: perform some trigonometric calculations here, namely getting the sine and
            *  cosine values in the current angle that the camera is facing, so that the
            *  'move forward' and 'move backward' functionality can be properly implemented
            * */

            // setting the view matrix to the identity matrix so that it has no effect initially.
            viewMatrix.setAsIdentityMatrix()

            // Describing which axis should the camera be rotated around by changing the rotation value of the camera object
            viewMatrix.rotateAboutAxis(-camera.rotation, 'y')

            // Translation of the view matrix to see drawn shapes with new eye coordinates
            viewMatrix.translate(-camera.position.x, -camera.position.y, -camera.position.z)

            // Create a reference to the attribute variable aVertex
            val ref_aVertex = gpu.getAttribLocation("aVertex")

            // getting a handle on the uniform variable for our shape, which is colour in this case
            val ref_uColour = gpu.getUniformLocation("uColour")

            val refUView = gpu.getUniformLocation("uView")
            val refUProj = gpu.getUniformLocation("uProj")

            gpu.sendMatrix(refUView, viewMatrix)
            gpu.sendMatrix(refUProj, projectionMatrix)

            /*
            * Code for drawing our square goes here!
            * */
            gpu.setUniform4FloatArray(ref_uColour, greenCol)
            gpu.drawIndexedBufferedData(squarebuf!!, indexbuf!!, 0, ref_aVertex)

            // Sending data to the shader
            gpu.setUniform4FloatArray(ref_uColour, blueCol)

            // Telling Android OpenGL ES 2.0 what format our data is in, and what shader variable will
            // receive the data
            gpu.specifyBufferedDataFormat(ref_aVertex, fbuf!!, 0)

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

        // Calculating the projection matrix each time the dimensions of the 'GLSurfaceView' change
        val hfov = 60.0f // Horizontal field of view
        val aspect : Float = width.toFloat() / height
        projectionMatrix.setProjectionMatrix(hfov, aspect, 0.001f, 100f)
    }
}