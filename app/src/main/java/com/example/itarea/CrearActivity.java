package com.example.itarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.itarea.MODELOS.AlumnoModelo;

import java.util.HashMap;
import java.util.Map;

public class CrearActivity extends AppCompatActivity {

    private Spinner carreras;
    EditText matricula,nombre,apellidos,correo,contrasena;
    Button register;
    String helper;
    AlumnoModelo alumno = new AlumnoModelo(CrearActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        carreras = (Spinner) findViewById(R.id.spnCarrera);

        String listaCarreras[] = {"Desarrollo de Software", "Industrial", "Mecatronica", "Diseño Electronico"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_personalizado, listaCarreras);
        carreras.setAdapter(adapter);

        matricula = findViewById(R.id.txtMatricula);
        nombre = findViewById(R.id.txtNombre);
        apellidos = findViewById(R.id.txtApellidos);
        correo = findViewById(R.id.txtCorreo);
        contrasena = findViewById(R.id.txtContrasena);
        register = findViewById(R.id.btnCrearCuenta);

        carreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                helper = "";

                String selectedItemText = (String) parentView.getItemAtPosition(position);

                if(selectedItemText.equals("Desarrollo de Software")){
                    helper="1";
                }else if(selectedItemText.equals("Industrial")){
                    helper="3";
                }else if(selectedItemText.equals("Mecatronica")){
                    helper="2";
                }else if(selectedItemText.equals("Diseño Electronico")){
                    helper="4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estado = verificarCampos();
                if(estado == 1){
                    final String matriculaa = matricula.getText().toString();
                    final String nombrea = nombre.getText().toString();
                    final String apellidosa = apellidos.getText().toString();
                    final String correoa = correo.getText().toString();
                    final String contrasenaa = contrasena.getText().toString();
                    final String carreraa = helper;
                    register(matriculaa,nombrea,apellidosa,correoa,contrasenaa,carreraa);
                }
            }
        });
    }

    private void register(final String matriculaa, final String nombrea, final String apellidosa, final String correoa, final String contrasenaa,final String carreraa){
        String uRl = alumno.getUrl() + "registrar_alumno.php";

        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Usuario registrado")){
                    alumno.guardarUsuarioEnCelular(matriculaa, nombrea, apellidosa,  correoa, carreraa);
                    Toast.makeText(CrearActivity.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(CrearActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CrearActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("matricula",matriculaa);
                param.put("nombre",nombrea);
                param.put("apellidos",apellidosa);
                param.put("correo",correoa);
                param.put("contrasena",contrasenaa);
                param.put("carrera",carreraa);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(CrearActivity.this).addToRequestQueue(request);
    }

    public void Regresar(View view){
        Intent intent = new Intent(CrearActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public int verificarCampos(){
        int estado = 1;
        String campos = "";
        if(matricula.getText().toString().equals("")){
            estado = 0;
            campos += "Matricula ";
        }
        if(nombre.getText().toString().equals("")){
            estado = 0;
            campos += "Nombre ";
        }
        if(apellidos.getText().toString().equals("")){
            estado = 0;
            campos += "apellidos ";
        }
        if(correo.getText().toString().equals("")){
            estado = 0;
            campos += "correo ";
        }
        if(contrasena.getText().toString().equals("")){
            estado = 0;
            campos += "contraseña ";
        }
        if(estado == 0)
            Toast.makeText(this, "Campos "+campos+" requeridos", Toast.LENGTH_SHORT).show();
        return estado;
    }
}
