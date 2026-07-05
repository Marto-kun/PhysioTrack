package main.java.services;

import main.java.modelo.SesionTratamiento;
import main.java.negocio.ControladorRecursos;
import main.java.repositories.SesionTratamientoRepository;
import org.springframework.stereotype.Service;

@Service
public class SesionTratamientoService {

    private final SesionTratamientoRepository sesionRepository;
    private final ControladorRecursos controladorRecursos = new ControladorRecursos();

    public SesionTratamientoService(SesionTratamientoRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    /**
     * Crea y persiste una sesión de tratamiento. Antes de persistir, asigna el equipo
     * y consume el insumo requerido a través del controlador de recursos.
     *
     * @param sesion sesión a guardar (debe contener paciente, fisioterapeutaResponsable, equipo e insumo)
     * @param cantidadInsumo cantidad de insumo a consumir en la sesión
     * @return sesión guardada
     */
    public SesionTratamiento crearSesion(SesionTratamiento sesion, int cantidadInsumo) {
        if (sesion == null) throw new IllegalArgumentException("Sesion no puede ser nula");
        if (sesion.getEquipo() == null) throw new IllegalArgumentException("Equipo es requerido");
        if (sesion.getInsumo() == null) throw new IllegalArgumentException("Insumo es requerido");

        // Reservar equipo y consumir insumo en memoria
        controladorRecursos.asignarEquipo(sesion.getEquipo());
        controladorRecursos.consumirInsumo(sesion.getInsumo(), cantidadInsumo);

        // Persistir sesión sanitizada
        return sesionRepository.save(PersistenceSanitizer.sanitize(sesion));
    }
}

