package com.example.sensorgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnStart = findViewById(R.id.imageButton2);
        final Spinner colorSpinner = findViewById(R.id.colorSpinner);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, labyrinthActivity.class);
                String selectedColor = colorSpinner.getSelectedItem().toString();
                intent.putExtra("ballColor", selectedColor);
                startActivity(intent);
            }
        });
    }
}