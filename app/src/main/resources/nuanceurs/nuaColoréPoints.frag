//cspell:ignore mediump
#version 460 compatibility
precision mediump float;

in vec3 norm_O;
in vec4 col_O;

out vec4 Fragment;

void main(){
    Fragment = vec4(col_O.rgb*min( max( dot( norm_O, vec3(0,1,0) )+0.5, 0), 1.0), col_O.a);
}