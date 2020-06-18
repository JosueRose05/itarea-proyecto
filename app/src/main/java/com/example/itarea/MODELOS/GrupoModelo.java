package com.example.itarea.MODELOS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.itarea.DB.DB;
import com.example.itarea.LoginActivity;
import com.example.itarea.MenuActivity;
import com.example.itarea.MySingleton;
import com.example.itarea.PendientesActivity;
import com.example.itarea.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GrupoModelo extends DB {
    private int idGrupo;
    private String nombre;
    private int semestre;
    private int matriculaAdmin;
    private int numAlumnos;
    private int idCarrera;
    Activity activity;


    public GrupoModelo(Activity activity){
        this.idGrupo = -1;
        this.semestre = -1;
        this.matriculaAdmin = -1;
        this.numAlumnos = -1;
        this.idCarrera = -1;
        this.url += "grupo/";
        this.activity = activity;
    }

    public void crearGrupo(String _nombre, int _semestre, int _madmin, int _idCarrera){
        ejecutarWebService(url+"crearGrupo.php?nombre="+_nombre+"&semestre="+_semestre+"&madmin="+_madmin+"&carrera="+_idCarrera, activity);
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor cargar = preferencias.edit();
        cargar.putString("admin", "1");//guardamos que el usuario ya creo un grupo
        cargar.commit();
    }

    public void salirGrupo(int _matricula, int _idGrupo){
        ejecutarWebService(url+"salirGrupo.php?matricula="+_matricula+"&idGrupo="+_idGrupo, activity);
    }

    public void getGrupos(final int _matricula, final LinearLayout botonesLayout){
        StringRequest request = new StringRequest(Request.Method.POST, url+"getGrupos.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("0")){
                    int i = 0;
                    try {
                        JSONArray datos = new JSONArray(new String(response));
                        ArrayList<String> datosGrupos = new ArrayList<String>();
                        while( i< datos.length()){
                            datosGrupos.add(datos.getJSONObject(i).getString("id_grupo"));
                            datosGrupos.add(datos.getJSONObject(i).getString("nombre"));
                            datosGrupos.add(datos.getJSONObject(i).getString("semestre"));
                            datosGrupos.add(datos.getJSONObject(i).getString("id_carrera"));
                            datosGrupos.add(datos.getJSONObject(i).getString("matricula_admin"));
                            i++;
                        }
                        mostrarBotones(botonesLayout, datosGrupos, datosGrupos.size());
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    showEmptyGrupos(botonesLayout);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("matricula", String.valueOf(_matricula));
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(activity).addToRequestQueue(request);
    }

    public void mostrarBotones(LinearLayout botonesLayout, ArrayList datosGrupos, int size){
        //Formato de los botones
        if(size>0){
            for (int i= 0; i<size; i+=5){
                Button boton = new Button(activity);
                //Personalizando botones
                boton.setText(""+datosGrupos.get(i+1));
                boton.setTextColor(Color.WHITE);
                boton.setBackgroundResource(R.drawable.input);
                LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        /*height*/ ViewGroup.LayoutParams.WRAP_CONTENT
                );
                parametros.setMargins(0, 3, 0, 8);
                boton.setLayoutParams(parametros);
                boton.setContentDescription(""+datosGrupos.get(i)+ "_" +datosGrupos.get(i+1)+"_"+datosGrupos.get(i+2)+"_"+datosGrupos.get(i+3)+"_"+datosGrupos.get(i+4));
                boton.setOnClickListener(misEventosButton);
                botonesLayout.addView(boton);
            }
        }
    }

    private View.OnClickListener misEventosButton = new View.OnClickListener() {
        public void onClick(View v) {
            Button botonPrecionado = (Button) v;
            String mystring = String.valueOf(botonPrecionado.getContentDescription());
            String arr[] = mystring.split("_", 5);//Obtenemos los datos del arrayList
            String idG = arr[0];
            String nom = arr[1];
            String sem = arr[2];
            String idC = arr[3];
            String mtrAdmin = arr[4];
            Intent intent = new Intent(activity, PendientesActivity.class);
            intent.putExtra("idGrupo", idG);
            intent.putExtra("nombreG", nom);
            intent.putExtra("semestre", sem);
            intent.putExtra("idCa", idC);
            intent.putExtra("mtrAdmin", mtrAdmin);
            activity.startActivity(intent);
            activity.finish();
        }
    };

    public void showEmptyGrupos(LinearLayout botonesLayout){
        TextView textView = new TextView(activity);
        textView.setText("Aun no estas en ningun grupo");
        textView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                /*height*/ ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(parametros);
        botonesLayout.addView(textView);
    }

    public void borrarGrupo(int m){
        ejecutarWebService(url+"delGrupo.php?idGrupo="+m, activity);
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor cargar = preferencias.edit();
        cargar.putString("admin", "0");//guardamos que el usuario elimino el grupo
        cargar.commit();
    }

    public void ingresarGrupo(int _matricula, int cod){
        ejecutarWebService(url+"entrarGrupo.php?matricula="+_matricula+"&idGrupo="+cod, activity);
        final Intent intent = new Intent(activity, MenuActivity.class);
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                activity.startActivity(intent);
                activity.finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea,1090);
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombre(int _idGrupo) {
        return null;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getMatriculaAdmin() {
        return matriculaAdmin;
    }

    public void setMatriculaAdmin(int matriculaAdmin) {
        this.matriculaAdmin = matriculaAdmin;
    }

    public int getNumAlumnos() {
        return numAlumnos;
    }

    public void setNumAlumnos(int numAlumnos) {
        this.numAlumnos = numAlumnos;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }
}
