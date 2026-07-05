package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import main.java.modelo.EquipoBiomedico;
import main.java.modelo.Insumo;
import main.java.services.EquipoBiomedicoService;
import main.java.services.InsumoService;

import java.util.List;

public class InventarioView extends VerticalLayout {

    private final InsumoService insumoService;
    private final EquipoBiomedicoService equipoService;

    private final Grid<Insumo> gridInsumos = new Grid<>(Insumo.class, false);
    private final Grid<EquipoBiomedico> gridEquipos = new Grid<>(EquipoBiomedico.class, false);

    public InventarioView(InsumoService insumoService, EquipoBiomedicoService equipoService) {
        this.insumoService = insumoService;
        this.equipoService = equipoService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        H3 title = new H3("Gestión de Recursos Clínicos");
        add(title);

        Tab tabInsumos = new Tab("Insumos");
        Tab tabEquipos = new Tab("Equipos Biomédicos");
        Tabs tabs = new Tabs(tabInsumos, tabEquipos);
        tabs.setWidthFull();

        Div contentInsumos = buildInsumosPanel();
        Div contentEquipos = buildEquiposPanel();

        VerticalLayout content = new VerticalLayout(contentInsumos, contentEquipos);
        contentInsumos.setWidthFull();
        content.setPadding(false);
        content.setSpacing(true);

        contentInsumos.setVisible(true);
        contentEquipos.setVisible(false);

        tabs.addSelectedChangeListener(e -> {
            contentInsumos.setVisible(e.getSelectedTab() == tabInsumos);
            contentEquipos.setVisible(e.getSelectedTab() == tabEquipos);
        });

        add(tabs, content);
    }

    private Div buildInsumosPanel() {
        Div panel = new Div();
        panel.setWidthFull();

        gridInsumos.setItems(loadInsumos());
        gridInsumos.addColumn(Insumo::getNombre).setHeader("Nombre").setAutoWidth(true);
        gridInsumos.addColumn(Insumo::getStock).setHeader("Stock").setAutoWidth(true);
        gridInsumos.addColumn(Insumo::getStockMinimo).setHeader("Stock Mínimo").setAutoWidth(true);
        gridInsumos.setWidthFull();
        gridInsumos.setHeight("450px");

        Button btnRefrescar = new Button("Refrescar", e -> gridInsumos.setItems(loadInsumos()));
        Button btnAgregar = new Button("Agregar Insumo", e -> openInsumoDialog(null));

        panel.add(new HorizontalLayout(btnRefrescar, btnAgregar), gridInsumos);
        return panel;
    }

    private Div buildEquiposPanel() {
        Div panel = new Div();
        panel.setWidthFull();

        gridEquipos.setItems(loadEquipos());
        gridEquipos.addColumn(EquipoBiomedico::getNombre).setHeader("Nombre").setAutoWidth(true);
        gridEquipos.addColumn(EquipoBiomedico::getEstado).setHeader("Estado").setAutoWidth(true);
        gridEquipos.addColumn(EquipoBiomedico::getHorasDeUso).setHeader("Horas de Uso").setAutoWidth(true);
        gridEquipos.setWidthFull();
        gridEquipos.setHeight("450px");

        Button btnRefrescar = new Button("Refrescar", e -> gridEquipos.setItems(loadEquipos()));
        Button btnAgregar = new Button("Agregar Equipo", e -> openEquipoDialog(null));

        panel.add(new HorizontalLayout(btnRefrescar, btnAgregar), gridEquipos);
        return panel;
    }

    private List<Insumo> loadInsumos() {
        return insumoService.findAll();
    }

    private List<EquipoBiomedico> loadEquipos() {
        return equipoService.findAll();
    }

    private void openInsumoDialog(Insumo insumo) {
        Dialog dlg = new Dialog();
        dlg.setCloseOnEsc(true);
        dlg.setCloseOnOutsideClick(true);

        TextField nombre = new TextField("Nombre");
        NumberField stock = new NumberField("Stock");
        NumberField stockMin = new NumberField("Stock Mínimo");

        Button btnGuardar = new Button("Guardar", e -> {
            Insumo i = new Insumo(insumo != null ? insumo.getId() : null, nombre.getValue(), null, null, (int) stock.getValue().doubleValue(), (int) stockMin.getValue().doubleValue());
            insumoService.save(i);
            gridInsumos.setItems(loadInsumos());
            dlg.close();
        });

        Button btnConsumir = new Button("Consumir", e -> {
            if (insumo == null) return;
            NumberField cantidad = new NumberField("Cantidad");
            Button confirmar = new Button("Confirmar", ev -> {
                insumoService.consumirInsumo(insumo.getId(), (int) cantidad.getValue().doubleValue());
                gridInsumos.setItems(loadInsumos());
                dlg.close();
            });
            VerticalLayout inner = new VerticalLayout(cantidad, confirmar);
            dlg.removeAll();
            dlg.add(inner);
        });

        VerticalLayout layout = new VerticalLayout(nombre, stock, stockMin, new HorizontalLayout(btnGuardar, btnConsumir));
        dlg.add(layout);
        dlg.open();
    }

    private void openEquipoDialog(EquipoBiomedico equipo) {
        Dialog dlg = new Dialog();
        TextField nombre = new TextField("Nombre");
        TextField fecha = new TextField("Fecha Elaboración");
        TextField estado = new TextField("Estado");
        NumberField horas = new NumberField("Horas de Uso");

        Button btnGuardar = new Button("Guardar", e -> {
            EquipoBiomedico eq = new EquipoBiomedico(equipo != null ? equipo.getId() : null, nombre.getValue(), fecha.getValue(), estado.getValue());
            eq.setHorasDeUso((int) horas.getValue().doubleValue());
            equipoService.save(eq);
            gridEquipos.setItems(loadEquipos());
            dlg.close();
        });

        Button btnMant = new Button("Enviar a Mantenimiento", e -> {
            if (equipo == null) return;
            equipoService.enviarAMantenimiento(equipo.getId());
            gridEquipos.setItems(loadEquipos());
        });

        Button btnLiberar = new Button("Liberar Equipo", e -> {
            if (equipo == null) return;
            equipoService.liberarEquipo(equipo.getId(), 1);
            gridEquipos.setItems(loadEquipos());
        });

        VerticalLayout layout = new VerticalLayout(nombre, fecha, estado, horas, new HorizontalLayout(btnGuardar, btnMant, btnLiberar));
        dlg.add(layout);
        dlg.open();
    }
}

