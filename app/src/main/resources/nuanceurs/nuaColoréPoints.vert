//cspell:ignore mediump
//cspell:ignore transformee
#version 460 compatibility
precision mediump float;

layout (location=0) in vec3 pos;
layout (location=1) in vec3 norm;
layout (location=2) in vec4 col;

uniform mat4 projection;
uniform mat4 vue;
uniform mat4 transforme;
uniform mat4 rotation;

out vec3 norm_O;
out vec4 col_O;

void main(){
    norm_O = (rotation*vec4(norm,1)).xyz;
    col_O = col;
    gl_Position = projection*vue*transforme*vec4(pos,1.0);
}