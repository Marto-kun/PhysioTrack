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

    public void consumirInsumo(Insumo usando, int cantidad){

        if (cantidad <= 0){
            throw new IllegalArgumentException("Error: La cantidad a consumir debe ser mayor que cero.");
        }

        if(cantidad >= usando.getStock()){
            throw new IllegalArgumentException("Error: La cantidad a consumir no puede ser mayor que el stock disponible del insumo " + usando.getNombre());
        }

        if (usando == null) {
            throw new IllegalArgumentException("Error: El insumo no puede ser nulo");
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





}
