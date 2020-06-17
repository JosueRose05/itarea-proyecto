package com.example.itarea.MODELOS;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.itarea.DB.DB;
import com.example.itarea.MySingleton;
import com.example.itarea.PendientesActivity;
import com.example.itarea.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ActividadModelo extends DB {
    private int idActividad;
    private String nombre;
    private String tipo;
    private String fecha;
    private String descripcion;
    private String idmMateria;
    Activity activity;

    public ActividadModelo(Activity a){
        this.url += "tareas/";
        this.activity = a;
    }

    public void crearTarea(String nombre, String tipo, String fechaL, String desc, String materia, int idG){
        //nombre, tipo, fecha_limite, descripcion, id_materia, id_grupo
        ejecutarWebService(url+"crearTarea.php?nombre="+nombre+"&tipo="+tipo+"&fecha="+fechaL+"&descripcion="+desc+"&materia="+materia+"&id_grupo="+idG+"", activity);
    }

    public void getTareas(final int idGrupo, final int _matricula, final TableLayout tabla){
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
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
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
        MySingleton.getmInstance(activity).addToRequestQueue(request);
    }

    public void mostrarBotones(TableLayout tabla, ArrayList datosActividades, int size){
        //Formato de los botones
        if(size>0){
            for (int i= 0; i<size; i+=3){
                TableRow row = new TableRow(activity);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                row.setId(i);

                Button boton = new Button(activity);
                //Personalizando botones
                boton.setText(""+datosActividades.get(i+1));
                boton.setTextColor(Color.WHITE);

                boton.setBackgroundResource(R.drawable.input);
                TableLayout.LayoutParams parametros = new TableLayout.LayoutParams();
                boton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                //0=id actividad 1=nombre 2=estado
                boton.setContentDescription(""+datosActividades.get(i));
                boton.setOnClickListener(eventoActividad);
                boton.setWidth(550);
                boton.setMaxWidth(550);
                boton.setMinWidth(550);

                //Añadimos el boton de eliminar
                Button borrar  = new Button(activity);
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
                CheckBox checar = new CheckBox(activity);
                checar.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                checar.setBackgroundColor(Color.DKGRAY);
                checar.setTextColor(Color.WHITE);
                checar.setHintTextColor(Color.WHITE);


                checar.setContentDescription(""+datosActividades.get(i+2));
                if(datosActividades.get(i+2).equals("1"))
                    checar.setChecked(true);


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
            String idAct = String.valueOf(botonPrecionado.getContentDescription());
            Intent intent = new Intent(activity, PendientesActivity.class);//Ver como hacemos para que salga esta madre
            intent.putExtra("idGrupo", idAct);
            activity.startActivity(intent);
            activity.finish();
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
            borrarRow = (TableRow) activity.findViewById(Integer.parseInt(idRow));
            borrarRow.setVisibility(View.GONE);
            delTarea(Integer.parseInt(idA));
        }
    };

    public void showEmptyMaterias(TableLayout t){
        TableRow row = new TableRow(activity);
        TextView textView = new TextView(activity);
        textView.setText("No hay actividades");
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(textView);
        t.addView(row);
    }

    public void delTarea(int idTarea){
        ejecutarWebService(url+"deleteTarea.php?idTarea="+idTarea, activity);
    }



}
