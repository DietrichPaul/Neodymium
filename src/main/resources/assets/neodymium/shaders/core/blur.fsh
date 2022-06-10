#version 320 core

in vec2 vertexPos;
out vec4 fragColor;

uniform vec2 bildschirm;
uniform float radius;

uniform sampler2D framebuffer;

float weight(float d) {
    float gd = 1 - abs(d);
    return (gd * gd) * (3 - 2 * gd);
}

void main() {
    vec2 uv = vertexPos / bildschirm;
    uv.y = 1 - uv.y;

    float sampleSize = 0.0;
    vec4 returnValue = vec4(0.0);
    for (float xr = -radius; xr <= radius; xr += 1.0) {
        for (float yr = -radius; yr <= radius; yr += 1.0) {
            float dist = sqrt(xr * xr + yr * yr) / radius;
            if (dist > 1.0) {
                continue;
            }

            float weight = weight(dist);
            sampleSize += weight;
            returnValue += texture(framebuffer, uv + (vec2(xr, yr) / bildschirm.xy)) * weight;
        }
    }
    fragColor = returnValue / sampleSize;
}