#version 120 // версия шейдера
varying vec2 lightCoord;
varying vec2 texcoord; // текстурная координата
varying vec4 color; // цвет вершины

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; // находим позицию вертекса, перемножая его на матрицу проекции
    lightCoord = (gl_MultiTexCoord1.st + vec2(8.0)) / 256.0;
    texcoord = vec2(gl_MultiTexCoord0);
    color = gl_Color;
}