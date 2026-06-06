#version 120
uniform sampler2D screenTexture;
uniform sampler2D depthTexture;
uniform sampler2D shaderArea;
uniform mat4 invProjMatrix;
uniform vec2 texelSize;
varying vec2 texcoord;
float edgeThreshold = 0.5;

float distance(vec2 uv) {
    float depthRaw = texture2D(depthTexture, uv).r;
    vec3 ndc = vec3(uv * 2.0 - 1.0, depthRaw * 2.0 - 1.0);
    vec4 viewH = invProjMatrix * vec4(ndc, 1.0);
    vec3 viewPos = viewH.xyz / viewH.w;
    return length(viewPos);
}

void main() {
    vec4 texture = texture2D(screenTexture, texcoord);

    if (texture2D(shaderArea, texcoord).rgba == 1.0) {
        float dC = distance(texcoord);
        float dT = distance(texcoord + vec2(0.0, texelSize.y));
        float dL = distance(texcoord + vec2(-texelSize.x, 0.0));
        float dR = distance(texcoord + vec2(texelSize.x, 0.0));
        float dB = distance(texcoord + vec2(0.0, -texelSize.y));
        float dTL = distance(texcoord + vec2(-texelSize.x, texelSize.y));
        float dTR = distance(texcoord + vec2(texelSize.x, texelSize.y));
        float dBL = distance(texcoord + vec2(-texelSize.x, -texelSize.y));
        float dBR = distance(texcoord + vec2(texelSize.x, -texelSize.y));

        float gx = (dTR + 2.0 * dR + dBR) - (dTL + 2.0 * dL + dBL);
        float gy = (dBL + 2.0 * dB + dBR) - (dTL + 2.0 * dT + dTR);
        float edge = sqrt(gx * gx + gy * gy);

        if (edge > edgeThreshold) {
            gl_FragColor = vec4((vec3(173, 10, 189) / 255.0) * (edge / 10.0), 1.0);
        } else {
            gl_FragColor = vec4(vec3(0.0), texture.a);
        }
    } else {
        gl_FragColor = texture;
    }
}