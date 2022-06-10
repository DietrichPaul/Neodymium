#version 150

in vec2 vertexPos;
in vec4 vertexColor;
out vec4 fragColor;

uniform vec2 startBounds;
uniform vec2 stopBounds;
uniform float radius;
uniform float glow;

void main() {
    float dist = distance(vec2(clamp(vertexPos.x, startBounds.x + radius, stopBounds.x - radius), clamp(vertexPos.y, startBounds.y + radius, stopBounds.y - radius)), vertexPos);
    if (dist < radius) {
        fragColor = vec4(vertexColor.r, vertexColor.g, vertexColor.b, vertexColor.a * ((radius - dist) / glow));
    }
}