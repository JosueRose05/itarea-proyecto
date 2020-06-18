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

//<<<<<<< HEAD
import androidx.fragment.app.FragmentManager;
//=======
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
//>>>>>>> 926e81a0d3134601f9e48a5936340ba05d7a9183

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        //nombre, tipo, fecha_limite, descripcion, nombre materia, id_grupo
        ejecutarWebService(url+"crearTarea.php?nombre="+nombre+"&tipo="+tipo+"&fecha="+fechaL+"&descripcion="+desc+"&materia="+materia+"&id_grupo="+idG+"", activity);
    }

    public String getUrl(){
        return this.url;
    }

    public void updateActividad(int _idA, String _nombre, String _descripcion, String _tipo, String _fecha){
        ejecutarWebService(url+"updateTarea.php?id="+_idA+"&nombre="+_nombre+"&desc="+_descripcion+"&tipo="+_tipo+"&fecha="+_fecha, activity);
    }

    public void updateStatus(int _matricula, int _idAct, int _status){
        ejecutarWebService(url+"updateStatusTarea.php?matricula="+_matricula+"&idAct="+_idAct+"&status="+_status, activity);
    }

    public void delTarea(int idTarea){
        ejecutarWebService(url+"deleteTarea.php?idTarea="+idTarea, activity);
    }



}
