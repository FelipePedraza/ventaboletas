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
    private ComboBox<String> prioridadBox;
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
        // Inicializar los ComboBox con los valores deseados
        prioridadBox.getItems().addAll("Alta", "Media", "Baja");
        prioridadBox.getSelectionModel().selectFirst();

        tipoEntradaBox.getItems().addAll("VIP", "Preferencial", "General");
        tipoEntradaBox.getSelectionModel().select("General");

        // Opcional: Mostrar información de inicio en el TextArea
        colaTextArea.setEditable(false);
    }

    @FXML
    private void agregarCliente() {
        String nombre = nombreField.getText().trim();
        String prioridadStr = prioridadBox.getValue();
        String tipoEntrada = tipoEntradaBox.getValue();

        if(nombre.isEmpty()){
            colaTextArea.appendText("Por favor ingrese el nombre del cliente.\n");
            return;
        }
        int prioridad = convertirPrioridad(prioridadStr);
        Cliente cliente = new Cliente(nombre, prioridad, tipoEntrada);
        cola.agregarCliente(cliente);
        colaTextArea.appendText("Agregado: " + nombre + " [Prioridad " + prioridadStr + ", " + tipoEntrada + "]\n");

        // Limpiar campos para siguiente ingreso
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

    private int convertirPrioridad(String prioridadStr) {
        switch (prioridadStr) {
            case "Alta": return 1;
            case "Media": return 2;
            case "Baja": return 3;
            default: return 3;
        }
    }
}

