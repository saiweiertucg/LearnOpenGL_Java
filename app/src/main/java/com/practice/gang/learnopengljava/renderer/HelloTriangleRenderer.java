package com.practice.gang.learnopengljava.renderer;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by gang on 2018/12/22.
 */

public class HelloTriangleRenderer implements GLSurfaceView.Renderer {

    private float[] data = {
            -0.5f, -0.5f, 0.0f, // Left
            0.5f, -0.5f, 0.0f, // Right
            0.0f, 0.5f, 0.0f  // Top
    };

    private static final String TAG = "HelloTriangleRenderer";

    private FloatBuffer vertexDataBuffer;
    private int[] vao = new int[1];
    private int mProgram;

    private static final String VERTEXCODE = ""
            + "#version 300 es\n"
            + "in vec3 vPosition;\n"
            + "void main() {\n"
            + "  gl_Position = vec4(vPosition.x, vPosition.y, vPosition.z, 1.0);\n"
            + "}\n";

    private static final String FRAGMENTCODE = ""
            + "#version 300 es\n"
            + "precision mediump float;\n"
            + "out vec4 fragColor;\n"
            + "void main() {\n"
            + "  fragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
            + "}\n";

    public HelloTriangleRenderer() {
        vertexDataBuffer = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexDataBuffer.put(data, 0, data.length).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int vertexShader = createShader(GLES30.GL_VERTEX_SHADER, VERTEXCODE);
        int fragmentShader = createShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENTCODE);
        mProgram = createProgram(vertexShader, fragmentShader);

        createVAO(vao);

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    }

    private int createShader(int type, String code) {
        int shader = 0;

        shader = GLES30.glCreateShader(type);
        if (shader == 0) {
            Log.e(TAG, "create shader failed, code is: " + code);
            return 0;
        }

        GLES30.glShaderSource(shader, code);
        GLES30.glCompileShader(shader);
        int[] compile = new int[1];
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compile, 0);
        if (compile[0] == 0) {
            Log.e(TAG, "gl compile failed, info: " + GLES30.glGetShaderInfoLog(shader));
            GLES30.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

    public int createProgram(int vertexShader, int frgmentShader) {
        int program;

        program = GLES30.glCreateProgram();
        if (program == 0) {
            Log.e(TAG, "create program fail");
            return 0;
        }

        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, frgmentShader);
        GLES30.glLinkProgram(program);
        int[] linkState = new int[1];
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkState, 0);
        if (linkState[0] == 0) {
            Log.e(TAG, "link program failed, info: " + GLES30.glGetProgramInfoLog(program));
            GLES30.glDeleteProgram(program);
            return 0;
        }
        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(frgmentShader);
        return program;
    }

    private void createVAO(int[] vao) {
        int[] vbo = new int[1];
        GLES30.glGenVertexArrays(1, vao, 0);
        GLES30.glGenBuffers(1, vbo, 0);
        // Bind the Vertex Array Object first, then bind and set vertex buffer(s) and attribute pointer(s).
        GLES30.glBindVertexArray(vao[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4, vertexDataBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3 * 4, 0);
        GLES30.glEnableVertexAttribArray(0);

        // Note that this is allowed, the call to glVertexAttribPointer registered VBO as the currently bound vertex buffer object
        // so afterwards we can safely unbind
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        // Unbind VAO (it's always a good thing to unbind any buffer/array to prevent strange bugs)
        GLES30.glBindVertexArray(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Clear the color buffer
        GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        //Use the program object
        GLES30.glUseProgram(mProgram);

        GLES30.glBindVertexArray(vao[0]);
        draw();
        GLES30.glBindVertexArray(0);
    }

    private void draw() {
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
    }
}
