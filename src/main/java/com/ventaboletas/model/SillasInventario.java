package com.ventaboletas.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import com.ventaboletas.util.Queue; 

public class SillasInventario implements Serializable {
    private Queue<String> vipSeats, prefSeats, genSeats;
    private final String path;

    public SillasInventario(String path) throws IOException, ClassNotFoundException {
        this.path = path;
        File file = new File(path);
        if (!file.exists()) {
            initSeats(); save();
        } else load();
    }

    private void initSeats() {
        vipSeats = new Queue<>();
        prefSeats = new Queue<>();
        genSeats = new Queue<>();
        for (int i = 1; i <= 1; i++) vipSeats.offer("A" + i);
        for (int i = 1; i <= 2; i++) prefSeats.offer("B" + i);
        for (int i = 1; i <= 5; i++) genSeats.offer("C" + i);
    }

    private synchronized void load() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            SillasInventario saved = (SillasInventario) in.readObject();
            this.vipSeats = saved.vipSeats;
            this.prefSeats = saved.prefSeats;
            this.genSeats = saved.genSeats;
        }
    }

    private synchronized void save() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(this);
        }
    }

    public synchronized String assignSeat(String categoria) throws IOException {
        String seat = null;
        try {
            if ("VIP".equals(categoria) && !vipSeats.isEmpty()) seat = vipSeats.poll();
            else if ("Preferencial".equals(categoria) && !prefSeats.isEmpty()) seat = prefSeats.poll();
            else if ("General".equals(categoria) && !genSeats.isEmpty()) seat = genSeats.poll();
        } catch (RuntimeException e) {
            // Ya está manejado con isEmpty, pero por si acaso
            seat = null;
        }
        if (seat != null) save();
        return seat;
    }

    public synchronized int getAvailable(String categoria) {
        if ("VIP".equals(categoria)) return vipSeats.size();
        if ("Preferencial".equals(categoria)) return prefSeats.size();
        return genSeats.size();
    }
}
