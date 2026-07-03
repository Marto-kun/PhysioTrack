package negocio;

import modelo.EquipoBiomedico;
import modelo.Insumo;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsable de coordinar la disponibilidad de recursos físicos de la clínica.
 * Valida el stock disponible de consumibles antes de una sesión e impide la asignación
 * de equipos biomédicos que se encuentren ocupados por otro especialista.
 *
 * @author [Martin Vozmediano]
 * @version 1.0
 * @since 2026-05-19
 */

public class ControladorRecursos {

    //Recursos, final para proteger las variables
    private final List<Insumo> listaInsumos = new ArrayList<>();
    private final List<EquipoBiomedico> listaEquipos = new ArrayList<>();

    public ControladorRecursos() {
        //Contructor vacio
    }

    public void registrarInsumo(Insumo nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("Error: El Insumo no puede ser nulo");
        }

        if (!listaInsumos.contains(nuevo)) {
            listaInsumos.add(nuevo);
        } else {
            throw new IllegalStateException("Error: El insumo ya ha sido registrado. Intente con uno nuevo.");
        }

    }

    public void registrarEquipo(EquipoBiomedico nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("Error: El equipo no puede ser nulo.");
        }
        if (!listaEquipos.contains(nuevo)) {
            listaEquipos.add(nuevo);

        } else {
            throw new IllegalStateException("Error: El equipo ya se encuentra registrado.");
        }

    }

    public void consumirInsumo(Insumo usando, int cantidad) {

        if (usando == null) {
            throw new IllegalArgumentException("Error: El insumo no puede ser nulo");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("Error: La cantidad a consumir debe ser mayor que cero.");
        }

        if (cantidad >= usando.getStockMinimo()) {
            throw new IllegalArgumentException("Error: La cantidad a consumir no puede ser mayor ni igual que el stock disponible del insumo " + usando.getNombre());
        }


        if (!listaInsumos.contains(usando)) {
            throw new IllegalStateException("Error: El insumo no se encuentra registrado.");
        }

        if (usando.getStock() <= usando.getStockMinimo()) {
            throw new IllegalStateException("Error: No hay stock disponible del insumo " + usando.getNombre());
        }

        // Disminuir el stock en la cantidad especificada
        usando.utilizarInsumo(cantidad);
    }

    public void asignarEquipo(EquipoBiomedico asignado) {
        if (asignado == null) {
            throw new IllegalArgumentException("Error: El equipo no puede ser nulo");
        }

        if (asignado.getEstado().equals("Ocupado")) {
            throw new IllegalStateException("Error: El equipo " + asignado.getNombre() + " ya se encuentra ocupado.");
        }

        if (asignado.necesitaMantenimiento()) {
            asignado.enviarMantenimiento();
            throw new IllegalStateException("Error: El equipo " + asignado.getNombre() + " necesita mantenimiento.");
        }

        if (asignado.getEstado().equals("Mantenimiento")) {
            throw new IllegalStateException("Error: El equipo " + asignado.getNombre() + " se encuentra en mantenimiento.");
        }

        asignado.asignarEquipo();

    }

    public void liberarEquipo(EquipoBiomedico liberado, int horas) {
        if (liberado == null) {
            throw new IllegalArgumentException("Error: El equipo no puede ser nulo");
        }

        if (liberado.getEstado().equals("Disponible")) {
            throw new IllegalStateException("Error: El equipo " + liberado.getNombre() + " ya se encuentra disponible.");
        }

        liberado.liberarEquipo();
        liberado.setHorasDeUso(liberado.getHorasDeUso() + horas);
    }

    public void registrarReparacionManual(String idEquipo) {
        if (idEquipo == null || idEquipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: El ID del equipo no puede estar vacío.");
        }

        EquipoBiomedico equipo = null;
        for (EquipoBiomedico e : listaEquipos) {
            if (e.getId().equals(idEquipo)) {
                equipo = e;
                break;
            }
        }

        if (equipo == null) {
            throw new IllegalArgumentException("Error: No se encontró ningún equipo con el ID proporcionada.");
        }

        if (!equipo.getEstado().equalsIgnoreCase("En Mantenimiento")) {
            throw new IllegalStateException("Error: El equipo '" + equipo.getNombre() +
                    "' no puede marcarse como reparado porque su estado actual es: " + equipo.getEstado() + ".");
        }

        equipo.setEstado("Disponible");
        equipo.setHorasDeUso(0);
    }

    //Getters de las listas
    public List<Insumo> getListaInsumos() {
        return listaInsumos;
    }

    public List<EquipoBiomedico> getListaEquipos() {
        return listaEquipos;
    }


}
