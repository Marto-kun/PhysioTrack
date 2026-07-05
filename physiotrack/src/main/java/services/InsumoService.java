package main.java.services;

import main.java.modelo.Insumo;
import main.java.negocio.ControladorRecursos;
import main.java.repositories.InsumoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;
    private final ControladorRecursos controladorRecursos = new ControladorRecursos();

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public List<Insumo> findAll() {
        return insumoRepository.findAll();
    }

    public Insumo findById(String id) {
        return insumoRepository.findById(id).orElse(null);
    }

    public Insumo save(Insumo insumo) {
        if (insumo == null) throw new IllegalArgumentException("Insumo no puede ser nulo");
        // Registrar en controlador de recursos para mantener sincronía en memoria
        controladorRecursos.registrarInsumo(insumo);
        Insumo sanitized = PersistenceSanitizer.sanitize(insumo);
        return insumoRepository.save(sanitized);
    }

    public Insumo update(Insumo insumo) {
        if (insumo == null || insumo.getId() == null) throw new IllegalArgumentException("Insumo o ID nulo");
        Insumo existing = findById(insumo.getId());
        if (existing == null) throw new IllegalStateException("Insumo no encontrado");
        // Como el modelo Insumo no provee setters para stock, construimos una nueva instancia
        Insumo updated = new Insumo(existing.getId(),
                insumo.getNombre() != null ? insumo.getNombre() : existing.getNombre(),
                insumo.getFechaElaboracion() != null ? insumo.getFechaElaboracion() : existing.getFechaElaboracion(),
                insumo.getFechaCaducidad() != null ? insumo.getFechaCaducidad() : existing.getFechaCaducidad(),
                insumo.getStock() != 0 ? insumo.getStock() : existing.getStock(),
                insumo.getStockMinimo() != 0 ? insumo.getStockMinimo() : existing.getStockMinimo());
        controladorRecursos.registrarInsumo(updated);
        return insumoRepository.save(PersistenceSanitizer.sanitize(updated));
    }

    public void delete(String id) {
        insumoRepository.deleteById(id);
    }

    public void consumirInsumo(String id, int cantidad) {
        Insumo i = findById(id);
        if (i == null) throw new IllegalArgumentException("Insumo no encontrado");
        // usar las reglas del controlador
        controladorRecursos.consumirInsumo(i, cantidad);
        // persistir cambios
        insumoRepository.save(PersistenceSanitizer.sanitize(i));
    }
}


