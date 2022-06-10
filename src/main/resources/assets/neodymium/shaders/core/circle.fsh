#version 150

in vec2 vertexPos;
in vec4 vertexColor;
out vec4 fragColor;

uniform vec2 center;
uniform float radius;
uniform float glow;

void main() {
    vec2 delta = center - vertexPos;
    float dist = sqrt(delta.x * delta.x + delta.y * delta.y);
    if (dist > radius) {
        discard;
    }

    float antialias = 1.0;

    if (dist > radius - glow) {
        antialias = (radius - dist) / glow;
    }

    fragColor = vertexColor * antialias;
}