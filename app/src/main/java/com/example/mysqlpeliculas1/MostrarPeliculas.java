package com.example.mysqlpeliculas1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysqlpeliculas1.clases.ConfiguracionDB;
import com.example.mysqlpeliculas1.clases.Pelicula;
import com.example.mysqlpeliculas1.recyclerview.ListaPeliculasAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MostrarPeliculas extends AppCompatActivity {
    public  ArrayList<Pelicula> peliculas;
    public ListaPeliculasAdapter adapter;
    private RecyclerView rv_peliculas;
    private EditText edt_buscar_pelicula;

    protected void onStart() {
        super.onStart();
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        String email = sharedpreferences.getString(MainActivity.EMAIL_KEY, null);
        String password = sharedpreferences.getString(MainActivity.PASSWORD_KEY, null);
        if(email== null || password==null)
        {
            Toast.makeText(getApplicationContext(), "debes loguearte", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creamos rv y cargamos lista peliculas
        setContentView(R.layout.activity_mostrar_peliculas);
        rv_peliculas = (RecyclerView) findViewById(R.id.rv_Peliculas);
        edt_buscar_pelicula = (EditText) findViewById(R.id.edt_buscar_titulo2);
        peliculas = new ArrayList<Pelicula>();
        adapter = new ListaPeliculasAdapter(this,peliculas);
        rv_peliculas.setAdapter(adapter);
        mostrarPeliculas();

        //establecemos orientacion
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            rv_peliculas.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            // In portrait
            rv_peliculas.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void mostrarPeliculas() {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ+ "/mostrar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        peliculas.clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String exito=jsonObject.getString("exito");
                            JSONArray jsonArray =jsonObject.getJSONArray("peliculas");

                            if (exito.equals("1")){
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String idPelicula = object.getString("idPelicula");
                                    String titulo = object.getString("titulo");
                                    String genero = object.getString("genero");;
                                    Pelicula p1 = new Pelicula(idPelicula, titulo, genero);
                                    peliculas.add(p1);
                                }
                                adapter.setPeliculas(peliculas);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("mysql1","error al pedir los datos");
            }
        }
        ){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarPeliculas.this);
        requestQueue.add(request);
    }

    public void mostrarAddPelicula(View view) {
        Intent intent = new Intent(this, AddPeliculaActivity.class);
        startActivity(intent);
        finish();
    }

    public void buscarPeliculas(View view) {
        String textoBusqueda = String.valueOf(edt_buscar_pelicula.getText());
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ+ "/mostrar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        peliculas.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String exito=jsonObject.getString("exito");
                            JSONArray jsonArray =jsonObject.getJSONArray("peliculas");

                            if (exito.equals("1")){
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String idPelicula = object.getString("idPelicula");
                                    String titulo = object.getString("titulo");
                                    String genero = object.getString("genero");
                                    Pelicula p1 = new Pelicula(idPelicula, titulo, genero);
                                    if(p1.getTitulo().contains(textoBusqueda)) {
                                        peliculas.add(p1);
                                    }
                                }
                                adapter.setPeliculas(peliculas);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarPeliculas.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i("mysql1","error al pedir los datos");
            }
        }
        ){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarPeliculas.this);
        requestQueue.add(request);
    }
}