package com.slow.foda.foda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showResult(View view){
        Intent newResult = new Intent(this, DisplayResult.class);
        //EditText theEditText = (EditText) findViewById(R.id.edit_message);
        //String message = theEditText.getText().toString();
        startActivity(newResult);
    }
}
