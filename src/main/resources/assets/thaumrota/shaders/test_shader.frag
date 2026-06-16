#version 120
varying vec2 texcoord;

uniform sampler2D screenTexture;
uniform sampler2D depthTexture;
uniform mat4 invProjMatrix;
uniform float time;

float lerp(float start, float end, float delta) {
    return (start + (end - start) * delta);
}

vec3 lerp(vec3 start, vec3 end, float delta) {
    return vec3(lerp(start.r, end.r, delta), lerp(start.g, end.g, delta), lerp(start.b, end.b, delta));
}

float distanceToCamera(vec2 uv) {
    float depthRaw = texture2D(depthTexture, uv).r;
    vec3 ndc = vec3(uv * 2.0 - 1.0, depthRaw * 2.0 - 1.0);
    vec4 viewH = invProjMatrix * vec4(ndc, 1.0);
    vec3 viewPos = viewH.xyz / viewH.w;
    return length(viewPos);
}

float hash(vec2 uv) {
    return fract(sin(dot(uv, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 uv) {
    vec2 i = floor(uv);
    vec2 f = fract(uv);

    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x)
    + (c - a) * u.y * (1.0 - u.x)
    + (d - b) * u.x * u.y;
}

float random(vec2 uv) {
    return fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43758.5453123);
}

vec4 heatWaves(float dist) {
    float mask = smoothstep(0.0, 16.0, dist);
    vec2 p = texcoord * 3.0 + vec2(time * 0.1);
    vec2 noi = vec2(noise(p) - 0.5) * 0.008 * mask;

    vec2 largeWave = vec2(sin(texcoord.y * 20.0 + time * 0.7) * cos(texcoord.x * 18.0 + time * 0.5), cos(texcoord.x * 22.0 + time * 0.6) * sin(texcoord.y * 19.0 + time * 0.8));
    vec2 smallWave = vec2(sin(texcoord.y * 80.0 + time * 1.3) * cos(texcoord.x * 75.0 + time * 1.1), cos(texcoord.x * 85.0 + time * 1.2) * sin(texcoord.y * 78.0 + time * 1.4));
    float noise = hash(texcoord + floor(time * 0.5));
    vec2 jitter = vec2(noise - 0.5, hash(texcoord.yx + noise) - 0.5) * 0.3;
    vec2 wav = (largeWave * 0.6 + smallWave * 0.4 + jitter * 0.1) * (0.002) * mask;
    vec2 off = (noi * 0.75) + (wav * 0.75);

    vec2 uv = texcoord + off;

    float radius = 0.0006 * mask;
    vec3 col = vec3(0.0);
    float count = 0.0;
    for (int i = 0; i < 8; i++) {
        float ang = float(i) / 8.0 * 6.2831853;
        vec2 off = vec2(cos(ang), sin(ang)) * radius;
        col += texture2D(screenTexture, uv + off).rgb;
        count += 1.0;
    }
    col /= count;

    return vec4(col, 1.0);
}

vec4 modifyColor(vec4 color) {
    return vec4(lerp(color.rgb, vec3(0.8, 0.3, 0.0), 0.1) * 1.2, color.a);
}

void main() {
    float dist = distanceToCamera(texcoord) + 32;
    vec4 color;

    color = heatWaves(dist);
    color = modifyColor(color);

    gl_FragColor = color;
}