package com.a3louq.sci_tab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void getDetails(View view) {
        TextView txt = (TextView) view ;
        String symbol = txt.getText().toString().split("\n")[1];

        Toast.makeText(MainActivity.this, symbol, Toast.LENGTH_SHORT).show();


    }

}
