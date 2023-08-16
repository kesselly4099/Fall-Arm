package com.example.project4;


import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    EditText fullName;
    Button registerBtn;
    private static final String PREFERENCE_NAME = "your_preference_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName = findViewById(R.id.fullName);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = fullName.getText().toString();
                String patientID = generateUniqueID();

                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("patient_name_key", patientName);
                editor.putString("patient_id_key", patientID);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    public String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}