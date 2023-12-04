/*
This file is used for dealing with texture, by using the feed obtained from the
device's camera for assisting in showing textures
*/
attribute vec4 aVertex;
varying vec2 vTextureValue;

void main(void){
    gl_Position = aVertex;
    vTextureValue = vec2(0.5*(1.0 + aVertex.x), 0.5*(1.0 + (-aVertex.y)));
}
/*
Need to negate the T-coordinate due to the way the video memory is presented. The video
memory is presented from top to bottom, whereas S&T coordinates start from bottom to top.
Thus, we need to negate the T-coordinate so that the camera feed displays properly.
*/