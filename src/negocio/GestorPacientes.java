package negocio;

import modelo.Paciente;
import modelo.Fisioterapeuta;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de la lógica de negocio para la administración de pacientes.
 * Gestiona el registro, persistencia en colecciones y el proceso automatizado
 * de categorización y asignación de prioridades médicas según la gravedad de la lesión.
 *
 * @author [Martin Vozmediano]
 * @version 1.0
 * @since 2026-05-19
 */

public class GestorPacientes {

    private List<Paciente> listaPacientes = new ArrayList();

    public GestorPacientes() {
        //Constructor vacio
    }

    /**
     * Metodo para ingresar un paciente a una listaPacientes
     *
     * @param nuevo
     */
    public void agregarPaciente(Paciente nuevo) {

        if (nuevo == null) {
            throw new IllegalArgumentException("Error: El Paciente no puede ser nulo");
        }
        if (!listaPacientes.contains(nuevo)) {
            listaPacientes.add(nuevo);
        } else {
            System.out.println("Error: El paciente ya ha sido registrado. Intente con uno nuevo.");
        }
    }

    /**
     * Metodo para asignar un paciente a un fisioterapeuta e incluirlo en listaPacientes
     *
     * @param paciente
     * @param fisio
     */
    public void registrarAsignacion(Paciente paciente, Fisioterapeuta fisio) {
        if (paciente == null || fisio == null) {
            // Revisar que no sean null
            throw new IllegalArgumentException("Error: El Paciente y el Fisioterapeuta no pueden ser nulos");
        }

        if (fisio.cantidadPacientes() >= 10) {
            // Revisar cantidad de pacientes
            throw new IllegalStateException("Error: El fisioterapeuta " + fisio.getNombre() + " ya alcanzó el límite máximo de 10 pacientes.");
        }

        paciente.setFisioterapeutaAsignado(fisio);// Asignar fisioterapeuta al paciente
        fisio.asignarPaciente(paciente); //Asignar el paciente al fisioterapeuta

        agregarPaciente(paciente); //Agregar a la lista del paciente
    }

    /**
     * Metodo de busqueda de pacientes según una cédula
     *
     * @param cedula
     * @return datos del paciente si se encontró
     */
    public Paciente buscarPorCedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("Error: La cédula no puede ser nula o vacía");
        }

        for (Paciente p : listaPacientes) {
            if (p.getCedula().equals(cedula)) {
                System.out.println("Se ha encontrado el siguiente paciente: \n");
                return p;
            }
        }
        throw new IllegalArgumentException("Error: No se encontró un paciente con la cédula proporcionada");
    }

    /**
     * Metodo para encontrar los pacientes asignados a cada fisioterapeuta
     * @param fisio fisioterapeuta a comprobar
     * @return lista con los pacientes por fisio
     */
    public List<Paciente> pacientesPorFisio(Fisioterapeuta fisio) {
        if (fisio == null) {
            throw new IllegalArgumentException("Error: El fisioterapeuta no puede ser nulo");
        }

        List<Paciente> asignados = new ArrayList<>();
        for (Paciente p : listaPacientes) {
            if (p.getFisioterapeutaAsignado() != null && p.getFisioterapeutaAsignado().equals(fisio)) {
                asignados.add(p);
            }
        }
        return asignados;
    }


    /**
     * Metodo para buscar que pacientes registrados son urgentes
     * @return lista de pacientes urgentes encontrados
     */
    public List<Paciente> pacientesUrgentes() {
        List<Paciente> urgentes = new ArrayList<>();

        for (Paciente p : listaPacientes) {
            if (p.requiereAtencionUrgente()) {
                urgentes.add(p);
            }
        }
        return urgentes;
    }

}
