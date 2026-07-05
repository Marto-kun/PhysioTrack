package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import main.java.modelo.Fisioterapeuta;
import main.java.modelo.Paciente;
import main.java.services.FisioterapeutaService;
import main.java.services.PacienteService;

import java.util.List;

/**
 * Vista para la gestión de pacientes.
 * Permite visualizar, registrar y asignar pacientes a fisioterapeutas.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
public class PacientesView extends VerticalLayout {

    private FisioterapeutaService fisioterapeutaService;
    private PacienteService pacienteService;

    private ComboBox<Fisioterapeuta> fisioterapeutaCombo;
    private Grid<Paciente> pacientesGrid;
    private Div pacientesContainer;
    private List<Fisioterapeuta> fisioterapeutas;
    private Fisioterapeuta fisioterapeutaSeleccionado;

    public PacientesView(FisioterapeutaService fisioterapeutaService, PacienteService pacienteService) {
        this.fisioterapeutaService = fisioterapeutaService;
        this.pacienteService = pacienteService;
        setWidthFull();
        initializeView();
    }

    private void initializeView() {
        setSpacing(true);
        setPadding(true);
        addClassNames(LumoUtility.Padding.LARGE);

        // Título
        H2 title = new H2("Gestión de Pacientes");
        title.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.TextColor.PRIMARY,
            LumoUtility.Margin.Bottom.LARGE
        );

        // Descripción
        Div description = new Div();
        description.setText("Visualice, registre y asigne pacientes a los fisioterapeutas.");
        description.addClassNames(
            LumoUtility.TextColor.SECONDARY,
            LumoUtility.Margin.Bottom.LARGE,
            LumoUtility.FontSize.SMALL
        );

        // Selector de Fisioterapeuta
        HorizontalLayout selectorArea = createFisioterapeutaSelector();

        // Contenedor de pacientes
        pacientesContainer = new Div();
        pacientesContainer.setWidthFull();
        pacientesContainer.addClassNames(LumoUtility.Padding.LARGE);
        pacientesContainer.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        pacientesContainer.getStyle().set("border-radius", "4px");
        pacientesContainer.getStyle().set("background", "white");

        // Grid de pacientes
        pacientesGrid = createPacientesGrid();
        pacientesGrid.setWidthFull();
        pacientesContainer.add(pacientesGrid);

        // Botón para registrar nuevo paciente
        Button btnNuevoPaciente = new Button("Registrar Nuevo Paciente");
        btnNuevoPaciente.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNuevoPaciente.setIcon(new Icon(VaadinIcon.PLUS));
        btnNuevoPaciente.addClickListener(e -> openNuevoPacienteDialog());

        add(title, description, selectorArea, pacientesContainer, btnNuevoPaciente);
    }

    private HorizontalLayout createFisioterapeutaSelector() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);
        layout.addClassNames(
            LumoUtility.Padding.LARGE,
            LumoUtility.Background.CONTRAST_5
        );

        Span label = new Span("Seleccione un Fisioterapeuta:");
        label.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        fisioterapeutaCombo = new ComboBox<>();
        fisioterapeutaCombo.setLabel("Fisioterapeuta");
        fisioterapeutaCombo.setItemLabelGenerator(Fisioterapeuta::getNombre);
        fisioterapeutaCombo.setWidth("300px");
        fisioterapeutaCombo.addValueChangeListener(e -> {
            fisioterapeutaSeleccionado = e.getValue();
            updatePacientesGrid();
        });

        // Cargar fisioterapeutas
        if (fisioterapeutaService != null) {
            fisioterapeutas = fisioterapeutaService.obtenerTodosFisioterapeutas();
            fisioterapeutaCombo.setItems(fisioterapeutas);
        }

        layout.add(label, fisioterapeutaCombo);
        return layout;
    }

    private Grid<Paciente> createPacientesGrid() {
        Grid<Paciente> grid = new Grid<>(Paciente.class, false);
        grid.setWidthFull();
        grid.setHeight("450px");

        // Columna: Cédula
        grid.addColumn(Paciente::getCedula)
            .setHeader("Cédula")
            .setAutoWidth(true);

        // Columna: Nombre
        grid.addColumn(Paciente::getNombre)
            .setHeader("Nombre")
            .setAutoWidth(true);

        // Columna: Edad
        grid.addColumn(Paciente::getEdad)
            .setHeader("Edad")
            .setAutoWidth(true);

        // Columna: Tipo de Lesión
        grid.addColumn(Paciente::getTipoLesion)
            .setHeader("Tipo de Lesión")
            .setAutoWidth(true);

        // Columna: Nivel de Lesión
        grid.addColumn(Paciente::getNivelLesion)
            .setHeader("Nivel")
            .setAutoWidth(true);

        // Columna: Prioridad
        grid.addColumn(new ComponentRenderer<>(paciente -> {
            Span span = new Span(paciente.getPrioridad());
            span.getElement().getThemeList().add("badge");

            if ("Urgente".equals(paciente.getPrioridad())) {
                span.getElement().getThemeList().add("error");
            } else if ("Seguimiento".equals(paciente.getPrioridad())) {
                span.getElement().getThemeList().add("warning");
            } else {
                span.getElement().getThemeList().add("success");
            }

            return span;
        }))
        .setHeader("Prioridad")
        .setAutoWidth(true);

        // Columna: Acciones
        grid.addColumn(new ComponentRenderer<>(paciente -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button btnEditar = new Button(new Icon(VaadinIcon.EDIT));
            btnEditar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            btnEditar.addClickListener(e -> openEditPacienteDialog(paciente));

            Button btnEliminar = new Button(new Icon(VaadinIcon.TRASH));
            btnEliminar.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            btnEliminar.addClickListener(e -> deletePaciente(paciente));

            actions.add(btnEditar, btnEliminar);
            return actions;
        }))
        .setHeader("Acciones")
        .setAutoWidth(true);

        return grid;
    }

    private void updatePacientesGrid() {
        pacientesGrid.setItems(List.of());

        if (fisioterapeutaSeleccionado != null && pacienteService != null) {
            List<Paciente> pacientes = pacienteService.obtenerPacientesPorFisioterapeuta(fisioterapeutaSeleccionado);
            pacientesGrid.setItems(pacientes);
            pacientesGrid.getDataProvider().refreshAll(); //Recargar para que aparezcan los nuevos pacientes agregados
        }
    }

    private void openNuevoPacienteDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Registrar Nuevo Paciente");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        FormLayout form = createPacienteForm(null);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGuardar.addClickListener(e -> {
            savePaciente(dialog, form, null);
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(e -> dialog.close());

        HorizontalLayout actions = new HorizontalLayout(btnGuardar, btnCancelar);
        dialogLayout.add(form, actions);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openEditPacienteDialog(Paciente paciente) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Editar Paciente");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        FormLayout form = createPacienteForm(paciente);

        Button btnGuardar = new Button("Actualizar");
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGuardar.addClickListener(e -> savePaciente(dialog, form, paciente));

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(e -> dialog.close());

        HorizontalLayout actions = new HorizontalLayout(btnGuardar, btnCancelar);
        dialogLayout.add(form, actions);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private FormLayout createPacienteForm(Paciente paciente) {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );

        TextField cedulaField = new TextField("Cédula");
        cedulaField.setRequired(true);
        if (paciente != null) {
            cedulaField.setValue(paciente.getCedula());
            cedulaField.setReadOnly(true);
        }

        TextField nombreField = new TextField("Nombre Completo");
        nombreField.setRequired(true);
        if (paciente != null) {
            nombreField.setValue(paciente.getNombre());
        }

        NumberField edadField = new NumberField("Edad");
        edadField.setRequired(true);
        edadField.setMin(1);
        edadField.setMax(150);
        if (paciente != null) {
            edadField.setValue((double) paciente.getEdad());
        }

        TextField tipoLesionField = new TextField("Tipo de Lesión");
        tipoLesionField.setRequired(true);
        if (paciente != null) {
            tipoLesionField.setValue(paciente.getTipoLesion());
        }

        NumberField nivelLesionField = new NumberField("Nivel de Lesión (1-10)");
        nivelLesionField.setRequired(true);
        nivelLesionField.setMin(1);
        nivelLesionField.setMax(10);
        if (paciente != null) {
            nivelLesionField.setValue((double) paciente.getNivelLesion());
        }

        ComboBox<Fisioterapeuta> fisioAsignadoCombo = new ComboBox<>("Asignar Fisioterapeuta");
        fisioAsignadoCombo.setItemLabelGenerator(Fisioterapeuta::getNombre);
        fisioAsignadoCombo.setItems(fisioterapeutas);
        if (paciente != null && paciente.getFisioterapeutaAsignado() != null) {
            fisioAsignadoCombo.setValue(paciente.getFisioterapeutaAsignado());
        }

        form.add(cedulaField, nombreField, edadField, tipoLesionField, nivelLesionField, fisioAsignadoCombo);

        // No es necesario almacenar referencias en el elemento DOM.
        // Los campos se recuperan directamente desde el layout cuando se guardan.

        return form;
    }

    private void savePaciente(Dialog dialog, FormLayout form, Paciente pacienteExistente) {
        try {
            // Obtener los campos del formulario por índice
            java.util.List<com.vaadin.flow.component.Component> components = form.getChildren().collect(java.util.stream.Collectors.toList());

            TextField cedulaField = (TextField) components.get(0);
            TextField nombreField = (TextField) components.get(1);
            NumberField edadField = (NumberField) components.get(2);
            TextField tipoLesionField = (TextField) components.get(3);
            NumberField nivelLesionField = (NumberField) components.get(4);
            ComboBox<Fisioterapeuta> fisioAsignadoCombo = (ComboBox<Fisioterapeuta>) components.get(5);

            String cedula = cedulaField.getValue();
            String nombre = nombreField.getValue();
            int edad = edadField.getValue().intValue();
            String tipoLesion = tipoLesionField.getValue();
            int nivelLesion = nivelLesionField.getValue().intValue();
            Fisioterapeuta fisioterapeuta = fisioAsignadoCombo.getValue();

            if (cedula.isEmpty() || nombre.isEmpty() || tipoLesion.isEmpty()) {
                showError("Por favor complete todos los campos requeridos");
                return;
            }

            Paciente paciente;
            if (pacienteExistente == null) {
                paciente = new Paciente(cedula, nombre, edad, tipoLesion, nivelLesion);
            } else {
                paciente = pacienteExistente;
                paciente.setNombre(nombre);
                paciente.setEdad(edad);
                paciente.setTipoLesion(tipoLesion);
                paciente.setNivelLesion(nivelLesion);
            }

            if (pacienteService != null) {
                if (pacienteExistente == null) {
                    if(fisioterapeuta == null){
                        showError("Por favor seleccione un fisioterapeuta antes de continuar.");
                        return;
                    }
                    paciente = pacienteService.registrarPaciente(paciente, fisioterapeuta);
                } else {
                    paciente = pacienteService.actualizarPaciente(paciente);
                }
            }

            showSuccess(pacienteExistente == null ? "¡Paciente registrado exitosamente!" : "¡Paciente actualizado exitosamente!");
            dialog.close();
            updatePacientesGrid();
        } catch (Exception e) {
            showError("Error al guardar el paciente: " + e.getMessage());
        }
    }

    private void deletePaciente(Paciente paciente) {
        try {
            if (pacienteService != null) {
                pacienteService.eliminarPaciente(paciente.getId());
                showSuccess("¡Paciente eliminado exitosamente!");
                updatePacientesGrid();
            }
        } catch (Exception e) {
            showError("Error al eliminar el paciente: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        Notification notification = new Notification(message);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(3000);
        notification.open();
    }

    private void showError(String message) {
        Notification notification = new Notification(message);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(3000);
        notification.open();
    }
}



