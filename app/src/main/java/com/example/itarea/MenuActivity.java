package com.example.itarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.itarea.MODELOS.AlumnoModelo;
import com.example.itarea.MODELOS.GrupoModelo;

public class MenuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private EditText CodI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        LinearLayout botonesLayout = (LinearLayout) findViewById(R.id.botoneGrupos);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        AlumnoModelo alumno = new AlumnoModelo(MenuActivity.this);
        GrupoModelo grupos = new GrupoModelo(MenuActivity.this);
        //Mostramos los grupos en la activity
        grupos.getGrupos(alumno.getMatricula(), botonesLayout);
        CodI = (EditText) findViewById(R.id.txtCodInv);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_salon, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.itmAnadir:
                SharedPreferences preferencias = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                String adm = preferencias.getString("admin", "");
                if(adm.equals("0")){
                    Intent intent = new Intent(MenuActivity.this, CreaSalonActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(this, "Ya eres administrador de un grupo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.itmCerrar:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getResources().getString(R.string.prefStatus),"logout");
                editor.apply();
                startActivity(new Intent(MenuActivity.this,LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ingresarSalon(View view){
        AlumnoModelo alumno = new AlumnoModelo(this);
        GrupoModelo grupo = new GrupoModelo(this);
        int codigo = Integer.parseInt(CodI.getText().toString());
        grupo.ingresarGrupo(alumno.getMatricula(), codigo);
    }
}