package com.example.myrecyclerviewexample.model;

public class Oficio {
    private int idOficio;
    private String descripcion;
    private byte[] imagen;
    private String imageurl;

    public Oficio(int idOficio, String descripcion) {
        this.idOficio=idOficio;
        this.descripcion=descripcion;
    }

    public int getIdOficio() {
        return idOficio;
    }

    public void setIdOficio(int idOficio) {
        this.idOficio = idOficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Oficio))
            return false;

        Oficio o = (Oficio) obj;
        return o.getIdOficio()==getIdOficio();
    }

}
