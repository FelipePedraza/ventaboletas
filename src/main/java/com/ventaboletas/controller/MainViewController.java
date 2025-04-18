package com.ventaboletas.controller;

import com.ventaboletas.model.Boleta;
import com.ventaboletas.model.ColaBoletas;
import com.ventaboletas.network.VentaboletasClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainViewController {
    @FXML private TextField nombreField;
    @FXML private ComboBox<String> tipoEntradaBox;
    @FXML private Button agregarBtn;
    @FXML private Button atenderBtn;
    @FXML private TextArea colaTextArea;
    @FXML private TableView<Boleta> colaTable;
    @FXML private TableColumn<Boleta, String> nombreCol;
    @FXML private TableColumn<Boleta, String> tipoCol;
    @FXML private TableColumn<Boleta, String> asientoCol;
    @FXML private TableColumn<Boleta, String> fechaCol;

    private final ObservableList<Boleta> tablaCola = FXCollections.observableArrayList();
    private final ColaBoletas cola = new ColaBoletas();
    private final VentaboletasClient client = new VentaboletasClient("localhost", 5555);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    private void initialize() {
        tipoEntradaBox.getItems().addAll("VIP", "Preferencial", "General");
        tipoEntradaBox.getSelectionModel().select("General");
        colaTextArea.setEditable(false);
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipoEntrada"));
        asientoCol.setCellValueFactory(new PropertyValueFactory<>("asiento"));
        fechaCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFechaCompra().format(dtf))
        );

        colaTable.setItems(tablaCola);
        client.escucharNotificaciones(this); // Escuchar notificaciones del servidor
    }

    @FXML
    private void agregarBoleta() {
        String nombre = nombreField.getText().trim();
        String tipo = tipoEntradaBox.getValue();

        if (nombre.isEmpty()) {
            colaTextArea.appendText("Por favor, ingrese el nombre del cliente.\n");
            return;
        }

        new Thread(() -> {
            Boleta cliente = client.enviarOrden(nombre, tipo);
            if (cliente != null) {
                cliente.setPrioridad(getPrioridadPorCategoria(tipo));
                cliente.setFechaCompra(LocalDateTime.now());

                cola.agregarCliente(cliente);

                Platform.runLater(() -> {
                    colaTextArea.appendText(String.format(
                        "Agregado: %s [%s, Prio %d, Asiento %s, %s]\n",
                        cliente.getNombre(),
                        cliente.getTipoEntrada(),
                        cliente.getPrioridad(),
                        cliente.getAsiento(),
                        cliente.getFechaCompra().format(dtf)
                    ));
                    
                    nombreField.clear();
                });
            } else {
                Platform.runLater(() ->
                    colaTextArea.appendText("Boletas agotadas en " + tipo + "\n")
                );
            }
        }).start();
    }

    @FXML
    private void atender() {
        new Thread(() -> {
            Boleta cliente = client.atenderCliente();  // Ahora se consulta al servidor
            if (cliente != null) {
                Platform.runLater(() -> {
                    colaTextArea.appendText(String.format(
                        "Atendiendo: %s (%s) - Asiento: %s - %s\n",
                        cliente.getNombre(),
                        cliente.getTipoEntrada(),
                        cliente.getAsiento(),
                        cliente.getFechaCompra().format(dtf)
                    ));
                    
                });
            } else {
                Platform.runLater(() ->
                    colaTextArea.appendText("No hay clientes en la cola.\n")
                );
            }
        }).start();
    }

    private int getPrioridadPorCategoria(String categoria) {
        switch (categoria) {
            case "VIP": return 1;
            case "Preferencial": return 2;
            default: return 3;
        }
    }

    public void actualizarTabla(ColaBoletas colaClientes) {
        // Actualiza la tabla de clientes en cola desde el servidor
        if (colaClientes != null) {
            Platform.runLater(() -> {
                colaTable.getItems().setAll(colaClientes.getCola().clone().toList());
            });
        } else {
            Platform.runLater(() -> {
                colaTextArea.appendText("Error al obtener la cola del servidor.\n");
            });
        }
    }
}
