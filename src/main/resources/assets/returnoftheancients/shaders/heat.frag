#version 120
uniform sampler2D texture;

varying vec2 texcoord;
varying vec4 color;

float interpolate(float start, float end) {
    return start + (end - start) * 0.08;
}

void main() {
    vec4 original = texture2D(texture, texcoord);
    vec3 orange = vec3(1.0, 0.6, 0.0);
    gl_FragColor = vec4(interpolate(original.r, orange.r), interpolate(original.g, orange.g), interpolate(original.b, orange.b), original.a);
}