package com.ventaboletas.model;
import java.util.PriorityQueue;

public class ColaBoletas {
    private PriorityQueue<Cliente> cola;

    public ColaBoletas() {
        cola = new PriorityQueue<>();
    }

    public void agregarCliente(Cliente c) {
        cola.offer(c);
    }

    public Cliente atenderCliente() {
        return cola.poll(); // Retorna y remueve el cliente de mayor prioridad (menor valor)
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int tamañoCola() {
        return cola.size();
    }
}

