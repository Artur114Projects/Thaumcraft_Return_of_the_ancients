#version 120
uniform sampler2D screenTexture;   // unit 0
uniform sampler2D depthTexture;    // unit 1
uniform float Time;

varying vec2 texcoord;

float interpolate(float start, float end) {
    return (start + (end - start) * 0.14) * 1.2;
}

vec3 interpolate(vec3 start, vec3 end) {
    return vec3(interpolate(start.r, end.r), interpolate(start.g, end.g), interpolate(start.b, end.b));
}

void main() {
    vec2 uv = texcoord;
    float d = clamp(texture2D(depthTexture, uv).r - 0.06, 0.0, 1.0);
    float radius = (d * 0.8) * 0.008;

    if (d > 0) {
        float dx = sin((uv.x + Time) * 100) * (0.006 * clamp(d + 0.06, 0.0, 0.6));
        float dy = sin((uv.y + Time * 0.5) * 100) * (0.006 * clamp(d + 0.06, 0.0, 0.6));
        uv += vec2(dx, dy);
    }

    vec3 col = vec3(0.0);
    float count = 0.0;
    for (int i = 0; i < 8; i++) {
        float ang = float(i) / 8.0 * 6.2831853;
        vec2 off = vec2(cos(ang), sin(ang)) * radius;
        col += texture2D(screenTexture, uv + off).rgb;
        count += 1.0;
    }
    col /= count;

    vec3 orange = vec3(1.0, 0.4, 0.0);

    gl_FragColor = vec4(interpolate(col, orange), 1.0);
}