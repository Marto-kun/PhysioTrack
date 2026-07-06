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
import main.java.modelo.EquipoBiomedico;
import main.java.negocio.ControladorRecursos;
import main.java.repositories.EquipoBiomedicoRepository;

import java.util.List;

/**
 * Vista para gestión de equipos biomédicos. Acciones habilitadas al seleccionar un equipo.
 */
public class InventarioEquiposView extends VerticalLayout {

    private final EquipoBiomedicoRepository equipoRepository;
    private final ControladorRecursos controladorRecursos = new ControladorRecursos();

    private final Grid<EquipoBiomedico> grid = new Grid<>(EquipoBiomedico.class, false);
    private EquipoBiomedico seleccionado;

    private final Button btnEnviarMantenimiento = new Button("Enviar a Mantenimiento");
    private final Button btnLiberarEquipo = new Button("Liberar Equipo");
    private final IntegerField horasField = new IntegerField("Horas de uso");
    private final Button btnAgregar = new Button("Agregar Equipo");

    public InventarioEquiposView(EquipoBiomedicoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        // Inicializar controlador con equipos existentes
        equipoRepository.findAll().forEach(controladorRecursos::registrarEquipo);

        grid.setWidthFull();
        grid.setHeight("450px");
        grid.addColumn(EquipoBiomedico::getNombre).setHeader("Nombre");
        grid.addColumn(EquipoBiomedico::getEstado).setHeader("Estado");
        grid.addColumn(EquipoBiomedico::getHorasDeUso).setHeader("Horas de Uso");

        grid.setItems(loadItems());

        grid.asSingleSelect().addValueChangeListener(ev -> {
            seleccionado = ev.getValue();
            boolean has = seleccionado != null;
            btnEnviarMantenimiento.setEnabled(has);
            btnLiberarEquipo.setEnabled(has);
        });

        btnEnviarMantenimiento.setEnabled(false);
        btnLiberarEquipo.setEnabled(false);

        btnEnviarMantenimiento.addClickListener(e -> enviarMantenimiento());
        btnLiberarEquipo.addClickListener(e -> liberarEquipo());
        btnAgregar.addClickListener(e -> openAgregarDialog());

        horasField.setMin(0);
        horasField.setWidthFull();

        VerticalLayout actions = new VerticalLayout(horasField, btnLiberarEquipo, btnEnviarMantenimiento);
        actions.setWidth("320px");

        HorizontalLayout main = new HorizontalLayout(grid, actions);
        main.setWidthFull();
        main.expand(grid);

        HorizontalLayout toolbar = new HorizontalLayout(btnAgregar);
        toolbar.setWidthFull();

        add(toolbar, main);
    }

    private List<EquipoBiomedico> loadItems() {
        return equipoRepository.findAll();
    }

    private void refresh() {
        grid.setItems(loadItems());
    }

    private void enviarMantenimiento() {
        if (seleccionado == null) return;
        try {
            seleccionado.enviarMantenimiento();
            equipoRepository.save(seleccionado);
            Notification n = Notification.show("Equipo enviado a mantenimiento");
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            refresh();
        } catch (IllegalStateException ex) {
            Notification n = new Notification(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
        }
    }

    private void openAgregarDialog() {
        Dialog dlg = new Dialog();

        TextField nombre = new TextField("Nombre");
        TextField fecha = new TextField("Fecha Elaboración");
        TextField estado = new TextField("Estado");
        IntegerField horas = new IntegerField("Horas de Uso");

        Button btnGuardar = new Button("Guardar", e -> {
            if (nombre.getValue() == null || nombre.getValue().isEmpty() || fecha.getValue() == null || estado.getValue() == null || horas.getValue() == null) {
                Notification.show("Todos los campos son obligatorios.", 2500, Notification.Position.MIDDLE);
                return;
            }
            try {
                EquipoBiomedico nuevoEquipo = new EquipoBiomedico(null, nombre.getValue(), fecha.getValue(), estado.getValue());
                nuevoEquipo.setHorasDeUso(horas.getValue());
                equipoRepository.save(nuevoEquipo);
                refresh();
                dlg.close();
                Notification.show("Equipo creado exitosamente", 2500, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error al crear equipo: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button btnCancelar = new Button("Cancelar", e -> dlg.close());

        VerticalLayout layout = new VerticalLayout(nombre, fecha, estado, horas, new HorizontalLayout(btnGuardar, btnCancelar));
        dlg.add(layout);
        dlg.open();
    }

    private void liberarEquipo() {
        if (seleccionado == null) return;
        Integer horas = horasField.getValue();
        if (horas == null) {
            Notification.show("Ingrese horas de uso antes de liberar.", 2500, Notification.Position.MIDDLE);
            return;
        }
        try {
            controladorRecursos.liberarEquipo(seleccionado, horas);
            equipoRepository.save(seleccionado);
            Notification n = Notification.show("Equipo liberado y horas actualizadas");
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            horasField.clear();
            refresh();
        } catch (Exception ex) {
            Notification n = new Notification(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            n.open();
        }
    }
}

