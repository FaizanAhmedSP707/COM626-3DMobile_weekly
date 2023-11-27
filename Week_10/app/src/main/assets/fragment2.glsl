/*
This file is for drawing shapes whose vertices each have different colours.
Thus, some new variables have to be defined in order for this to work
*/
precision mediump float;
varying vec4 vColour;

void main(void){
    gl_FragColor = vColour;
}