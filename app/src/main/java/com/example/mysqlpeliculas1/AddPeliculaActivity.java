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
import com.example.mysqlpeliculas1.utilidades.ImagenesBlobBitmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPeliculaActivity extends AppCompatActivity {
    //ATRIBUTOS----------------------------
    private EditText edt_add_titulo, edt_add_genero, edt_add_id;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    ImageView img_add_pelicula;

    @Override
    protected void onStart() {
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
        setContentView(R.layout.activity_add_pelicula);
        edt_add_id = (EditText) findViewById(R.id.edt_add_id);
        edt_add_titulo = (EditText) findViewById(R.id.edt_edit_titulo);
        edt_add_genero = (EditText) findViewById(R.id.edt_edit_genero);
        img_add_pelicula = (ImageView) findViewById(R.id.img_add_pelicula);
        //-------------------------------------------------------------------------

    }


    public void aceptarAddPelicula(View view)
    {
        String identificador = String.valueOf(edt_add_id.getText()).trim();
        String titulo = String.valueOf(edt_add_titulo.getText()).trim();
        String genero = String.valueOf(edt_add_genero.getText()).trim();
        Pelicula p1 = new Pelicula(identificador,titulo,genero);
        //------------------------------------------------------------------------
        insertarPeliculadb(p1);
        Toast.makeText(AddPeliculaActivity.this, "Película añadida correctamente.", Toast.LENGTH_SHORT).show();

        //---------------------- codigo para añadir la foto al MYSQL ------------------------------
        if(imagen_seleccionada != null) {
            insertarFotodb(p1.getIdPelicula(),img_add_pelicula);
            Toast.makeText(AddPeliculaActivity.this, "Foto añadida correctamente.", Toast.LENGTH_SHORT).show();
        }
//        Intent intent = new Intent(this, MostrarPeliculas.class);//-----------------------------------------------------------prueba
//        startActivity(intent);

    }

    //-----------------------------------------------------------------------------------------------------------------

    private void insertarPeliculadb(Pelicula p1) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/insertar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Datos insertados")) {
                            Toast.makeText(AddPeliculaActivity.this, "Añadida correctamente", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MostrarPeliculas.class));
                            finish();
                        } else {
                            Toast.makeText(AddPeliculaActivity.this, "No se pudo insertar la película", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPeliculaActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idPelicula",p1.getIdPelicula());
                params.put("titulo",p1.getTitulo());
                params.put("genero",String.valueOf(p1.getGenero()));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddPeliculaActivity.this);
        requestQueue.add(request);
    }
    //-----------------------------------------------------------------------------------------------------------------
    private void insertarFotodb(String idPelicula, ImageView imgNuevopFoto) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/insertar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("foto insertada")) {
                            Toast.makeText(AddPeliculaActivity.this, "Foto insertada correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AddPeliculaActivity.this, "No se pudo insertar la foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPeliculaActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("idPelicula",idPelicula);
                imgNuevopFoto.buildDrawingCache();
                Bitmap foto_bm = imgNuevopFoto.getDrawingCache();
                byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
                String fotostring = ImagenesBlobBitmap.byte_to_string(fotobytes);
                params.put("foto",fotostring);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(AddPeliculaActivity.this);
        requestQueue.add(request);
    }

    //--------------------------------------------------------------------------
    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------
    public void cambiar_imagen(View view) {
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
                img_add_pelicula.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}