package main.java.com.physiotrack.gui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import main.java.modelo.Fisioterapeuta;
import main.java.services.FisioterapeutaService;

/**
 * Vista para el registro de nuevos Fisioterapeutas.
 * Permite crear perfiles de fisioterapeutas con sus datos básicos.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
public class RegistroFisioterapeutaView extends VerticalLayout {

    private FisioterapeutaService fisioterapeutaService;

    private TextField cedulaField;
    private TextField nombreField;
    private ComboBox<String> especialidadCombo;

    public RegistroFisioterapeutaView(FisioterapeutaService fisioterapeutaService) {
        this.fisioterapeutaService = fisioterapeutaService;
        initializeView();
    }

    private void initializeView() {
        setSpacing(true);
        setPadding(true);
        setMaxWidth("800px");
        addClassNames(LumoUtility.Margin.AUTO);

        // Título
        H2 title = new H2("Registro de Nuevo Fisioterapeuta");
        title.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.TextColor.PRIMARY,
            LumoUtility.Margin.Bottom.LARGE
        );

        // Descripción
        Div description = new Div();
        description.setText("Ingrese los datos del fisioterapeuta a registrar en el sistema.");
        description.addClassNames(
            LumoUtility.TextColor.SECONDARY,
            LumoUtility.Margin.Bottom.LARGE,
            LumoUtility.FontSize.SMALL
        );

        // Formulario
        FormLayout form = createForm();

        // Botones de acción
        Div buttonArea = new Div();
        buttonArea.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.MEDIUM);

        Button btnGuardar = new Button("Guardar Fisioterapeuta");
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGuardar.addClickListener(e -> saveFisioterapeuta());

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnLimpiar.addClickListener(e -> clearForm());

        buttonArea.add(btnGuardar, btnLimpiar);

        add(title, description, form, buttonArea);
    }

    private FormLayout createForm() {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );

        // Campo Cédula
        cedulaField = new TextField("Cédula de Identidad");
        cedulaField.setPlaceholder("Ingrese cédula");
        cedulaField.setRequired(true);
        cedulaField.setHelperText("Cédula única del fisioterapeuta");

        // Campo Nombre
        nombreField = new TextField("Nombre Completo");
        nombreField.setPlaceholder("Ingrese nombre");
        nombreField.setRequired(true);
        nombreField.setHelperText("Nombre completo del profesional");

        // ComboBox Especialidad
        especialidadCombo = new ComboBox<>("Especialidad");
        especialidadCombo.setItems(
            "Fisioterapia General",
            "Ortopedia",
            "Neurología",
            "Cardiología",
            "Pediatría",
            "Deportología",
            "Reumatología"
        );
        especialidadCombo.setRequired(true);
        especialidadCombo.setHelperText("Seleccione la especialidad");

        form.add(cedulaField, nombreField, especialidadCombo);
        form.setColspan(nombreField, 2);

        return form;
    }

    private void saveFisioterapeuta() {
        if (!validateForm()) {
            showError("Por favor complete todos los campos requeridos");
            return;
        }

        try {
            Fisioterapeuta fisioterapeuta = new Fisioterapeuta(
                cedulaField.getValue(),
                especialidadCombo.getValue(),
                nombreField.getValue()
            );

            if (fisioterapeutaService != null) {
                fisioterapeutaService.registrarFisioterapeuta(fisioterapeuta);
            }

            showSuccess("¡Fisioterapeuta registrado exitosamente!");
            clearForm();
        } catch (IllegalStateException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error al registrar el fisioterapeuta: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        return !cedulaField.getValue().isEmpty() &&
               !nombreField.getValue().isEmpty() &&
               especialidadCombo.getValue() != null;
    }

    private void clearForm() {
        cedulaField.clear();
        nombreField.clear();
        especialidadCombo.clear();
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


