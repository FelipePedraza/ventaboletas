package com.ventaboletas.controller;

import com.ventaboletas.model.Cliente;
import com.ventaboletas.model.ColaBoletas;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainViewController {

    @FXML
    private TextField nombreField;
    @FXML
    private ComboBox<String> tipoEntradaBox;
    @FXML
    private Button agregarBtn;
    @FXML
    private Button atenderBtn;
    @FXML
    private TextArea colaTextArea;

    private ColaBoletas cola = new ColaBoletas();

    @FXML
    private void initialize() {

        tipoEntradaBox.getItems().addAll("VIP", "Preferencial", "General");
        tipoEntradaBox.getSelectionModel().select("General");

        // Opcional: Mostrar información de inicio en el TextArea
        colaTextArea.setEditable(false);
    }

    @FXML
    private void agregarCliente() {
        String nombre = nombreField.getText().trim();
        String tipoEntrada = tipoEntradaBox.getValue();
    
        if (nombre.isEmpty()) {
            colaTextArea.appendText("Por favor, ingrese el nombre del cliente.\n");
            return;
        }
        
        int prioridad = getPrioridadPorCategoria(tipoEntrada);
        Cliente cliente = new Cliente(nombre, prioridad, tipoEntrada);
        cola.agregarCliente(cliente);
        colaTextArea.appendText("Agregado: " + nombre + " [Tipo de Entrada: " + tipoEntrada + ", Prioridad: " + prioridad + "]\n");
    
        nombreField.clear();
    }

    @FXML
    private void atenderSiguiente() {
        Cliente cliente = cola.atenderCliente();
        if(cliente != null) {
            colaTextArea.appendText("Atendiendo: " + cliente.getNombre() + " (" + cliente.getTipoEntrada() + ")\n");
        } else {
            colaTextArea.appendText("La cola está vacía.\n");
        }
    }

    private int getPrioridadPorCategoria(String categoria) {
        switch (categoria) {
            case "VIP": 
                return 1; // Alta prioridad
            case "Preferencial": 
                return 2; // Media prioridad
            case "General": 
            default:
                return 3; // Baja prioridad
        }
    }
}

