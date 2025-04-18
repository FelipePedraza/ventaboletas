package com.ventaboletas.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T extends Comparable<T>> implements Serializable {
    private class Node implements Serializable{
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
    public synchronized void offer(T data) {
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
        notifyAll(); // Notificar a los hilos en espera
    }
    
    public synchronized T poll() {
        if (isEmpty()) {
            return null;
        }
        T data = front.data;
        front = front.next;
        size--;
        return data;
    }
    
    /**
     * Bloqueante: espera hasta que haya un elemento.
     
    public synchronized T pollBlocking() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        return poll();
    }
    */

    public synchronized int size() {
        return size;
    }

    public synchronized void clear() {
        front = null;
        size = 0;
    }

    @Override
    public synchronized PriorityQueue<T> clone() {
        PriorityQueue<T> clonedQueue = new PriorityQueue<>();
        Node current = front;
        while (current != null) {
            clonedQueue.offer(current.data);
            current = current.next;
        }
        return clonedQueue;
    }
    public synchronized List<T> toList() {
        List<T> list = new ArrayList<>();
        Node current = front;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

}

