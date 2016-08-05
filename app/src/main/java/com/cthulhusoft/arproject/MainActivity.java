package com.cthulhusoft.arproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.cthulhusoft.arproject.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showMessage(View view) {
        Intent intent = new Intent(this, ShowMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.msg_input);
        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        startActivity(intent);
    }

    public void showSensorData(View view) {
        Intent intent = new Intent(this, SensorDataActivity.class);
        startActivity(intent);
    }

    public void startCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

}
