#version 120
varying vec2 texcoord;

uniform sampler2D screenTexture;
uniform sampler2D depthTexture;
uniform mat4 invMVPMatrix;

#define MAX_POINT_LIGHTS 32
uniform vec3 pointLightPos[MAX_POINT_LIGHTS];
uniform vec3 pointLightColor[MAX_POINT_LIGHTS];
uniform vec2 pointLightParams[MAX_POINT_LIGHTS];
uniform int pointLightCount;

#define MAX_LINE_LIGHTS 64
uniform vec3 lineLightPosTo[MAX_LINE_LIGHTS];
uniform vec3 lineLightPosFrom[MAX_LINE_LIGHTS];
uniform vec3 lineLightColor[MAX_LINE_LIGHTS];
uniform vec2 lineLightParams[MAX_LINE_LIGHTS];
uniform int lineLightCount;


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

vec3 computeLight(float distSq, vec3 color, vec2 params) {
//    float attenuation = 1.0 / (1.0 + distSq * params.x);
    float att = 1.0 / (1.0 + distSq * params.x);
    att *= att;
    return color * att * params.y;
}

void main() {
    if (pointLightCount == 0 && lineLightCount == 0) {gl_FragColor = texture2D(screenTexture, texcoord); return;}
    float depth = texture2D(depthTexture, texcoord).r;
    if (depth >= 0.999999) {gl_FragColor = texture2D(screenTexture, texcoord); return;}

    vec4 original = texture2D(screenTexture, texcoord);
    vec3 worldPos = worldPosFromDepth(texcoord);
    vec3 lighting = vec3(0.0);


    for (int i = 0; i != MAX_POINT_LIGHTS; i++) {
        if (i >= pointLightCount) break;

        vec3 vec = worldPos - pointLightPos[i];
        float dist = dot(vec, vec);
        lighting += computeLight(dist, pointLightColor[i], pointLightParams[i]);
    }

    for (int i = 0; i != MAX_LINE_LIGHTS; i++) {
        if (i >= lineLightCount) break;
        float dist = distToLineSq(worldPos, lineLightPosFrom[i], lineLightPosTo[i]);
        lighting += computeLight(dist, lineLightColor[i], lineLightParams[i]);
    }

    gl_FragColor = vec4(original.rgb + lighting, original.a);
}
