/*
This file is used for dealing with texture, by using the feed obtained from the
device's camera for assisting in showing textures
*/
attribute vec4 aVertex;
varying vec2 vTextureValue;

void main(void){
    gl_Position = aVertex;
    vTextureValue = vec2(0.5*(1.0 + aVertex.x), 0.5*(1.0 + aVertex.y));
}