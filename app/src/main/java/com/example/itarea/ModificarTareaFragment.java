package com.example.itarea;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.itarea.MODELOS.ActividadModelo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModificarTareaFragment extends Fragment {

    TextView tMateria;
    ImageButton close;
    Activity estaActividad = getActivity();
    ActividadModelo actividad = new ActividadModelo(estaActividad);
    EditText etNombre, etDescripcion, etDia, etMes, etAno;
    Button btnAct;
    RadioButton rbT, rbP, rbE;

    public ModificarTareaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View fragTarea = inflater.inflate(R.layout.fragment_modificar_tarea, container, false);

        etNombre = (EditText) fragTarea.findViewById(R.id.txtNombreAct);
        etDescripcion = (EditText) fragTarea.findViewById(R.id.txtDescripcion);
        tMateria = (TextView) fragTarea.findViewById(R.id.txtMateria);
        rbT = (RadioButton) fragTarea.findViewById(R.id.rbTarea);
        rbP = (RadioButton) fragTarea.findViewById(R.id.rbProyecto);
        rbE = (RadioButton) fragTarea.findViewById(R.id.rbExamen);
        etDia = (EditText)  fragTarea.findViewById(R.id.etDia);
        etMes = (EditText)  fragTarea.findViewById(R.id.etMes);
        etAno = (EditText)  fragTarea.findViewById(R.id.etAno);
        btnAct = (Button) fragTarea.findViewById(R.id.btnAct);
        close = (ImageButton) fragTarea.findViewById(R.id.btnCerrar);

        int idA =  getArguments().getInt("idAct");
        int idM =  getArguments().getInt("idM");
        getMateriaById(idA, idM);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(ModificarTareaFragment.this).commitAllowingStateLoss();
            }
        });//

        btnAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass
            }
        });

        return fragTarea;
    }



    public void getMateriaById(final int _idA, final int _idM){
        String url = actividad.getUrl();
        StringRequest request = new StringRequest(Request.Method.POST, url+"getTareaById.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("0")){
                    try {
                        JSONArray datos = new JSONArray(new String(response));
                            etNombre.setText(datos.getJSONObject(0).getString("nombre"));
                            etDescripcion.setText(datos.getJSONObject(0).getString("descripcion"));
                            tMateria.setText("Materia: "+datos.getJSONObject(0).getString("materia"));

                            String tipo = datos.getJSONObject(0).getString("tipo");
                            if(tipo.equals("Tarea"))
                                rbT.setChecked(true);
                            else if(tipo.equals("Proyecto"))
                                rbP.setChecked(true);
                            else
                                rbE.setChecked(true);

                            String fechaCommpleta = datos.getJSONObject(0).getString("fecha_limite");
                            String arr[] = fechaCommpleta.split("-", 3);//Obtenemos los datos del arrayList
                            etDia.setText(arr[2]);
                            etMes.setText(arr[1]);
                            etAno.setText(arr[0]);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
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
                param.put("idAct", String.valueOf(_idA));
                param.put("idM", String.valueOf(_idM));
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(estaActividad).addToRequestQueue(request);
    }
}
