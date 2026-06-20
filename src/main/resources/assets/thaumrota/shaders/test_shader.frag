#version 120
varying vec2 texcoord;

uniform sampler2D screenTexture;
uniform sampler2D depthTexture;
uniform mat4 invMVPMatrix;
uniform float globalHeat;
uniform float time;

#define MAX_POINT_LIGHTS 48
uniform vec3 pointLightPos[MAX_POINT_LIGHTS];
uniform vec3 pointLightColor[MAX_POINT_LIGHTS];
uniform vec3 pointLightParams[MAX_POINT_LIGHTS];
uniform int pointLightCount;

#define MAX_LINE_LIGHTS 48
uniform vec3 lineLightABBMax[MAX_LINE_LIGHTS];
uniform vec3 lineLightABBMin[MAX_LINE_LIGHTS];
uniform vec3 lineLightPosTo[MAX_LINE_LIGHTS];
uniform vec3 lineLightPosFrom[MAX_LINE_LIGHTS];
uniform vec3 lineLightColor[MAX_LINE_LIGHTS];
uniform vec3 lineLightParams[MAX_LINE_LIGHTS];
uniform int lineLightCount;


float lerp(float start, float end, float delta) {
    return (start + (end - start) * delta);
}

vec3 lerp(vec3 start, vec3 end, float delta) {
    return vec3(lerp(start.r, end.r, delta), lerp(start.g, end.g, delta), lerp(start.b, end.b, delta));
}

float hash(vec2 uv) {
    return fract(sin(dot(uv, vec2(127.1, 311.7))) * 43758.5453);
}

float lenghtSq(vec3 vec) {
    return vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
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

vec3 worldPosFromDepth(vec2 uv) {
    float depth = texture2D(depthTexture, uv).r;
    vec4 ndc = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 worldH = invMVPMatrix * ndc;
    return worldH.xyz / worldH.w;
}

float distToLineSq(vec3 p, vec3 a, vec3 b) {
    vec3 ab = b - a;
    vec3 ap = p - a;
    float t = clamp(dot(ap, ab) / dot(ab, ab), 0.0, 1.0);
    vec3 closest = a + t * ab;
    vec3 diff = p - closest;
    return dot(diff, diff);
}

float computeAtt(float distSq, float range) {
    float att = 1.0 / (1.0 + distSq * range);
    return att * att;
}

vec3 computeLight(float distSq, vec3 color, float brigness, float att) {
    return color * att * brigness;
}

vec4 heatWaves(vec3 col, float dist, float heatFactor) {
    float mask = smoothstep(0.0, 14.0 * 14.0, dist);
    vec2 p = texcoord * 3.0 + vec2(time * 0.1);
    vec2 noi = vec2(noise(p) - 0.5) * 0.008 * ((globalHeat * mask) + (heatFactor * (1 - mask)));

    vec2 largeWave = vec2(sin(texcoord.y * 20.0 + time * 0.7) * cos(texcoord.x * 18.0 + time * 0.5), cos(texcoord.x * 22.0 + time * 0.6) * sin(texcoord.y * 19.0 + time * 0.8));
    vec2 smallWave = vec2(sin(texcoord.y * 80.0 + time * 1.3) * cos(texcoord.x * 75.0 + time * 1.1), cos(texcoord.x * 85.0 + time * 1.2) * sin(texcoord.y * 78.0 + time * 1.4));
    float noise = hash(texcoord + floor(time * 0.5));
    vec2 jitter = vec2(noise - 0.5, hash(texcoord.yx + noise) - 0.5) * 0.3;
    vec2 wav = (largeWave * 0.6 + smallWave * 0.4 + jitter * 0.1) * 0.002 * ((globalHeat * mask) + (heatFactor * (1 - mask)));
    vec2 off = (noi * 0.75) + (wav * 0.75);
    vec2 uv = texcoord + off;

    return vec4(col + texture2D(screenTexture, uv).rgb, 1.0);
}

vec4 modifyColor(vec4 color) {
    return vec4(lerp(color.rgb, vec3(0.8, 0.3, 0.0), (0.1 * globalHeat)) * (1.0 + (0.2 * globalHeat)), color.a);
}

void main() {
    float depth = texture2D(depthTexture, texcoord).r;
    if (depth >= 0.999999) {gl_FragColor = texture2D(screenTexture, texcoord); return;}

    vec4 original = texture2D(screenTexture, texcoord);
    vec3 worldPos = worldPosFromDepth(texcoord);
    vec3 lighting = vec3(0.0);
    float heat = 0;

    for (int i = 0; i != pointLightCount; i++) {
        vec3 par = pointLightParams[i];
        float dist = lenghtSq(worldPos - pointLightPos[i]);
        if (dist > (par.x * 4)) continue;
        float att = computeAtt(dist, 1.0 / par.x);
        vec3 color = computeLight(dist, pointLightColor[i], par.y, att);
        heat += att * att * dist * par.z;
        lighting += color;
    }

    for (int i = 0; i != lineLightCount; i++) {
        vec3 abbMin = lineLightABBMin[i];
        vec3 abbMax = lineLightABBMax[i];
        if (worldPos.x >= abbMin.x && worldPos.y >= abbMin.y && worldPos.z >= abbMin.z && worldPos.x <= abbMax.x && worldPos.y <= abbMax.y && worldPos.z <= abbMax.z) {
            vec3 par = lineLightParams[i];
            float dist = distToLineSq(worldPos, lineLightPosFrom[i], lineLightPosTo[i]);
            if (dist > (par.x * 4)) continue;
            float att = computeAtt(dist, 1.0 / par.x);
            vec3 color = computeLight(dist, lineLightColor[i], par.y, att);
            heat += att * att * dist * par.z;
            lighting += color;
        }
    }

    lighting = lighting / (lighting + 1.0);
    heat = 1.0 - exp(-heat);
    heat *= 1.6;
    float dist = dot(worldPos, worldPos);
    vec4 color;

    color = heatWaves(lighting, dist, heat);
    color = modifyColor(color);

    gl_FragColor = color;
}