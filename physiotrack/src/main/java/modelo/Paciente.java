package main.java.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Representa a un paciente dentro del sistema PhysioTrack.
 * Almacena la información personal, datos de contacto y variables clínicas
 * de la lesión para calcular el nivel de prioridad de atención en la clínica.
 *
 * @author [Roberto Cordero]
 * @version 1.0
 * @since 2026-05-19
 */

@Document(collection = "pacientes")
public class Paciente {

    @Id
    private String id;
    private String cedula;
    private String nombre;
    private int edad;
    private String tipoLesion;
    private int nivelLesion;
    private String prioridad;
    
    private Fisioterapeuta fisioterapeutaAsignado;

    public Paciente() {
        // Constructor vacío para MongoDB
    }

    public Paciente(String cedula, String nombre, int edad,
                    String tipoLesion, int nivelLesion) {

        this.cedula = cedula;
        this.nombre = nombre;
        this.edad = edad;
        this.tipoLesion = tipoLesion;
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

    private void calcularPrioridad() {

        if (nivelLesion >= 8 || edad >= 65) {
            prioridad = "Urgente";
        } else if (nivelLesion >= 5) {
            prioridad = "Seguimiento";
        } else {
            prioridad = "Preventivo";
        }
    }

    /**
     * Comparacion mediante numero de cedula
     *
     * @param object the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Paciente otro = (Paciente) object;
        return this.getCedula() != null && this.getCedula().equals(otro.getCedula());
    }

    @Override
    public String toString() {
        return "Paciente: " + getNombre() +
                ", Cédula: " + getCedula() +
                ", Edad: " + edad
                + ", Tipo de Lesión: " + getTipoLesion() +
                ", Nivel de Lesión: " + getNivelLesion()
                + ", Prioridad: " + getPrioridad()
                + (fisioterapeutaAsignado != null ? ", Fisioterapeuta Asignado: " + fisioterapeutaAsignado.getNombre() : "");
    }

    // Getters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Fisioterapeuta getFisioterapeutaAsignado() {
        return fisioterapeutaAsignado;
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

    public void setFisioterapeutaAsignado(Fisioterapeuta fisioterapeutaAsignado) {
        this.fisioterapeutaAsignado = fisioterapeutaAsignado;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}

