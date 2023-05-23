package com.example.myrecyclerviewexample.model;

public class Oficio {
    private int idOficio;
    private String descripcion;
    private String imagen;
    private String imageurl;

    public Oficio(int idOficio, String descripcion, String imagen, String imageurl) {
        this.idOficio=idOficio;
        this.descripcion=descripcion;
        this.imagen=imagen;
        this.imageurl=imageurl;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
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
