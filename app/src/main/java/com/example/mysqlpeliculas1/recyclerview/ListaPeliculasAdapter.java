package com.example.mysqlpeliculas1.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysqlpeliculas1.R;
import com.example.mysqlpeliculas1.clases.ConfiguracionDB;
import com.example.mysqlpeliculas1.clases.Pelicula;
import com.example.mysqlpeliculas1.utilidades.ImagenesBlobBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaPeliculasAdapter extends RecyclerView.Adapter<PeliculaViewHolder> {

    // atributos----------------
    private Context contexto = null;
    private ArrayList<Pelicula> Peliculas = null;
    private LayoutInflater inflate = null;

    //constructores----------------
    public ListaPeliculasAdapter(Context contexto, ArrayList<Pelicula> Peliculas ) {
        this.contexto = contexto;
        this.Peliculas = Peliculas;
        inflate =  LayoutInflater.from(this.contexto);
    }

    //setters--------------
    public void setPeliculas(ArrayList<Pelicula> Peliculas) {
        this.Peliculas = Peliculas;
        notifyDataSetChanged();
    }

    //getters----------------

    public Context getContexto() {
        return contexto;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return Peliculas;
    }


    //metodos---------------

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflate.inflate(R.layout.item_rv_pelicula,parent,false);

        PeliculaViewHolder evh = new PeliculaViewHolder(mItemView,this);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula p = this.getPeliculas().get(position);
        //----------------------------------------------------------------------
        holder.getTxt_item_titulo().setText(p.getTitulo());
        holder.getTxt_item_genero().setText(String.valueOf(p.getGenero()));
        //----------- codigo para mostrar la foto de la pelicula -----------------------
        String idPelicula = p.getIdPelicula();
        descargarImagen(idPelicula, holder.getImg_item_pelicula(), contexto);
    }

    private void descargarImagen(String idPelicula, ImageView img_foto, Context contexto) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ+ "/mostrar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String exito=jsonObject.getString("exito");
                            JSONArray jsonArray =jsonObject.getJSONArray("peliculas_fotos");
                            if (exito.equals("1")){
                                int cuantos = jsonArray.length();
                                if (cuantos > 0) {
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    String idPelicula = object.getString("idPelicula");
                                    String foto = object.getString("foto");
                                    byte[] fotobyte = ImagenesBlobBitmap.string_to_byte(foto);
                                    Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobyte, ConfiguracionDB.ancho_imagen,ConfiguracionDB.alto_imagen);
                                    img_foto.setImageBitmap(fotobitmap);
                                }
                            }
                        }
                        catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("mysql1","error al pedir la foto");
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
        RequestQueue requestQueue = Volley.newRequestQueue(contexto);
        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return this.Peliculas.size();
    }

    public void addPelicula(Pelicula peliculaAgregada) {
        Peliculas.add(peliculaAgregada);
        notifyDataSetChanged();
    }

}
