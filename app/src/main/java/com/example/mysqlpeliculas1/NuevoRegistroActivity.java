package com.example.mysqlpeliculas1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysqlpeliculas1.clases.ConfiguracionDB;

import java.util.HashMap;
import java.util.Map;


public class NuevoRegistroActivity extends AppCompatActivity {
    private EditText edt_reg_email, edt_reg_clave;
    SharedPreferences sharedpreferences; //mantiene las variables de sesion, usarlas para simular la sesion
    public static final String EMAIL_KEY = "com.example.mysqlpeliculas1.nuevoregistroactivity.email" ;
    public static final String PASSWORD_KEY = "com.example.mysqlpeliculas1.nuevoregistroactivity.password" ;
    public static final String SHARED_PREFS ="com.example.mysqlpeliculas1.nuevoregistroactivity.shared_prefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_registro);
        edt_reg_email = (EditText)findViewById(R.id.edt_login_email);
        edt_reg_clave = (EditText)findViewById(R.id.edt_login_clave);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void registrarse(View view)
    {
        String email = String.valueOf(edt_reg_email.getText());
        String password = String.valueOf(edt_reg_clave.getText());
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/registrar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("registro ok")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(EMAIL_KEY, email);
                            editor.putString(PASSWORD_KEY, password);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Registro existoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al registrarse", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NuevoRegistroActivity.this);
        requestQueue.add(request);
    }
}