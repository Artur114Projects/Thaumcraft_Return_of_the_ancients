#version 120
void main() {
    // обязательно проектировать вершины в экранное пространство
    gl_Position   = gl_ModelViewProjectionMatrix * gl_Vertex;
    // пробрасываем UV в фрагментный
    gl_TexCoord[0] = gl_MultiTexCoord0;
}
