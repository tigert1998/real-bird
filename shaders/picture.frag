#version 410 core

in vec2 vTexCoord;

uniform sampler2D uSampler;

out vec4 fragColor;

void main() {
    fragColor = texture(uSampler, vTexCoord);
}