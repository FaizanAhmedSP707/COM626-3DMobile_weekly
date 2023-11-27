package com.unilearning.opengl_complexshapes

import java.nio.FloatBuffer
import java.nio.ShortBuffer
import freemap.openglwrapper.OpenGLUtils
import freemap.openglwrapper.GPUInterface

class Cube(val x: Float, val y: Float, val z: Float) {
    var vertexBuf: FloatBuffer
    var indexBuf: ShortBuffer
    init {
        vertexBuf = OpenGLUtils.makeFloatBuffer(
            floatArrayOf(
                x, y+0.5f, z,
                x+0.5f, y+0.5f, z,
                x+0.5f, y+0.5f, z+0.5f,
                x, y+0.5f, z+0.5f,
                x, y, z,
                x+0.5f, y, z,
                x+0.5f, y, z+0.5f,
                x, y, z+0.5f
            )
        )

        indexBuf = OpenGLUtils.makeShortBuffer(
            shortArrayOf(
                0,4,1,1,4,5,3,0,2,2,0,1,1,5,2,2,5,6,3,7,0,0,7,4,2,6,3,3,6,7,4,7,5,5,7,6
            )
        )
    }

    fun render(gpuRef: GPUInterface, aVertexRef: Int) {
        gpuRef.drawIndexedBufferedData(vertexBuf, indexBuf, 0, aVertexRef)
    }
}