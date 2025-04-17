package com.ventaboletas.model;

import java.time.LocalDateTime;

public class Cliente implements Comparable<Cliente> {
    private String nombre;
    private int prioridad; // 1: Alta, 2: Media, 3: Baja
    private String tipoEntrada; // VIP, Preferencial, General
    private LocalDateTime fechaCompra;


    public Cliente(String nombre, int prioridad, String tipoEntrada) {
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.tipoEntrada = tipoEntrada;
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

    @Override
    public int compareTo(Cliente otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }
}

