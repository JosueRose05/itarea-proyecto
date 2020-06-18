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
