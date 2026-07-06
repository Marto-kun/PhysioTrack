package main.java.com.physiotrack.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import main.java.com.physiotrack.gui.views.*;
import main.java.services.FisioterapeutaService;
import main.java.services.PacienteService;
import main.java.services.InsumoService;
import main.java.services.EquipoBiomedicoService;
import main.java.services.SesionTratamientoService;
import main.java.repositories.SesionTratamientoRepository;
import main.java.repositories.InsumoRepository;
import main.java.repositories.EquipoBiomedicoRepository;
import main.java.services.PlanRehabilitacionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Vista principal de la aplicación PhysioTrack.
 * Proporciona navegación entre las diferentes secciones de la aplicación.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
@Route("")
public class MainView extends AppLayout {

    @Autowired
    private FisioterapeutaService fisioterapeutaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private EquipoBiomedicoService equipoBiomedicoService;

    @Autowired
    private SesionTratamientoService sesionTratamientoService;

    @Autowired
    private SesionTratamientoRepository sesionTratamientoRepository;

    @Autowired
    private PlanRehabilitacionService planRehabilitacionService;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private EquipoBiomedicoRepository equipoBiomedicoRepository;

    private Div contentArea;
    private RegistroFisioterapeutaView registroFisioterapeutaView;
    private PacientesView pacientesView;
    private InventarioView inventarioView;
    private SesionesView sesionesView;
    private PlanesView planesView;

    public MainView(FisioterapeutaService fisioterapeutaService, PacienteService pacienteService,
                    InsumoService insumoService, EquipoBiomedicoService equipoBiomedicoService,
                    SesionTratamientoService sesionTratamientoService, PlanRehabilitacionService planRehabilitacionService) {
        this.fisioterapeutaService = fisioterapeutaService;
        this.pacienteService = pacienteService;
        this.insumoService = insumoService;
        this.equipoBiomedicoService = equipoBiomedicoService;
        this.sesionTratamientoService = sesionTratamientoService;
        this.planRehabilitacionService = planRehabilitacionService;
        createHeader();
        createDrawer();
        createContent();
    }

    private void createHeader() {
        H1 title = new H1("PhysioTrack");
        title.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.NONE,
            "header-title"
        );

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), title);
        header.setDefaultVerticalComponentAlignment(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.MEDIUM,
            LumoUtility.Padding.Horizontal.LARGE,
            "header"
        );

        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout drawer = new VerticalLayout();
        drawer.setSpacing(false);
        drawer.setPadding(false);
        drawer.setDefaultHorizontalComponentAlignment(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.STRETCH);

        H1 appTitle = new H1("PhysioTrack");
        appTitle.addClassNames(
            LumoUtility.FontSize.MEDIUM,
            LumoUtility.Margin.Vertical.MEDIUM,
            LumoUtility.Padding.Horizontal.MEDIUM
        );
        drawer.add(appTitle);

        // Botón para Registro de Fisioterapeutas
        Button btnRegistroFisio = new Button("Registrar Fisioterapeuta");
        btnRegistroFisio.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnRegistroFisio.setWidthFull();
        btnRegistroFisio.addClickListener(e -> showRegistroFisioterapeuta());

        // Botón para Gestión de Pacientes
        Button btnPacientes = new Button("Gestión de Pacientes");
        btnPacientes.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnPacientes.setWidthFull();
        btnPacientes.addClickListener(e -> showPacientes());

        // Botón para Gestión de Inventario
        Button btnInventario = new Button("Gestión de Inventario");
        btnInventario.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnInventario.setWidthFull();
        btnInventario.addClickListener(e -> showInventario());

        // Botón para Registro de Sesiones
        Button btnSesiones = new Button("Registrar Sesiones");
        btnSesiones.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnSesiones.setWidthFull();
        btnSesiones.addClickListener(e -> showSesiones());

        // Botón para Planes de Rehabilitación
        Button btnPlanes = new Button("Planes de Rehabilitación");
        btnPlanes.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnPlanes.setWidthFull();
        btnPlanes.addClickListener(e -> showPlanes());

        drawer.add(btnRegistroFisio, btnPacientes, btnInventario, btnSesiones, btnPlanes);

        addToDrawer(drawer);
    }

    private void createContent() {
        contentArea = new Div();
        contentArea.setWidthFull();
        contentArea.addClassNames(LumoUtility.Padding.LARGE);
        setContent(contentArea);

        // Mostrar la vista de registro de fisioterapeutas por defecto
        showRegistroFisioterapeuta();
    }

    private void showRegistroFisioterapeuta() {
        contentArea.removeAll();
        if (registroFisioterapeutaView == null) {
            registroFisioterapeutaView = new RegistroFisioterapeutaView(fisioterapeutaService);
        }
        contentArea.add(registroFisioterapeutaView);
    }

    private void showPacientes() {
        contentArea.removeAll();
        if (pacientesView == null) {
            pacientesView = new PacientesView(fisioterapeutaService, pacienteService);
        }
        contentArea.add(pacientesView);
    }

    private void showInventario() {
        contentArea.removeAll();

        Tab tabInsumos = new com.vaadin.flow.component.tabs.Tab("Insumos");
        Tab tabEquipos = new com.vaadin.flow.component.tabs.Tab("Equipos Biomédicos");
        Tabs tabs = new com.vaadin.flow.component.tabs.Tabs(tabInsumos, tabEquipos);
        tabs.setWidthFull();

        InventarioInsumosView vistaInsumos = new InventarioInsumosView(insumoRepository);
        InventarioEquiposView vistaEquipos = new InventarioEquiposView(equipoBiomedicoRepository);


        VerticalLayout vistaContenedor = new VerticalLayout(vistaInsumos, vistaEquipos);
        vistaContenedor.setPadding(false);
        vistaContenedor.setSpacing(false);

        vistaInsumos.setVisible(true);
        vistaEquipos.setVisible(false);

        tabs.addSelectedChangeListener(e -> {
            vistaInsumos.setVisible(e.getSelectedTab() == tabInsumos);
            vistaEquipos.setVisible(e.getSelectedTab() == tabEquipos);
        });

        contentArea.add(tabs, vistaContenedor);
    }

    private void showSesiones() {
        contentArea.removeAll();
        if (sesionesView == null) {
            sesionesView = new SesionesView(pacienteService, fisioterapeutaService,
                                           equipoBiomedicoService, insumoService, sesionTratamientoService, sesionTratamientoRepository);
        }
        contentArea.add(sesionesView);
    }

    private void showPlanes() {
        contentArea.removeAll();
        if (planesView == null) {
            planesView = new PlanesView(pacienteService, planRehabilitacionService);
        }
        contentArea.add(planesView);
    }
}



