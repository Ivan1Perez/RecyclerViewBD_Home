package com.example.myrecyclerviewexample.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int imagen;
    private String nombre;
    private String apellidos;
    private int oficio;

    public Usuario(int imagen, String nombre, String apellidos, int oficio) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.oficio = oficio;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getOficio() {
        return oficio;
    }
}
