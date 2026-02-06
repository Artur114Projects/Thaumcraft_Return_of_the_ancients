#version 120
uniform sampler2D screenTexture;   // unit 0
uniform sampler2D customTexture;    // unit 1
uniform float Time;

varying vec2 texcoord;
varying vec2 lightCoord;

float interpolate(float start, float end) {
    return (start + (end - start) * 0.1) * 1.2;
}

vec3 interpolate(vec3 start, vec3 end) {
    return vec3(interpolate(start.r, end.r), interpolate(start.g, end.g), interpolate(start.b, end.b));
}

void main() {
    vec2 uv = texcoord;
    vec4 color = texture2D(screenTexture, texcoord);
    if (texture2D(customTexture, texcoord).r > 0.0) {
        float dx = sin((uv.x + Time) * 100) * (0.006 * clamp(0.06, 0.0, 0.6));
        float dy = sin((uv.y + Time * 0.5) * 100) * (0.006 * clamp(0.06, 0.0, 0.6));
        uv += vec2(dx, dy);

        vec3 col = vec3(0.0);
        float count = 0.0;
        for (int i = 0; i < 8; i++) {
            float ang = float(i) / 8.0 * 6.2831853;
            vec2 off = vec2(cos(ang), sin(ang));
            col += texture2D(screenTexture, uv + off).rgb;
            count += 1.0;
        }
        col /= count;

        vec3 orange = vec3(1.0, 0.4, 0.0);
        color = vec4(interpolate(col, orange), 1.0);
    }
    gl_FragColor = color;
}