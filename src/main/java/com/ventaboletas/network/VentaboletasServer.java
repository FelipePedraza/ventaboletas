package com.ventaboletas.network;

import com.ventaboletas.model.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class VentaboletasServer {
    private final int port;
    private final ColaBoletas cola;
    private final SillasInventario inventory;
    private ServerSocket server;
    private ExecutorService pool;

    public VentaboletasServer(int port, ColaBoletas cola, SillasInventario inv) {
        this.port = port; this.cola = cola; this.inventory = inv;
    }

    public void start() throws IOException {
        server = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        while (true) {
            Socket client = server.accept();
            pool.submit(() -> handleClient(client));
        }
    }

    private void handleClient(Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
    
                String received = (String) in.readUTF();
                if ("ATENDER_CLIENTE".equals(received)) {
                    Boleta siguiente = cola.atenderCliente();
                    out.writeObject(siguiente);  // Puede ser null si no hay nadie en la cola
                    out.flush();
                } else if ("OBTENER_COLA".equals(received)) {
                    // Envía la cola completa al cliente
                    out.writeObject(cola);
                    out.flush();
                } else {
                    String nombre = received;
                    String tipo = in.readUTF();
                    String seat = inventory.assignSeat(tipo);
                    boolean sold = seat != null;
                    out.writeBoolean(sold);
                    out.writeUTF(sold ? seat : "");
                    out.flush();
                
                    if (sold) {
                        int prio = getPrioridadPorCategoria(tipo);
                        Boleta c = new Boleta(nombre, prio, tipo, seat);
                        cola.agregarCliente(c);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
    
    private int getPrioridadPorCategoria(String cat) {
        switch (cat) {
            case "VIP": return 1;
            case "Preferencial": return 2;
            default: return 3;
        }
    }

    

    public static void main(String[] args) throws Exception {
        ColaBoletas cola = new ColaBoletas();
        SillasInventario inv = new SillasInventario("inventory.html");
        System.out.println("Server started on port 5555");
        new VentaboletasServer(5555, cola, inv).start();
    }
}