package com.ventaboletas.model;

public class Cliente implements Comparable<Cliente> {
    private String nombre;
    private int prioridad; // 1: Alta, 2: Media, 3: Baja
    private String tipoEntrada; // Ejemplo: VIP, Preferencial, General

    public Cliente(String nombre, int prioridad, String tipoEntrada) {
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.tipoEntrada = tipoEntrada;
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

    @Override
    public int compareTo(Cliente otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
    }
}

