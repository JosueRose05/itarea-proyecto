package com.example.itarea.MODELOS;

import android.app.Activity;
import android.widget.TextView;
import com.example.itarea.DB.DB;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class MateriaModelo extends DB implements Serializable {
    private int idMateria;
    private String nombre;
    private String apellido;
    private int semestre;
    private int idCarrera;
    protected Activity activity;
    public TextView mostrar;

    public MateriaModelo(Activity activity) {
        this.url += "materias/";
        this.activity = activity;
    }


    public void getMateriasBySemestre(int id_carrera, int semestre, TextView m){
        mostrar = m;
        mostrar.setText("");
        //Consultamos al servidor
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url+"materiaBySemestre.php?carrera="+id_carrera+"&semestre="+semestre,
            new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if(statusCode == 200){
                        try{
                            String x = new String(responseBody);
                            if(!x.equals("0")) {
                                int i = 0;
                                JSONArray materia = new JSONArray(new String(responseBody));
                                while (i < materia.length()) {
                                    mostrar.setText(mostrar.getText().toString() + materia.getJSONObject(i).getString("nombre") + "\n");
                                    i++;
                                }//while
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
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }
}
