package modelo;

/**
 * Modela el plan de ejercicios terapéuticos asignado a un paciente.
 * Controla la intensidad de las rutinas (series, repeticiones y días) y realiza
 * el seguimiento del porcentaje de cumplimiento y progreso físico del tratamiento.
 *
 * @author [Por llenar]
 * @version 1.0
 * @since 2026-05-19
 */

public class PlanRehabilitacion {

    private String idPlan;
    private String nombrePaciente;
    private String ejercicio;

    private int series;
    private int repeticiones;
    private int diasPorSemana;

    private double porcentajeCumplimiento;
    private double progresoFisico;

    public PlanRehabilitacion(String idPlan,
                              String nombrePaciente,
                              String ejercicio,
                              int series,
                              int repeticiones,
                              int diasPorSemana) {

        this.idPlan = idPlan;
        this.nombrePaciente = nombrePaciente;
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
        this.diasPorSemana = diasPorSemana;

        this.porcentajeCumplimiento = 0;
        this.progresoFisico = 0;
    }

    // Getters

    public String getIdPlan() {
        return idPlan;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public int getSeries() {
        return series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public int getDiasPorSemana() {
        return diasPorSemana;
    }

    public double getPorcentajeCumplimiento() {
        return porcentajeCumplimiento;
    }

    public double getProgresoFisico() {
        return progresoFisico;
    }

    // Setters

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setDiasPorSemana(int diasPorSemana) {
        this.diasPorSemana = diasPorSemana;
    }

    // Métodos

    public void actualizarCumplimiento(double porcentaje) {

        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
        }

        porcentajeCumplimiento = porcentaje;
    }

    public void actualizarProgreso(double progreso) {

        if (progreso < 0 || progreso > 100) {
            throw new IllegalArgumentException("El progreso debe estar entre 0 y 100.");
        }

        progresoFisico = progreso;
    }

    public boolean tratamientoCompletado() {
        return porcentajeCumplimiento == 100;
    }

    public boolean progresoSatisfactorio() {
        return progresoFisico >= 80;
    }
}
