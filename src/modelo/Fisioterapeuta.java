package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa al profesional de la salud (Fisioterapeuta) dentro del sistema PhysioTrack.
 * Se encarga de centralizar la gestión de los pacientes asignados a su cargo, coordinar
 * los planes de rehabilitación y liderar las sesiones de tratamiento clínico.
 *
 * @author [Martín Vozmediano]
 * @version 1.1
 * @since 2026-06-28
 */

public class Fisioterapeuta {

    private String cedula;
    private String nombre;
    private String especialidad;
    private List<Paciente> pacientesAsignados = new ArrayList<>();

    public Fisioterapeuta(String cedula, String especialidad, String nombre) {
        this.cedula = cedula;
        this.especialidad = especialidad;
        this.nombre = nombre;
    }

    /**
     * Asignacion de un paciente nuevo al fisioterapeuta
     * @param nuevo Objeto Paciente que se desea asignar
     */
    public void asignarPaciente(Paciente nuevo){
        if (nuevo != null && !pacientesAsignados.contains(nuevo)){
            pacientesAsignados.add(nuevo);
        }else{
            System.out.println("Error: El paciente ya está asignado o es nulo.");
        }
    }

    /**
     * Eliminar paciente de la lista cuando reciba el alta o se decida
     * @param paciente objeto Paciente a eliminar
     */
    public void eliminarPaciente(Paciente paciente){
        if (paciente != null && pacientesAsignados.contains(paciente)){
            pacientesAsignados.remove(paciente);
        }else{
            System.out.println("Error: El paciente no está asignado o es nulo.");
        }
    }

    /**
     * Cantidad de pacientes asignnados al fisioterapeuta
     * @return
     */
    public int cantidadPacientes(){
        return pacientesAsignados.size();
    }


    // Getters y Setters

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Paciente> getPacientesAsignados() {
        return pacientesAsignados;
    }

    public void setPacientesAsignados(List<Paciente> pacientesAsignados) {
        this.pacientesAsignados = pacientesAsignados;
    }
}
