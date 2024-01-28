package com.example.mysqlpeliculas1.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysqlpeliculas1.MostrarDetallesPelicula;
import com.example.mysqlpeliculas1.R;
import com.example.mysqlpeliculas1.clases.Pelicula;
import com.example.mysqlpeliculas1.utilidades.ImagenesBlobBitmap;

public class PeliculaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String EXTRA_DETALLES_PELICULA = "com.example.nuevofirebasepeliculas.peliculaviewholder.detalles_pelicula" ;
    public static final String EXTRA_DETALLES_IMAGEN_PELICULA = "com.example.nuevofirebasepeliculas.peliculaviewholder.detalles_pelicula_imagen";
    
    // atributos-----------------------
    private TextView txt_item_titulo, txt_item_genero;
    private ImageView img_item_pelicula;
    private ListaPeliculasAdapter lpa;
    private Context contexto;
    public ImageView getImg_item_pelicula() {
        return img_item_pelicula;
    }
    
    //getters----------------------
    public Context getContexto() {
        return contexto;
    }

    public TextView getTxt_item_titulo() {
        return txt_item_titulo;
    }

    public TextView getTxt_item_genero() {
        return txt_item_genero;
    }

    public ListaPeliculasAdapter getLpa() {
        return lpa;
    }
    
    
    //setters----------------------
    public void setImg_item_pelicula(ImageView img_item_pelicula) {
        this.img_item_pelicula = img_item_pelicula;
    }
    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public void setTxt_item_titulo(TextView txt_item_titulo) {
        this.txt_item_titulo = txt_item_titulo;
    }

    public void setTxt_item_genero(TextView txt_item_genero) {
        this.txt_item_genero = txt_item_genero;
    }

    public void setLpa(ListaPeliculasAdapter lpa) {
        this.lpa = lpa;
    }
    
    
    //metodos-----------------------

    public PeliculaViewHolder(@NonNull View itemView, ListaPeliculasAdapter listaPeliculasAdapter) {
        super(itemView);
        txt_item_titulo = (TextView) itemView.findViewById(R.id.txt_item_titulo);
        txt_item_genero = (TextView) itemView.findViewById(R.id.txt_item_genero);
        img_item_pelicula = (ImageView) itemView.findViewById(R.id.img_item_pelicula);
        lpa = listaPeliculasAdapter;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int posicion = getLayoutPosition();
        Pelicula pelicula = lpa.getPeliculas().get(posicion);
        Intent intent = new Intent(lpa.getContexto(), MostrarDetallesPelicula.class);
        intent.putExtra(EXTRA_DETALLES_PELICULA,pelicula);
        img_item_pelicula.buildDrawingCache();
        Bitmap foto_bm = img_item_pelicula.getDrawingCache();
        byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
        intent.putExtra(EXTRA_DETALLES_IMAGEN_PELICULA,fotobytes );

        lpa.getContexto().startActivity(intent);
    }
}
