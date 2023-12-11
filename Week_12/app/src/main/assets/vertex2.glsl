/*
This file is for drawing shapes whose vertices each have different colours.
Thus, some new variables have to be defined in order for this to work
*/
attribute vec4 aVertex, aColour;
varying vec4 vColour;
uniform mat4 uPerspMtx, uMvMtx;

void main(void){
    gl_Position = uPerspMtx * uMvMtx * aVertex;
    vColour = aColour;
}