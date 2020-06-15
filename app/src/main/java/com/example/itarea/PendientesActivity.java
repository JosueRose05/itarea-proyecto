package com.example.itarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itarea.MODELOS.AlumnoModelo;
import com.example.itarea.MODELOS.GrupoModelo;

import java.util.Timer;
import java.util.TimerTask;

public class PendientesActivity extends AppCompatActivity {

    TextView tvTitulo;
    Button btnDelGrupo, btnGetId;
    int idGrupo;
    String semestre;
    String idCarrera;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);

        tvTitulo = (TextView) findViewById(R.id.tvActividades);
        Bundle parametros = this.getIntent().getExtras();
        semestre = parametros.getString("semestre");
        idGrupo = Integer.parseInt(parametros.getString("idGrupo"));
        String nombreG = parametros.getString("nombreG");
        idCarrera = parametros.getString("idCa");
        tvTitulo.setText(tvTitulo.getText().toString() + "\n"+ nombreG);
        //Si podemos obtener los datos basicos del grupo, nos ahorramos una consulta
        btnDelGrupo = (Button) findViewById(R.id.btnDelGrupo);
        btnGetId = (Button) findViewById(R.id.btnGetCodigo);
        AlumnoModelo alumno = new AlumnoModelo(this);
        if(alumno.getMatricula() == Integer.parseInt(parametros.getString("mtrAdmin"))){
            btnDelGrupo.setVisibility(View.VISIBLE);
            btnGetId.setVisibility(View.VISIBLE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_salon, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case R.id.itmAnadir:
                Intent intent = new Intent(PendientesActivity.this, TareaActivity.class);
                intent.putExtra("semestre", semestre);
                intent.putExtra("idcarrera", idCarrera);
                intent.putExtra("idGrupo", String.valueOf(idGrupo));
                startActivity(intent);//No hay que terminar esta activity, si no vale verga
                break;
            case R.id.itmCerrar:
                SharedPreferences preferencias = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor objEditor = preferencias.edit();
                objEditor.putString(getResources().getString(R.string.prefStatus),"logout");
                objEditor.apply();
                startActivity(new Intent(PendientesActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editar(View view){
        Intent intent = new Intent(PendientesActivity.this, TareaActivity.class);
        startActivity(intent);
        finish();
    }
    public void regresar(View view){
        Intent intent = new Intent(PendientesActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
    public void actividades(View view){
        Intent intent = new Intent(PendientesActivity.this, ActividadActivity.class);
        startActivity(intent);
        finish();
    }

    public void borrarGrupo(View view){
        GrupoModelo grupo = new GrupoModelo(this);
        grupo.borrarGrupo(idGrupo);
        final Intent intent = new Intent(this, MenuActivity.class);
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea,1090);
    }

    public void getCodigoInv(View view){
        Toast.makeText(this, "Codigo de invitacion: "+idGrupo, Toast.LENGTH_LONG).show();
    }
}
