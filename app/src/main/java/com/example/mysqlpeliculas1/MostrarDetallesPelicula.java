package com.example.mysqlpeliculas1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.mysqlpeliculas1.clases.Pelicula;
import com.example.mysqlpeliculas1.recyclerview.PeliculaViewHolder;
import com.example.mysqlpeliculas1.utilidades.ImagenesBlobBitmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MostrarDetallesPelicula extends AppCompatActivity {
    //atributos--------------------------
    private EditText edt_detalles_id, edt_detalles_titulo, edt_detalles_genero;
    private ImageView img_detalles_foto_pelicula;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    private Pelicula pelicula;

    //metodos----------------------------
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        String email = sharedpreferences.getString(MainActivity.EMAIL_KEY, null);
        String password = sharedpreferences.getString(MainActivity.PASSWORD_KEY, null);
        if(email== null || password==null)
        {
            Toast.makeText(getApplicationContext(), "Debes loguearte", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_detalles_pelicula);

        edt_detalles_id = (EditText) findViewById(R.id.edt_edit_id);
        edt_detalles_titulo = (EditText) findViewById(R.id.edt_edit_titulo);
        edt_detalles_genero = (EditText) findViewById(R.id.edt_edit_genero);
        img_detalles_foto_pelicula = (ImageView) findViewById(R.id.img_edit_foto_pelicula);


        Intent intent = getIntent();
        if(intent != null)
        {
            pelicula = (Pelicula) intent.getSerializableExtra(PeliculaViewHolder.EXTRA_DETALLES_PELICULA);
            //cargar la foto
            byte[] fotobinaria = (byte[]) intent.getByteArrayExtra(PeliculaViewHolder.EXTRA_DETALLES_IMAGEN_PELICULA);
            Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, ConfiguracionDB.ancho_imagen,ConfiguracionDB.alto_imagen);
            img_detalles_foto_pelicula.setImageBitmap(fotobitmap);
        }
        else{
            pelicula = new Pelicula();
        }
        edt_detalles_titulo.setText(pelicula.getTitulo());
        edt_detalles_genero.setText(pelicula.getGenero());
        edt_detalles_id.setText(String.valueOf(pelicula.getIdPelicula()));
    }

    public void borrar_pelicula(View view){
        String idPelicula = pelicula.getIdPelicula().toString();
        borrar_datos_pelicula(idPelicula);
        borrar_foto(idPelicula);
    }

    private void borrar_datos_pelicula(String idPelicula) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/eliminar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos eliminados")) {
                            Toast.makeText(MostrarDetallesPelicula.this, "Película eliminada", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MostrarPeliculas.class));

                        } else {
                            Toast.makeText(MostrarDetallesPelicula.this, "No se pudo eliminar la película", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesPelicula.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("idPelicula",idPelicula);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesPelicula.this);
        requestQueue.add(request);
    }


    private void borrar_foto(String idPelicula) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/eliminar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Foto eliminada")) {
                            Toast.makeText(MostrarDetallesPelicula.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MostrarDetallesPelicula.this, "Error no se puede eliminar la foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesPelicula.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idPelicula",idPelicula);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesPelicula.this);
        requestQueue.add(request);

    }

    public void editar_pelicula(View view){
        editar_datos_pelicula(pelicula);
        editar_foto_pelicula(pelicula, img_detalles_foto_pelicula);
    }

    private void editar_datos_pelicula(Pelicula p1) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/actualizar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos actualizados")) {
                            Toast.makeText(MostrarDetallesPelicula.this, "Película actualizada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MostrarPeliculas.class));
                            finish();
                        } else {
                            Toast.makeText(MostrarDetallesPelicula.this, "Edición fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesPelicula.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idPelicula",edt_detalles_id.getText().toString());
                params.put("titulo",edt_detalles_titulo.getText().toString());
                params.put("genero",edt_detalles_genero.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesPelicula.this);
        requestQueue.add(request);
    }


    private void editar_foto_pelicula(Pelicula p1, ImageView img_detalles_imagenp) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/actualizar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos actualizados")) {
                            Toast.makeText(MostrarDetallesPelicula.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MostrarDetallesPelicula.this, "Error al actualizar la foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesPelicula.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idPelicula",p1.getIdPelicula());
                img_detalles_imagenp.buildDrawingCache();
                Bitmap foto_bm = img_detalles_imagenp.getDrawingCache();
                byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
                String fotostring = ImagenesBlobBitmap.byte_to_string(fotobytes);
                params.put("foto",fotostring);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesPelicula.this);
        requestQueue.add(request);
    }


    public void mostrar_selector_imagenes(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                img_detalles_foto_pelicula.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}