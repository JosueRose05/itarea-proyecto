package com.example.itarea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.itarea.MODELOS.ActividadModelo;
import com.example.itarea.MODELOS.AlumnoModelo;
import com.example.itarea.MODELOS.GrupoModelo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PendientesActivity extends AppCompatActivity {

    TextView tvTitulo;
    Button btnDelGrupo, btnGetId, btnSalir;
    int idGrupo;
    String semestre, idCarrera;
    TableLayout tabla;
    ActividadModelo actividades = new ActividadModelo(this);
    AlumnoModelo alumno = new AlumnoModelo(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendientes);

        btnDelGrupo = (Button) findViewById(R.id.btnDelGrupo);
        btnGetId = (Button) findViewById(R.id.btnGetCodigo);
        tabla = (TableLayout) findViewById(R.id.tablaActividades);
        tvTitulo = (TextView) findViewById(R.id.tvActividades);
        btnSalir = (Button) findViewById(R.id.btnSalirGrupo);

        Bundle parametros = this.getIntent().getExtras();
        idGrupo = Integer.parseInt(parametros.getString("idGrupo"));
        getTareas(idGrupo, alumno.getMatricula(), tabla);//Obtenemos las tareas y las mostramos
        semestre = parametros.getString("semestre");
        idCarrera = parametros.getString("idCa");
        String nombreG = parametros.getString("nombreG");
        tvTitulo.setText(tvTitulo.getText().toString() + "\n"+ nombreG);
        //Si podemos obtener los datos basicos del grupo, nos ahorramos una consulta
        if(alumno.getMatricula() == Integer.parseInt(parametros.getString("mtrAdmin"))){
            btnDelGrupo.setVisibility(View.VISIBLE);
            btnGetId.setVisibility(View.VISIBLE);
        } else
            btnSalir.setVisibility(View.VISIBLE);
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

    public void regresar(View view){
        Intent intent = new Intent(PendientesActivity.this, MenuActivity.class);
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

    public void salirGurpo(View view){
        GrupoModelo grupo = new GrupoModelo(this);
        AlumnoModelo alumno = new AlumnoModelo(this);
        grupo.salirGrupo(alumno.getMatricula(), idGrupo);
        Toast.makeText(this, "Saliendo...", Toast.LENGTH_SHORT).show();
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(PendientesActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea,2500);

    }

    public void modificarTarea(int _idAct, int _idM){
        FragmentManager manejador = getSupportFragmentManager();
        FragmentTransaction transaction = manejador.beginTransaction();
        Fragment mostrar = new ModificarTareaFragment();

        Bundle datos = new Bundle();
        datos.putInt("idAct", _idAct);
        datos.putInt("idM", _idM);
        mostrar.setArguments(datos);
        transaction.replace(R.id.linMostrarFragment, mostrar);
        transaction.commit();
    }

    public void getTareas(final int idGrupo, final int _matricula, final TableLayout tabla){
        String url = actividades.getUrl();
        StringRequest request = new StringRequest(Request.Method.POST, url+"getTareas.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("0")){
                    int i = 0;
                    try {
                        JSONArray datos = new JSONArray(new String(response));
                        ArrayList<String> datosActividades = new ArrayList<String>();
                        while( i< datos.length()){
                            datosActividades.add(datos.getJSONObject(i).getString("id_actividad"));
                            datosActividades.add(datos.getJSONObject(i).getString("nombre"));
                            datosActividades.add(datos.getJSONObject(i).getString("estado"));
                            datosActividades.add(datos.getJSONObject(i).getString("id_materia"));
                            i++;
                        }
                        mostrarBotones(tabla, datosActividades, datosActividades.size());
                        //mostrarBotones(botonesLayout, datosGrupos, datosGrupos.size());
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    showEmptyMaterias(tabla);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("matricula", String.valueOf(_matricula));
                param.put("idGrupo", String.valueOf(idGrupo));
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(this).addToRequestQueue(request);
    }

    public void mostrarBotones(TableLayout tabla, ArrayList datosActividades, int size){
        //Formato de los botones
        if(size>0){
            for (int i= 0; i<size; i+=4){
                final int idAct = Integer.parseInt(String.valueOf(datosActividades.get(i)));
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                row.setId(i);

                Button boton = new Button(this);
                //Personalizando botones
                boton.setText(""+datosActividades.get(i+1));
                boton.setTextColor(Color.WHITE);
                boton.setBackgroundResource(R.drawable.input);
                TableLayout.LayoutParams parametros = new TableLayout.LayoutParams();
                boton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                //0=id actividad 1=nombre 2=estado
                boton.setContentDescription(datosActividades.get(i)+"_"+datosActividades.get(i+3));
                boton.setOnClickListener(eventoActividad);
                boton.setWidth(300);

                //Añadimos el boton de eliminar
                Button borrar  = new Button(this);
                borrar.setText("Borrar");
                borrar.setTextColor(Color.WHITE);
                borrar.setMinWidth(100);
                borrar.setBackgroundResource(R.drawable.input);
                borrar.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                borrar.setContentDescription(""+datosActividades.get(i)+"_"+i);
                borrar.setOnClickListener(eventoBorrar);
                borrar.setWidth(200);
                borrar.setMaxWidth(200);
                borrar.setMinWidth(200);

                //Añadimos el checkbox
                CheckBox checar = new CheckBox(this);
                checar.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                checar.setBackgroundColor(Color.GRAY);
                checar.setContentDescription(""+datosActividades.get(i+2));
                if(datosActividades.get(i+2).equals("1"))
                    checar.setChecked(true);
                checar.setBackgroundColor(Color.DKGRAY);
                checar.setTextColor(Color.WHITE);
                checar.setHintTextColor(Color.WHITE);
                checar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if ( isChecked )
                            actividades.updateStatus(alumno.getMatricula(), idAct, 1);
                        else
                            actividades.updateStatus(alumno.getMatricula(), idAct, 0);
                    }
                });
                row.addView(boton);
                row.addView(borrar);
                row.addView(checar);
                tabla.addView(row);
            }
        }
    }

    private View.OnClickListener eventoActividad = new View.OnClickListener() {
        public void onClick(View v) {
            Button botonPrecionado = (Button) v;
            String mystring = String.valueOf(botonPrecionado.getContentDescription());
            String arr[] = mystring.split("_", 2);//Obtenemos los datos del arrayList
            String idAct = arr[0];
            String idM = arr[1];
            modificarTarea(Integer.parseInt(idAct), Integer.parseInt(idM));
        }
    };

    private View.OnClickListener eventoBorrar = new View.OnClickListener() {
        public void onClick(View v) {
            Button botonPrecionado = (Button) v;
            String idAct = String.valueOf(botonPrecionado.getContentDescription());
            String mystring = String.valueOf(botonPrecionado.getContentDescription());
            String arr[] = mystring.split("_", 2);//Obtenemos los datos del arrayList
            String idA = arr[0];
            String idRow = arr[1];
            TableRow borrarRow;
            borrarRow = (TableRow) findViewById(Integer.parseInt(idRow));
            borrarRow.setVisibility(View.GONE);
            actividades.delTarea(Integer.parseInt(idA));
        }
    };

    public void showEmptyMaterias(TableLayout t){
        TableRow row = new TableRow(this);
        TextView textView = new TextView(this);
        textView.setText("No hay actividades");
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(textView);
        t.addView(row);
    }
}
