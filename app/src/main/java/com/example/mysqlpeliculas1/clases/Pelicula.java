package com.example.mysqlpeliculas1.clases;

import java.io.Serializable;
import java.util.Objects;

public class Pelicula implements Serializable {
    //atributos----------------------
    private String titulo, genero, idPelicula;

    //constructores------------------
    public Pelicula(String idPelicula, String titulo, String genero) {
        this.titulo = titulo;
        this.genero = genero;
        this.idPelicula = idPelicula;
    }
    public Pelicula() {
        this.titulo = "Sin titulo";
        this.genero = "Sin genero";
        this.idPelicula = "Sin id";
    }
    //getters---------------------
    public String getTitulo() {
        return titulo;
    }
    public String getGenero() {
        return genero;
    }
    public String getIdPelicula() { return idPelicula; }

    //setters----------------------
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public void setIdPelicula(String idPelicula) { this.idPelicula = idPelicula; }

    //metodos-----------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pelicula)) return false;
        Pelicula pelicula = (Pelicula) o;
        return idPelicula.equals(pelicula.idPelicula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", idPelicula='" + idPelicula + '\'' +
                '}';
    }
}
