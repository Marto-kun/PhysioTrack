package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import main.java.modelo.EquipoBiomedico;
import main.java.modelo.Fisioterapeuta;
import main.java.modelo.Insumo;
import main.java.modelo.Paciente;
import main.java.modelo.SesionTratamiento;
import main.java.services.EquipoBiomedicoService;
import main.java.services.FisioterapeutaService;
import main.java.services.InsumoService;
import main.java.services.PacienteService;
import main.java.services.SesionTratamientoService;

import java.util.Collections;
import java.util.List;

/**
 * Vista para historial de sesiones filtrado por paciente. Incluye además
 * un botón para abrir el diálogo de registro existente.
 */
public class SesionesView extends VerticalLayout {

    private final PacienteService pacienteService;
    private final FisioterapeutaService fisioterapeutaService;
    private final EquipoBiomedicoService equipoService;
    private final InsumoService insumoService;
    private final SesionTratamientoService sesionService;
    private final main.java.repositories.SesionTratamientoRepository sesionRepository;

    private final ComboBox<Paciente> pacienteCombo;
    private final Grid<SesionTratamiento> grid;

    public SesionesView(PacienteService pacienteService,
                        FisioterapeutaService fisioterapeutaService,
                        EquipoBiomedicoService equipoService,
                        InsumoService insumoService,
                        SesionTratamientoService sesionService,
                        main.java.repositories.SesionTratamientoRepository sesionRepository) {
        this.pacienteService = pacienteService;
        this.fisioterapeutaService = fisioterapeutaService;
        this.equipoService = equipoService;
        this.insumoService = insumoService;
        this.sesionService = sesionService;
        this.sesionRepository = sesionRepository;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        pacienteCombo = new ComboBox<>("Paciente");
        pacienteCombo.setWidthFull();
        pacienteCombo.setItemLabelGenerator(Paciente::getNombre);
        pacienteCombo.setItems(loadPacientes());

        grid = new Grid<>(SesionTratamiento.class, false);
        grid.setWidthFull();
        grid.setHeight("450px");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addColumn(SesionTratamiento::getDuracionMinutos).setHeader("Duración (min)");
        grid.addColumn(SesionTratamiento::getObservaciones)
                .setHeader("Observaciones")
                .setWidth("400px")
                .setFlexGrow(1);

        grid.addColumn(s -> s.getEquipo() == null ? "-" : s.getEquipo().getNombre())
                .setHeader("Equipo Biomédico")
                .setWidth("180px")
                .setFlexGrow(0);

        grid.addColumn(s -> s.getInsumo() == null ? "-" : s.getInsumo().getNombre())
                .setHeader("Insumo")
                .setWidth("150px")
                .setFlexGrow(0);

        grid.setAllRowsVisible(false);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                abrirEditorObservacion(event.getFirstSelectedItem().get());
            }
        });

        pacienteCombo.addValueChangeListener(ev -> {
            Paciente sel = ev.getValue();
            if (sel == null) {
                grid.setItems(Collections.emptyList());
            } else {
                try {
                    List<SesionTratamiento> items = sesionRepository.findByPaciente_Id(sel.getId());
                    grid.setItems(items);
                } catch (Exception ex) {
                    Notification n = new Notification("Error cargando sesiones: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                    n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    n.open();
                    grid.setItems(Collections.emptyList());
                }
            }
        });

        Button btnNueva = new Button("Registrar Sesión", e -> openDialog());

        VerticalLayout header = new VerticalLayout(pacienteCombo, btnNueva);
        header.setPadding(false);
        header.setSpacing(true);

        btnNueva.setWidth("200px");

        add(header, grid);

    }

    private List<Paciente> loadPacientes() {
        try {
            return pacienteService.obtenerTodosPacientes();
        } catch (Exception e) {
            Notification.show("Error cargando pacientes: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            return Collections.emptyList();
        }
    }

    // Conservamos el diálogo de registro existente para comodidad del usuario
    private void openDialog() {
        Dialog dlg = new Dialog();
        FormLayout form = new FormLayout();

        ComboBox<Paciente> cbPaciente = new ComboBox<>("Paciente");
        cbPaciente.setItems(loadPacientes());
        // El atributo 'nombre' ya contiene el nombre completo
        cbPaciente.setItemLabelGenerator(Paciente::getNombre);

        ComboBox<Fisioterapeuta> cbFisio = new ComboBox<>("Fisioterapeuta Responsable");
        cbFisio.setItems(fisioterapeutaService.obtenerTodosFisioterapeutas());
        cbFisio.setItemLabelGenerator(Fisioterapeuta::getNombre);

        ComboBox<EquipoBiomedico> cbEquipo = new ComboBox<>("Equipo");
        List<EquipoBiomedico> equipos = equipoService.findAll();
        equipos.removeIf(e -> !e.disponible());
        cbEquipo.setItems(equipos);
        cbEquipo.setItemLabelGenerator(EquipoBiomedico::getNombre);

        ComboBox<Insumo> cbInsumo = new ComboBox<>("Insumo");
        cbInsumo.setItems(insumoService.findAll());
        cbInsumo.setItemLabelGenerator(Insumo::getNombre);

        IntegerField duracion = new IntegerField("Duración (minutos)");
        IntegerField cantidadInsumo = new IntegerField("Cantidad de Insumo");

        Button btnGuardar = new Button("Guardar", e -> {
            Paciente paciente = cbPaciente.getValue();
            Fisioterapeuta fisio = cbFisio.getValue();
            EquipoBiomedico equipo = cbEquipo.getValue();
            Insumo insumo = cbInsumo.getValue();

            if (paciente == null || fisio == null || equipo == null || insumo == null || duracion.getValue() == null || cantidadInsumo.getValue() == null) {
                Notification notification = new Notification("Todos los campos son obligatorios.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(3000);
                notification.open();
                return;
            }

            try {
                // Construcción aplanada (clonado simple) para evitar ciclos al persistir
                Paciente pClon = new Paciente(paciente.getCedula(), paciente.getNombre(), paciente.getEdad(), paciente.getTipoLesion(), paciente.getNivelLesion());
                pClon.setId(paciente.getId());

                Fisioterapeuta fClon = new Fisioterapeuta(fisio.getCedula(), fisio.getEspecialidad(), fisio.getNombre());
                fClon.setId(fisio.getId());

                EquipoBiomedico eClon = new EquipoBiomedico(equipo.getId(), equipo.getNombre(), equipo.getFechaElaboracion(), equipo.getEstado());
                eClon.setHorasDeUso(equipo.getHorasDeUso());

                Insumo iClon = new Insumo(insumo.getId(), insumo.getNombre(), null, null, insumo.getStock(), insumo.getStockMinimo());

                SesionTratamiento s = new SesionTratamiento(
                        null,
                        java.time.LocalDate.now().toString(),
                        pClon,
                        fClon,
                        eClon,
                        iClon,
                        "Sesión registrada desde el panel de control general.",
                        duracion.getValue()
                );

                sesionService.crearSesion(s, cantidadInsumo.getValue());

                Notification notification = new Notification("¡Sesión registrada y recursos actualizados con éxito!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setDuration(3000);
                notification.open();

                dlg.close();
            } catch (Exception ex) {
                Notification notification = new Notification("Fallo en la transacción: " + ex.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(3000);
                notification.open();
            }
        });

        form.add(cbPaciente, cbFisio, cbEquipo, cbInsumo, duracion, cantidadInsumo, btnGuardar);
        dlg.add(form);
        dlg.open();
    }

    private void abrirEditorObservacion(SesionTratamiento sesion) {
        if (sesion == null) return;

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Editar Observación de la Sesión");
        dialog.setWidth("450px");


        TextArea txtObservacion = new TextArea("Observaciones");
        txtObservacion.setValue(sesion.getObservaciones() != null ? sesion.getObservaciones() : "");
        txtObservacion.setWidthFull();
        txtObservacion.setMinHeight("150px");
        txtObservacion.setPlaceholder("Escribe la evolución del paciente o detalles de la sesión...");

        // Botón de guardar
        Button btnGuardar = new Button("Guardar", e -> {
            try {

                sesion.setObservaciones(txtObservacion.getValue());


                sesionRepository.save(sesion);

                Notification n = Notification.show("Observación actualizada con éxito");
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);


                Paciente pacienteActual = pacienteCombo.getValue();
                if (pacienteActual != null) {
                    grid.setItems(sesionRepository.findByPaciente_Id(pacienteActual.getId()));
                }

                grid.deselectAll();
                dialog.close();
            } catch (Exception ex) {
                Notification.show("Error al guardar: " + ex.getMessage(), 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botón de cancelar
        Button btnCancelar = new Button("Cancelar", e -> {
            grid.deselectAll();
            dialog.close();
        });

        // Organizar la estructura del diálogo
        VerticalLayout layout = new VerticalLayout(txtObservacion);
        layout.setPadding(false);

        dialog.add(layout);
        dialog.getFooter().add(btnCancelar, btnGuardar);

        dialog.open();
    }

}

