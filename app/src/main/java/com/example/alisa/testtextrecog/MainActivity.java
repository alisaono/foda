package com.example.alisa.testtextrecog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String API_KEY = "AIzaSyBkNwOBljqDt4GPboj9_IxCFwqB5zqVwmw";

    private TextRecognizer textRecognizer;
    private CameraSource cameraSource;

    private SurfaceView cameraView;
    private TextView textInfo;
    private TextView translation;

    boolean detecting = false;
    boolean detected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        textInfo = (TextView) findViewById(R.id.text_info);
        translation = (TextView) findViewById(R.id.translation);

        textRecognizer = new TextRecognizer
                .Builder(this)
                .build();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraSource = new CameraSource
                    .Builder(this, textRecognizer)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(500, 500)
                    .build();
        } else {
            Log.w(TAG, "Camera permission is not granted.");
        }

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (cameraSource != null &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> texts = detections.getDetectedItems();

                if (detecting && texts.size() != 0) {
                    textInfo.post(new Runnable() {
                        public void run() {
                            textInfo.setText(
                                    texts.valueAt(0).getValue()
                            );
                            detected = true;
                        }
                    });
                }
            }
        });

        final Button detectButton = (Button) findViewById(R.id.detectButton);

        detectButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    detecting = true;
                    detectButton.setText("detecting...");
                    return true;
                } else {
                    detecting = false;
                    detectButton.setText("DETECT");
                    return false;
                }
            }
        });

        final Button translateButton = (Button) findViewById(R.id.translateButton);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detected) {
                    String originalText = textInfo.getText().toString();
                    makeRequestWithOkHttp("https://translation.googleapis.com/language/translate/v2?key="+API_KEY+"&target=en&q="+originalText);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraSource == null) {
            cameraSource = new CameraSource
                    .Builder(this, textRecognizer)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(640, 480)
                    .build();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    private void makeRequestWithOkHttp(String url) {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response)
                    throws IOException {
                try {
                    String jsonData = response.body().string();
                    final String result = new JSONObject(jsonData)
                            .getJSONObject("data")
                            .getJSONArray("translations")
                            .getJSONObject(0)
                            .get("translatedText")
                            .toString();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            translation.setText(result);
                        }
                    });
                } catch (JSONException jse) {
                    jse.printStackTrace();
                }
            }
        });
    }

}

