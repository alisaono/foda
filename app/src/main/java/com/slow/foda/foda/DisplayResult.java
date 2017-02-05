package com.slow.foda.foda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DisplayResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
    }
    public void startOver(View view) {
        Intent backOriginal = new Intent(this, MainActivity.class);
        //EditText theEditText = (EditText) findViewById(R.id.edit_message);
        //String message = theEditText.getText().toString();
        startActivity(backOriginal);
    }
}
