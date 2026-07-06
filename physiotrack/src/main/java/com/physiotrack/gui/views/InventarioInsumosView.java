package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import main.java.modelo.Insumo;
import main.java.negocio.ControladorRecursos;
import main.java.repositories.InsumoRepository;

import java.util.List;

/**
 * Vista para gestión de insumos. Panel lateral con acciones Reabastecer y Consumir.
 */
public class InventarioInsumosView extends VerticalLayout {

    private final InsumoRepository insumoRepository;
    private final ControladorRecursos controladorRecursos = new ControladorRecursos();

    private final Grid<Insumo> grid = new Grid<>(Insumo.class, false);
    private Insumo seleccionado;

    private final IntegerField cantidadField = new IntegerField("Cantidad");
    private final Button btnReabastecer = new Button("Reabastecer Stock");
    private final Button btnConsumir = new Button("Consumir Stock");
    private final Button btnAgregar = new Button("Agregar Insumo");

    public InventarioInsumosView(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        // Inicializar controlador con insumos existentes
        insumoRepository.findAll().forEach(controladorRecursos::registrarInsumo);

        grid.setWidthFull();
        grid.setHeight("450px");
        grid.addColumn(Insumo::getNombre).setHeader("Nombre");
        grid.addColumn(Insumo::getStock).setHeader("Stock");
        grid.addColumn(Insumo::getStockMinimo).setHeader("Stock Mínimo");

        grid.setItems(loadItems());

        grid.asSingleSelect().addValueChangeListener(ev -> {
            seleccionado = ev.getValue();
            boolean has = seleccionado != null;
            cantidadField.setEnabled(has);
            btnReabastecer.setEnabled(has);
            btnConsumir.setEnabled(has);
        });

        cantidadField.setMin(1);
        cantidadField.setWidthFull();

        btnReabastecer.setEnabled(false);
        btnConsumir.setEnabled(false);

        btnReabastecer.addClickListener(e -> reabastecer());
        btnConsumir.addClickListener(e -> consumir());
        btnAgregar.addClickListener(e -> openAgregarDialog());

        VerticalLayout actions = new VerticalLayout(cantidadField, btnReabastecer, btnConsumir);
        actions.setWidth("320px");

        HorizontalLayout main = new HorizontalLayout(grid, actions);
        main.setWidthFull();
        main.expand(grid);

        HorizontalLayout toolbar = new HorizontalLayout(btnAgregar);
        toolbar.setWidthFull();

        add(toolbar, main);
    }

    private List<Insumo> loadItems() {
        return insumoRepository.findAll();
    }

    private void refresh() {
        grid.setItems(loadItems());
    }

    private void reabastecer() {
        if (seleccionado == null) return;
        Integer cantidad = cantidadField.getValue();
        if (cantidad == null || cantidad <= 0) {
            Notification.show("Ingrese una cantidad válida.", 2500, Notification.Position.MIDDLE);
            return;
        }
        try {
            seleccionado.agregarStock(cantidad);
            insumoRepository.save(seleccionado);
            Notification n = Notification.show("Stock reabastecido con éxito");
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            cantidadField.clear();
            refresh();
        } catch (Exception ex) {
            Notification n = new Notification(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
        }
    }

    private void openAgregarDialog() {
        Dialog dlg = new Dialog();

        TextField nombre = new TextField("Nombre");
        IntegerField stock = new IntegerField("Stock");
        IntegerField stockMin = new IntegerField("Stock Mínimo");

        Button btnGuardar = new Button("Guardar", e -> {
            if (nombre.getValue() == null || nombre.getValue().isEmpty() || stock.getValue() == null || stockMin.getValue() == null) {
                Notification.show("Todos los campos son obligatorios.", 2500, Notification.Position.MIDDLE);
                return;
            }
            try {
                Insumo nuevoInsumo = new Insumo(null, nombre.getValue(), null, null, stock.getValue(), stockMin.getValue());
                insumoRepository.save(nuevoInsumo);
                refresh();
                dlg.close();
                Notification.show("Insumo creado exitosamente", 2500, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error al crear insumo: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button btnCancelar = new Button("Cancelar", e -> dlg.close());

        VerticalLayout layout = new VerticalLayout(nombre, stock, stockMin, new HorizontalLayout(btnGuardar, btnCancelar));
        dlg.add(layout);
        dlg.open();
    }

    private void consumir() {
        if (seleccionado == null) return;
        Integer cantidad = cantidadField.getValue();
        if (cantidad == null || cantidad <= 0) {
            Notification.show("Ingrese una cantidad válida.", 2500, Notification.Position.MIDDLE);
            return;
        }
        try {
            controladorRecursos.consumirInsumo(seleccionado, cantidad); //Uso del controlador de recursos
            insumoRepository.save(seleccionado);
            Notification n = Notification.show("Consumo registrado correctamente");
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            cantidadField.clear();
            refresh();
        } catch (Exception ex) {
            Notification n = new Notification(ex.getMessage(), 3500, Notification.Position.MIDDLE);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
        }
    }
}

