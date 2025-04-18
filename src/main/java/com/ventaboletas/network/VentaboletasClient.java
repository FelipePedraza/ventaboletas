package com.ventaboletas.network;

import com.ventaboletas.controller.MainViewController;
import com.ventaboletas.model.Boleta;
import com.ventaboletas.model.ColaBoletas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VentaboletasClient {
    private final String host;
    private final int port;

    public VentaboletasClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Boleta enviarOrden(String nombre, String tipo) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeUTF(nombre);
            out.writeUTF(tipo);
            out.flush();

            boolean ok = in.readBoolean();
            String seat = in.readUTF();

            if (ok && !seat.isEmpty()) {
                return new Boleta(nombre, 0, tipo, seat); // La prioridad se define luego si es necesario
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boleta atenderCliente() {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
    
            out.writeUTF("ATENDER_CLIENTE");
            out.flush();
    
            Object response = in.readObject();
            if (response instanceof Boleta) {
                return (Boleta) response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ColaBoletas obtenerCola() {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
    
            // Solicita la cola al servidor
            out.writeUTF("OBTENER_COLA");
            out.flush();
    
            // Lee la cola enviada por el servidor
            ColaBoletas cola = (ColaBoletas) in.readObject();
            return cola;
    
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void escucharNotificaciones(MainViewController controller) {
        new Thread(() -> {
            ColaBoletas cola = obtenerCola();
            while (cola != null) {
                try {
                    Thread.sleep(5000); // Espera 5 segundos antes de volver a consultar
                    ColaBoletas nuevaCola = obtenerCola();
                    if (nuevaCola != null && !nuevaCola.equals(cola)) {
                        cola = nuevaCola;
                        controller.actualizarTabla(cola); // Actualiza la cola en la interfaz
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                }
            }
        }).start();
    }
}
