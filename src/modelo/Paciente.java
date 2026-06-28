package modelo;

/**
 * Representa a un paciente dentro del sistema PhysioTrack.
 * Almacena la información personal, datos de contacto y variables clínicas
 * de la lesión para calcular el nivel de prioridad de atención en la clínica.
 *
 * @author [Roberto Cordero]
 * @version 1.0
 * @since 2026-05-19
 */

public class Paciente {

    private String cedula;
    private String nombre;
    private int edad;
    private String tipoLesion;
    private int nivelLesion;
    private String prioridad;

    public Paciente(String cedula, String nombre, int edad,
                    String tipoLesion, int nivelLesion) {

        this.cedula = cedula;
        this.nombre = nombre;
        this.edad = edad;
        this.tipoLesion = tipoLesion;
        this.nivelLesion = nivelLesion;

        calcularPrioridad();
    }

    private void calcularPrioridad() {

        if (nivelLesion >= 8 || edad >= 65) {
            prioridad = "Urgente";
        } else if (nivelLesion >= 5) {
            prioridad = "Seguimiento";
        } else {
            prioridad = "Preventivo";
        }
    }

    // Getters

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getTipoLesion() {
        return tipoLesion;
    }

    public int getNivelLesion() {
        return nivelLesion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    // Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEdad(int edad) {
        this.edad = edad;
        calcularPrioridad();
    }

    public void setTipoLesion(String tipoLesion) {
        this.tipoLesion = tipoLesion;
    }

    public void setNivelLesion(int nivelLesion) {
        this.nivelLesion = nivelLesion;
        calcularPrioridad();
    }

    // Métodos

    public boolean esAdultoMayor() {
        return edad >= 65;
    }

    public boolean requiereAtencionUrgente() {
        return prioridad.equals("Urgente");
    }
}
