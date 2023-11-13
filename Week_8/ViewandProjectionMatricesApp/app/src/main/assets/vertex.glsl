attribute vec4 aVertex;
uniform mat4 uView, uProj;

void main(void) {
    gl_Position = uProj * uView * aVertex;
}