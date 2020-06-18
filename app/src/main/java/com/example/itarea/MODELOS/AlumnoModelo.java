package com.example.itarea.MODELOS;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.itarea.DB.DB;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class AlumnoModelo extends DB {
    private int matricula;
    private String nombre;
    private String apellidos;
    private boolean admin;
    private String correo;
    private int idCarrera;
    private Activity activity;

    public AlumnoModelo(Activity activity) {
        this.matricula = 0-1;
        this.nombre = null;
        this.apellidos = null;
        this.admin = false;
        this.correo = null;
        this.idCarrera = -1;
        this.url += "alumno/";
        this.activity = activity;
    }

    public String getUrl(){
        return url;
    }

    public void guardarUsuarioEnCelular(String matriculaa,  String nombrea,  String apellidosa,  String correoa,   String carreraa){
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferencias.edit();
        objEditor.putString("matricula", matriculaa);
        objEditor.putString("nombre", nombrea);
        objEditor.putString("apellidos", apellidosa);
        objEditor.putString("admin", "0");
        objEditor.putString("correo", correoa);
        objEditor.putString("carrera", carreraa);

        objEditor.commit();
    }

    public void cargarDatos(String _correo){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url+"getAlumno.php?correo="+_correo,
            new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200){
                        try{
                            String x = new String(responseBody);
                            if(!x.equals("0")) {
                                JSONArray datos = new JSONArray(new String(responseBody));
                                SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor cargar = preferencias.edit();
                                cargar.putString("matricula", datos.getJSONObject(0).getString("matricula"));
                                cargar.putString("nombre", datos.getJSONObject(0).getString("nombre"));
                                cargar.putString("apellidos", datos.getJSONObject(0).getString("apellidos"));
                                cargar.putString("admin", datos.getJSONObject(0).getString("admin"));
                                cargar.putString("carrera", datos.getJSONObject(0).getString("id_carrera"));
                                cargar.commit();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferencias.edit();
        objEditor.putString("correo", _correo);
        objEditor.commit();
    }

    public int getMatricula() {
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return Integer.parseInt(preferencias.getString("matricula", ""));
    }

    public String getNombre() {
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return preferencias.getString("nombre", "");
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return preferencias.getString("apellidos", "");
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String isAdmin() {
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return preferencias.getString("admin", "");
    }

    public String getCorreo() {
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return preferencias.getString("correo", "");
    }

    public int getIdCarrera() {
        SharedPreferences preferencias = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return Integer.parseInt(preferencias.getString("carrera", ""));
    }

}
