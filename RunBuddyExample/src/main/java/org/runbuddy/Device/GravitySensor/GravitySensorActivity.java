package org.runbuddy.Device.GravitySensor;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.TextView;
import org.runbuddy.Device.CounterSensor.SensorHub;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Johnny Chow on 2016/8/15.
 */
public class GravitySensorActivity extends Activity implements SensorHub.DataClient {

    private TextView mtextView;
    GLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(getApplication());
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                //AccelerometerGraphJNI.surfaceCreated();
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        });

    }


    @Override
    public void onData(SensorEvent event, String text) {

    }
}
