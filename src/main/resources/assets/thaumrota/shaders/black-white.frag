#version 120
varying vec2 texcoord;
varying vec4 color;
uniform sampler2D texture; // текстура полигона
uniform float time;

void main() {
    vec4 original = texture2D(texture, texcoord); // получаем оригинальный цвет текстуры
    float d = ((original.r + original.b + original.g) / 3.0) * 0.8; // вычисляем яркость
    gl_FragColor = vec4(d, d, d, color.a);
}