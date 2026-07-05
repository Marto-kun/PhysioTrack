package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import main.java.modelo.Paciente;
import main.java.modelo.PlanRehabilitacion;
import main.java.services.PacienteService;
import main.java.services.PlanRehabilitacionService;

import java.util.List;

public class PlanesView extends VerticalLayout {

    private final PacienteService pacienteService;
    private final PlanRehabilitacionService planService;

    private final ComboBox<Paciente> cbPaciente = new ComboBox<>("Seleccione Paciente");
    private final Div banner = new Div();
    private final Grid<PlanRehabilitacion> grid = new Grid<>(PlanRehabilitacion.class, false);

    public PlanesView(PacienteService pacienteService, PlanRehabilitacionService planService) {
        this.pacienteService = pacienteService;
        this.planService = planService;

        setWidthFull();

        cbPaciente.setItems(pacienteService.obtenerTodosPacientes());
        cbPaciente.setItemLabelGenerator(Paciente::getNombre);
        cbPaciente.addValueChangeListener(e -> onPacienteSelected(e.getValue()));

        grid.addColumn(PlanRehabilitacion::getEjercicio).setHeader("Ejercicio");
        grid.addColumn(PlanRehabilitacion::getSeries).setHeader("Series");
        grid.addColumn(PlanRehabilitacion::getRepeticiones).setHeader("Repeticiones");
        grid.addColumn(PlanRehabilitacion::getDiasPorSemana).setHeader("Días/Sem");
        grid.setWidthFull();
        grid.setHeight("450px");

        Button btnAsignar = new Button("Asignar Nuevo Plan", e -> openAsignarDialog());

        add(new H4("Seguimiento de Planes"), cbPaciente, banner, btnAsignar, grid);
    }

    private void onPacienteSelected(Paciente p) {
        if (p == null) {
            banner.removeAll();
            grid.setItems(List.of());
            return;
        }
        String seguimiento;
        try {
            seguimiento = new main.java.negocio.EvaluadorRutinas().seguimientoSemanal(p.getCedula());
        } catch (Exception ex) {
            seguimiento = "Error al calcular seguimiento";
        }
        banner.removeAll();
        banner.add(seguimiento);

        List<PlanRehabilitacion> planes = planService.findByPacienteCedula(p.getCedula());
        grid.setItems(planes);
    }

    private void openAsignarDialog() {
        Dialog dlg = new Dialog();
        ComboBox<Paciente> pacienteBox = new ComboBox<>("Paciente");
        pacienteBox.setItems(pacienteService.obtenerTodosPacientes());
        pacienteBox.setItemLabelGenerator(Paciente::getNombre);

        TextField ejercicio = new TextField("Ejercicio");
        IntegerField series = new IntegerField("Series");
        IntegerField repeticiones = new IntegerField("Repeticiones");
        IntegerField dias = new IntegerField("Días por semana");

        Button guardar = new Button("Guardar", e -> {
            Paciente p = pacienteBox.getValue();
            if (p == null) return;
            PlanRehabilitacion plan = new PlanRehabilitacion(java.util.UUID.randomUUID().toString(), p, ejercicio.getValue(), series.getValue(), repeticiones.getValue(), dias.getValue());
            // Validaciones del evaluador se ejecutan dentro del servicio
            planService.asignarPlan(p, plan, p.getNivelLesion());
            onPacienteSelected(p);
            dlg.close();
        });

        dlg.add(pacienteBox, ejercicio, series, repeticiones, dias, guardar);
        dlg.open();
    }
}

