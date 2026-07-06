package main.java.modelo;

/**
 * Representa el evento de una sesión de terapia física en tiempo real.
 * Funciona como entidad integradora que vincula al paciente con las observaciones
 * médicas, los equipos biomédicos y los insumos utilizados durante la atención.
 *
 * @author [Por llenar]
 * @version 1.0
 * @since 2026-05-19
 */

public class SesionTratamiento {

    private String idSesion;
    private String fecha;
    private Paciente paciente;
    private EquipoBiomedico equipo;
    private Insumo insumo;
    private String observaciones;
    private int duracionMinutos;
    private Fisioterapeuta fisioterapeutaResponsable;

    public SesionTratamiento(String idSesion,
                             String fecha,
                             Paciente paciente,
                             Fisioterapeuta fisioterapeutaResponsable,
                             EquipoBiomedico equipo,
                             Insumo insumo,
                             String observaciones,
                             int duracionMinutos) {

        this.idSesion = idSesion;
        this.fecha = fecha;
        this.paciente = paciente;
        this.fisioterapeutaResponsable = fisioterapeutaResponsable;
        this.equipo = equipo;
        this.insumo = insumo;
        this.observaciones = observaciones;
        this.duracionMinutos = duracionMinutos;
    }

    // Getters

    public String getIdSesion() {
        return idSesion;
    }

    public String getFecha() {
        return fecha;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public EquipoBiomedico getEquipo() {
        return equipo;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Fisioterapeuta getFisioterapeutaResponsable() {
        return fisioterapeutaResponsable;
    }

    // Setters

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void cambiarEquipo(EquipoBiomedico equipo) {
        this.equipo = equipo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public void setFisioterapeutaResponsable(Fisioterapeuta fisioterapeutaResponsable) {
        this.fisioterapeutaResponsable = fisioterapeutaResponsable;
    }

    // Métodos

    public void agregarObservacion(String nuevaObservacion) {

        if (observaciones == null || observaciones.isEmpty()) {
            observaciones = nuevaObservacion;
        } else {
            observaciones += "\n" + nuevaObservacion;
        }
    }

    public boolean sesionLarga() {
        return duracionMinutos >= 60;
    }
}
