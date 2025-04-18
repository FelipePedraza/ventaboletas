package com.ventaboletas.model;

import java.io.Serializable;
import com.ventaboletas.util.PriorityQueue;

public class ColaBoletas implements Serializable{
    private PriorityQueue<Boleta> cola;

    public ColaBoletas() {
        cola = new PriorityQueue<>();
    }

    public void agregarCliente(Boleta c) {
        cola.offer(c);
    }

    public Boleta atenderCliente() throws InterruptedException {
        return cola.poll();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int tamañoCola() {
        return cola.size();
    }

    public PriorityQueue<Boleta> getCola() {
        return cola.clone(); // Retorna una copia de la cola para evitar modificaciones externas
    }
}

