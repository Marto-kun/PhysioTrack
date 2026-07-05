package main.java.com.physiotrack.gui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import main.java.com.physiotrack.gui.views.RegistroFisioterapeutaView;
import main.java.com.physiotrack.gui.views.PacientesView;
import main.java.services.FisioterapeutaService;
import main.java.services.PacienteService;
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

    private Div contentArea;
    private RegistroFisioterapeutaView registroFisioterapeutaView;
    private PacientesView pacientesView;

    public MainView(FisioterapeutaService fisioterapeutaService, PacienteService pacienteService) {
        this.fisioterapeutaService = fisioterapeutaService;
        this.pacienteService = pacienteService;
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

        drawer.add(btnRegistroFisio, btnPacientes);

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
}



