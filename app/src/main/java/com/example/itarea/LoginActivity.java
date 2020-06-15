package com.example.itarea;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.itarea.MODELOS.AlumnoModelo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText correo,contrasena;
    Button register,login;
    CheckBox checkedStatus;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correo = findViewById(R.id.txtCorreo);
        contrasena = findViewById(R.id.txtContrasena);
        checkedStatus = findViewById(R.id.cbSesion);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefStatus),"");
        if (loginStatus.equals("loggeado")){
            Toast.makeText(LoginActivity.this, loginStatus, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        login = findViewById(R.id.btnIniciar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tx_correo = correo.getText().toString();
                String tx_contrasena = contrasena.getText().toString();
                if (TextUtils.isEmpty(tx_correo) || TextUtils.isEmpty(tx_contrasena)){
                    Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(tx_correo,tx_contrasena);
                }
            }
        });

        register = findViewById(R.id.btnCuenta);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CrearActivity.class));
                finish();
            }
        });
    }

    private void login(final String correo, final String contrasena){
        final AlumnoModelo alumno = new AlumnoModelo(LoginActivity.this);
        String uRl = alumno.getUrl() + "login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                String prueba = res;
                if (prueba.toLowerCase().trim().equals("exito")){
                    alumno.cargarDatos(correo);
                    Toast.makeText(LoginActivity.this, "Bienvenido "+alumno.getNombre(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (checkedStatus.isChecked()){
                        editor.putString(getResources().getString(R.string.prefStatus),"loggeado");
                    }
                    else{
                        editor.putString(getResources().getString(R.string.prefStatus),"logout");
                    }
                    editor.apply();
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("correo",correo);
                param.put("contrasena",contrasena);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(LoginActivity.this).addToRequestQueue(request);
    }
}
