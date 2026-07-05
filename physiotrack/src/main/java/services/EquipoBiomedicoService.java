package main.java.services;

import main.java.modelo.EquipoBiomedico;
import main.java.negocio.ControladorRecursos;
import main.java.repositories.EquipoBiomedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoBiomedicoService {

    private final EquipoBiomedicoRepository equipoRepository;
    private final ControladorRecursos controladorRecursos = new ControladorRecursos();

    public EquipoBiomedicoService(EquipoBiomedicoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<EquipoBiomedico> findAll() {
        return equipoRepository.findAll();
    }

    public EquipoBiomedico findById(String id) {
        return equipoRepository.findById(id).orElse(null);
    }

    public EquipoBiomedico save(EquipoBiomedico equipo) {
        if (equipo == null) throw new IllegalArgumentException("Equipo no puede ser nulo");
        controladorRecursos.registrarEquipo(equipo);
        return equipoRepository.save(PersistenceSanitizer.sanitize(equipo));
    }

    public EquipoBiomedico update(EquipoBiomedico equipo) {
        if (equipo == null || equipo.getId() == null) throw new IllegalArgumentException("Equipo o ID nulo");
        EquipoBiomedico existing = findById(equipo.getId());
        if (existing == null) throw new IllegalStateException("Equipo no encontrado");
        existing.setNombre(equipo.getNombre());
        existing.setEstado(equipo.getEstado());
        existing.setFechaElaboracion(equipo.getFechaElaboracion());
        existing.setHorasDeUso(equipo.getHorasDeUso());
        controladorRecursos.registrarEquipo(existing);
        return equipoRepository.save(PersistenceSanitizer.sanitize(existing));
    }

    public void enviarAMantenimiento(String id) {
        EquipoBiomedico e = findById(id);
        if (e == null) throw new IllegalArgumentException("Equipo no encontrado");
        if (e.necesitaMantenimiento()) {
            e.enviarMantenimiento();
            equipoRepository.save(PersistenceSanitizer.sanitize(e));
        } else {
            // Forzar cambio de estado y persistir
            e.enviarMantenimiento();
            equipoRepository.save(PersistenceSanitizer.sanitize(e));
        }
    }

    public void liberarEquipo(String id, int horas) {
        EquipoBiomedico e = findById(id);
        if (e == null) throw new IllegalArgumentException("Equipo no encontrado");
        e.liberarEquipo();
        e.setHorasDeUso(e.getHorasDeUso() + horas);
        equipoRepository.save(PersistenceSanitizer.sanitize(e));
    }
}

