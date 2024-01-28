package com.example.mysqlpeliculas1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    //atributos------------------------
    public static final String EMAIL_KEY = "com.example.mysqlpeliculas1.mainactivity.email" ;
    public static final String PASSWORD_KEY = "com.example.mysqlpeliculas1.mainactivity.password" ;
    public static final String SHARED_PREFS ="com.example.mysqlpeliculas1.mainactivity.shared_prefs" ;
    EditText edt_login_email;
    EditText edt_login_password;
    SharedPreferences sharedpreferences; //mantiene las variables de sesion, usarlas para simular la sesion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_login_email = (EditText) findViewById(R.id.edt_login_email);
        edt_login_password = (EditText) findViewById(R.id.edt_login_clave);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void entrar(View view) //login
    {
        String email = String.valueOf(edt_login_email.getText()); //recuperamos email y contraseña
        String password = String.valueOf(edt_login_password.getText());
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/loguear_.php", //crtl+clic en DIRECCION_URL_RAIZ para cambiar los datos por nuestra ip
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("autenticacion exitosa")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(EMAIL_KEY, email);
                            editor.putString(PASSWORD_KEY, password);
                            editor.apply();
                            finish();
                            Toast.makeText(MainActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MostrarPeliculas.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "error en la autenticacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    public void mostrarRegistro(View view) {
        Intent intent = new Intent(this, NuevoRegistroActivity.class);
        startActivity(intent);
    }

    public void salir(View view)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit(); //borro las variables y salgo del logeo
        editor.clear();
        editor.apply();
        Toast.makeText(this," ha cerrado sesión ", Toast.LENGTH_LONG).show();
    }

}