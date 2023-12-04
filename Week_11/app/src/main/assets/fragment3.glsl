// For dealing with textures
#extension GL_OES_EGL_image_external: require
precision mediump float;

varying vec2 vTextureValue;
uniform samplerExternalOES uTexture;

void main(void){
    gl_FragColor = texture2D(uTexture, vTextureValue);
}