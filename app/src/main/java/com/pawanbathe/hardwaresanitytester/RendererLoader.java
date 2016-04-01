package com.pawanbathe.hardwaresanitytester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by pbathe on 2/4/16.
 */
public class RendererLoader extends Activity {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences("GPUinfo", Context.MODE_PRIVATE);
        mGLView = new GLSurfaceView(this);
        mGLView.setRenderer(new ClearRenderer());
        setContentView(mGLView);

        final Intent intent = new Intent(this, MainActivity.class);
        new CountDownTimer(500, 9999)
        {
            public void onTick(long millisUntilFinished) {
                // Not used
            }
            public void onFinish() {
                startActivity(intent);
                finish();
            }
        }.start();

    }


    private GLSurfaceView mGLView;
    class ClearRenderer implements GLSurfaceView.Renderer {

        Random aleatorio = new Random();
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            float r = 255;//aleatorio.nextFloat();
            float g = 0;//aleatorio.nextFloat();
            float b = 0;//aleatorio.nextFloat();
            gl.glClearColor(r, g, b, 1.0f);

            editor = prefs.edit();
            editor.putString("RENDERER", gl.glGetString(GL10.GL_RENDERER));
            editor.putString("VENDOR", gl.glGetString(GL10.GL_VENDOR));
//            editor.putString("VERSION", gl.glGetString(GL10.GL_VERSION));

//          editor.putString("EXTENSIONS", gl.glGetString(GL10.GL_EXTENSIONS));
            editor.commit();

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }


    }
}