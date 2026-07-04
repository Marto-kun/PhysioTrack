package main.java.modelo;

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
    private String fechaElaboracion;
    private String estado;
    private int horasDeUso = 0;

    public EquipoBiomedico(String id,
                           String nombre,
                           String fechaElaboracion,
                           String estado) {

        this.id = id;
        this.nombre = nombre;
        this.fechaElaboracion = fechaElaboracion;
        this.estado = estado;
    }

    // Métodos

    public boolean disponible() {
        return estado.equalsIgnoreCase("Disponible");
    }

    public void asignarEquipo() {

        if (!disponible()) {
            throw new IllegalArgumentException("El equipo no está disponible.");
        }

        estado = "En Uso";
    }

    public boolean necesitaMantenimiento(){
        return this.horasDeUso >= 50;
    }

    public void liberarEquipo() {
        estado = "Disponible";
    }

    public void enviarMantenimiento() {
        estado = "Mantenimiento";
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaElaboracion() {
        return fechaElaboracion;
    }

    public String getEstado() {
        return estado;
    }

    public int getHorasDeUso() {
        return horasDeUso;
    }

    // Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setHorasDeUso(int horasDeUso) {
        this.horasDeUso = horasDeUso;
    }
}
