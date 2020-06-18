package com.example.itarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itarea.MODELOS.AlumnoModelo;
import com.example.itarea.MODELOS.GrupoModelo;
import com.example.itarea.MODELOS.MateriaModelo;

import java.util.Timer;
import java.util.TimerTask;

public class CreaSalonActivity extends AppCompatActivity {

    private Spinner spnSemestres;
    private EditText etNombre;
    private TextView mostrar;
    AlumnoModelo alumno = new AlumnoModelo(CreaSalonActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_salon);

        etNombre = (EditText) findViewById(R.id.txtNombreSalon);
        mostrar = (TextView) findViewById(R.id.tvMostrarMaterias);

        spnSemestres = (Spinner) findViewById(R.id.spnSemestre);
        String listaSemestres[] = {"1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_personalizado, listaSemestres);
        spnSemestres.setAdapter(adapter);
        spnSemestres.setSelection(-1);

        spnSemestres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                int semestre = Integer.parseInt(selectedItemText);
                if(i>=0){
                    //Poner}mos las materias de ese semestre en el TextView
                    int carrera = alumno.getIdCarrera();
                    MateriaModelo materias = new MateriaModelo(CreaSalonActivity.this);
                    //Esta funcion pone en el TextView las materias del semestre
                    materias.getMateriasBySemestre(carrera, semestre, mostrar);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void creaSalon(View view){
        int matricula = alumno.getMatricula();
        String nombre = etNombre.getText().toString();
        int semestre = Integer.parseInt(spnSemestres.getSelectedItem().toString());
        int carrera = alumno.getIdCarrera();
        GrupoModelo grupo = new GrupoModelo(CreaSalonActivity.this);
        grupo.crearGrupo(nombre, semestre, matricula, carrera);
        final Intent intent = new Intent(CreaSalonActivity.this, MenuActivity.class);
        Toast.makeText(this, "Creando...", Toast.LENGTH_SHORT).show();
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea,2090);
    }
    public void regresar(View view){
        Intent intent = new Intent(CreaSalonActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
