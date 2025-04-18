package com.ventaboletas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Boleta implements Comparable<Boleta>, Serializable {

    private String nombre;
    private int prioridad; // 1: Alta, 2: Media, 3: Baja
    private String tipoEntrada; // VIP, Preferencial, General
    private LocalDateTime fechaCompra;
    private String asiento;


    public Boleta(String nombre, int prioridad, String tipoEntrada, String asiento) {
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.tipoEntrada = tipoEntrada;
        this.asiento = asiento;
        this.fechaCompra = LocalDateTime.now();; // Fecha de la venta automáticamente
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    
    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    @Override
    public int compareTo(Boleta otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }
}

