package com.svalero.dadosytragos.domain;

import java.time.LocalDate;

public class Comida {
    private int idComida;
    private String nombre;
    private String tipo;
    private double precio;
    private int stock;
    private String descripcion;
    private boolean esVegetariana;
    private LocalDate fechaRegistro;

    public int getIdComida() {
        return idComida;
    }

    public void setIdComida(int idComida) {
        this.idComida = idComida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEsVegetariana() {
        return esVegetariana;
    }

    public void setEsVegetariana(boolean esVegetariana) {
        this.esVegetariana = esVegetariana;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}