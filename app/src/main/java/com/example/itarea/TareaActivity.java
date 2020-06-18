package com.example.itarea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itarea.MODELOS.ActividadModelo;
import com.example.itarea.MODELOS.MateriaModelo;

import java.util.Timer;
import java.util.TimerTask;

public class TareaActivity extends AppCompatActivity {

    private Spinner materias;
    private EditText etNombre, etDesc;
    private CalendarView calendario;
    private String fecha="";
    private RadioGroup rbG;
    int pos;
    final String listaMaterias[] = new String[20];
    int idG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        Bundle parametros = this.getIntent().getExtras();
        int semestre = Integer.parseInt(parametros.getString("semestre"));
        int idC = Integer.parseInt(parametros.getString("idcarrera"));
        idG = Integer.parseInt(parametros.getString("idGrupo"));

        //Aqui traemos las materias en el spinner
        materias = (Spinner) findViewById(R.id.spnMateria);
        final TextView t = new TextView(this);
        MateriaModelo m = new MateriaModelo(this);
        m.getMateriasBySemestre(idC, semestre, t);//esta funcion le pone las materias al text view
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {//Como es asincrono, hay que darle tiempo para que llegue la respuesta
            @Override
            public void run() {
                String mystring = t.getText().toString();//aca tomamos las materias del text view
                 String arr[] = mystring.split("\n");//Obtenemos los datos del string
                for(int i=0; i<arr.length;i++){
                    listaMaterias[i] = arr[i];
                }
                ArrayAdapter<String> adapter = buildAdapter(arr);
                materias.setAdapter(adapter);
            }
        }, 3000);
        etNombre = (EditText) findViewById(R.id.txtNombreAct);
        etDesc = (EditText) findViewById(R.id.txtDescripcion);
        rbG = (RadioGroup) findViewById(R.id.RadioGroup1);
        calendario = (CalendarView) findViewById(R.id.calendarView);
        calendario.setMinDate(System.currentTimeMillis() - 1000);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fecha = year+"-"+month+"-"+dayOfMonth;//Asi pide la fehca mysql
            }
        });
    }
    //Hola leo
    public void creatarea(View view){
        String nombre = etNombre.getText().toString();
        String desc = etDesc.getText().toString();
        //Obtenemos la materia
        pos = materias.getSelectedItemPosition();
        String materia = listaMaterias[pos];
        //Obtenemos el tipo
        int radioButtonId = rbG.getCheckedRadioButtonId();
        View radioButton = rbG.findViewById(radioButtonId);
        int indice = rbG.indexOfChild(radioButton);
        String tipo="";
        if(indice == 0 || indice== 1 || indice == 2){
            RadioButton rb = (RadioButton)  rbG.getChildAt(indice);
            tipo = rb.getText().toString();
        }

        if(nombre.equals("") || tipo.equals("")){
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
        } else if(fecha.equals(""))
            Toast.makeText(this, "Selecciona una fecha valida", Toast.LENGTH_SHORT).show();
        else {
            ActividadModelo actividad = new ActividadModelo(TareaActivity.this);
            actividad.crearTarea(nombre, tipo, fecha, desc, materia, idG);
            Toast.makeText(this, "Creando tarea...", Toast.LENGTH_SHORT).show();
            TimerTask tarea = new TimerTask() {
                @Override
                public void run() {
                    TareaActivity.this.finish();
                }
            };
            Timer tiempo = new Timer();
            tiempo.schedule(tarea,2000);
        }
    }
    public void regresar(View view){
        finish();
    }

    public ArrayAdapter buildAdapter(String n[]){
        ArrayAdapter<String> _adapter = new ArrayAdapter<String>(this, R.layout.spinner_personalizado, n);
        return _adapter;
    }
}