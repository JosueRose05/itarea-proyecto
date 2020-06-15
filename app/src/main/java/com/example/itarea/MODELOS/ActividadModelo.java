package com.example.itarea.MODELOS;

import android.app.Activity;

import com.example.itarea.DB.DB;

public class ActividadModelo extends DB {
    private int idActividad;
    private String nombre;
    private String tipo;
    private String fecha;
    private String descripcion;
    private String idmMateria;
    private int idGrupo;
    Activity activity;

    public ActividadModelo(Activity a){
        this.url += "tareas/";
        this.activity = a;
    }

    public void crearTarea(String nombre, String tipo, String fechaL, String desc, String materia, int idG){
        //nombre, tipo, fecha_limite, descripcion, id_materia, id_grupo
        ejecutarWebService(url+"crearTarea.php?nombre="+nombre+"&tipo="+tipo+"&fecha="+fechaL+"&descripcion="+desc+"&materia="+materia+"&id_grupo="+idG+"", activity);
    }



}
