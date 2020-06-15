package com.example.itarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActividadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
    }
    public void regresar(View view){
        Intent intent = new Intent(ActividadActivity.this, PendientesActivity.class);
        startActivity(intent);
        finish();
    }
}
