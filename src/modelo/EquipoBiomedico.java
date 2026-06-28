package modelo;

/**
 * Modela las máquinas especializadas de la clínica (ej. Ultrasonido, Magnetoterapia).
 * Encapsula el estado operativo del equipo (Disponible, Ocupado, Mantenimiento)
 * y acumula sus horas de uso para evaluar si requiere soporte técnico preventivo.
 *
 * @author [Roberto Cordero]
 * @version 1.0
 * @since 2026-05-19
 */

public class EquipoBiomedico {

    private String id;
    private String nombre;
    private String estado;

    public EquipoBiomedico(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.estado = "Disponible";
    }

    public String getId() {
        return id;
    }

    public void asignarPaciente(String paciente) {

        if (estado.equals("Disponible")) {

            estado = "En Uso";

            System.out.println("\nEquipo asignado correctamente.");
            System.out.println("Paciente: " + paciente);
            System.out.println("Equipo: " + nombre);

        } else {

            System.out.println("El equipo no está disponible.");
            System.out.println("Estado actual: " + estado);
        }
    }

    public void liberarEquipo() {
        estado = "Disponible";
    }

    public void enviarMantenimiento() {
        estado = "Mantenimiento";
    }

    public void mostrarEquipo() {

        System.out.println("\n===== EQUIPO BIOMÉDICO =====");
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Estado: " + estado);
    }
}
