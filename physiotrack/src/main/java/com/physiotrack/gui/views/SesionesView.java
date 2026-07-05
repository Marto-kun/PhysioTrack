package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
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

import java.util.List;

public class SesionesView extends VerticalLayout {

    private final PacienteService pacienteService;
    private final FisioterapeutaService fisioterapeutaService;
    private final EquipoBiomedicoService equipoService;
    private final InsumoService insumoService;
    private final SesionTratamientoService sesionService;

    public SesionesView(PacienteService pacienteService,
                        FisioterapeutaService fisioterapeutaService,
                        EquipoBiomedicoService equipoService,
                        InsumoService insumoService,
                        SesionTratamientoService sesionService) {
        this.pacienteService = pacienteService;
        this.fisioterapeutaService = fisioterapeutaService;
        this.equipoService = equipoService;
        this.insumoService = insumoService;
        this.sesionService = sesionService;

        setWidthFull();
        Button btnNueva = new Button("Registrar Sesión", e -> openDialog());
        add(btnNueva);
    }

    private void openDialog() {
        Dialog dlg = new Dialog();
        FormLayout form = new FormLayout();

        ComboBox<Paciente> cbPaciente = new ComboBox<>("Paciente");
        cbPaciente.setItems(pacienteService.obtenerTodosPacientes());
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

        NumberField duracion = new NumberField("Duración (minutos)");
        NumberField cantidadInsumo = new NumberField("Cantidad de Insumo");

        Button btnGuardar = new Button("Guardar", e -> {
            Paciente paciente = cbPaciente.getValue();
            Fisioterapeuta fisio = cbFisio.getValue();
            EquipoBiomedico equipo = cbEquipo.getValue();
            Insumo insumo = cbInsumo.getValue();
            if (paciente == null || fisio == null || equipo == null || insumo == null) return;

            SesionTratamiento s = new SesionTratamiento(null, java.time.LocalDate.now().toString(), paciente, fisio, equipo, insumo, "", (int) duracion.getValue().doubleValue());
            sesionService.crearSesion(s, (int) cantidadInsumo.getValue().doubleValue());
            dlg.close();
        });

        form.add(cbPaciente, cbFisio, cbEquipo, cbInsumo, duracion, cantidadInsumo, btnGuardar);
        dlg.add(form);
        dlg.open();
    }
}

