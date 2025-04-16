package com.ventaboletas.model;

public class PriorityQueue<T extends Comparable<T>> {
    private class Node {
        private T data;
        private Node next;
    
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node front;
    private int size;
    
    public PriorityQueue() {
        front = null;
        size = 0;
    }
    
    public boolean isEmpty(){
        return size == 0;
    }
    
    /**
     * Inserta el elemento en orden ascendente: 
     * se asume que un elemento "menor" es de mayor prioridad.
     */
    public void offer(T data) {
        Node newNode = new Node(data);
        // Si la cola está vacía o el nuevo dato tiene mayor prioridad que el front
        if (isEmpty() || data.compareTo(front.data) < 0) {
            newNode.next = front;
            front = newNode;
        } else {
            // Buscar la posición de inserción para mantener el orden
            Node current = front;
            while (current.next != null && data.compareTo(current.next.data) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }
    
    public T poll() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        T data = front.data;
        front = front.next;
        size--;
        return data;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        front = null;
        size = 0;
    }
    
    // Puedes mantener otros métodos (como clone o reverse) si son necesarios,
    // aunque en una estructura de priority queue su utilidad dependerá de tu caso.
}

