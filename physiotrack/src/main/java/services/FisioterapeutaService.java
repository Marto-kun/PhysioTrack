package main.java.services;

import main.java.modelo.Fisioterapeuta;
import main.java.modelo.Paciente;
import main.java.negocio.GestorPacientes;
import main.java.repositories.FisioterapeutaRepository;
import main.java.repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con Fisioterapeutas.
 * Utiliza el repositorio MongoDB para persistencia de datos.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
@Service
public class FisioterapeutaService {

    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final PacienteRepository pacienteRepository;
    private final GestorPacientes gestorPacientes = new GestorPacientes();

    public FisioterapeutaService(FisioterapeutaRepository fisioterapeutaRepository, PacienteRepository pacienteRepository) {
        this.fisioterapeutaRepository = fisioterapeutaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Registra un nuevo fisioterapeuta en la base de datos
     * @param fisioterapeuta el fisioterapeuta a registrar
     * @return el fisioterapeuta guardado
     */
    public Fisioterapeuta registrarFisioterapeuta(Fisioterapeuta fisioterapeuta) {
        if (fisioterapeuta == null || fisioterapeuta.getCedula() == null) {
            throw new IllegalArgumentException("El fisioterapeuta y su cédula no pueden ser nulos");
        }

        Fisioterapeuta existente = fisioterapeutaRepository.findByCedula(fisioterapeuta.getCedula());
        if (existente != null) {
            throw new IllegalStateException("Ya existe un fisioterapeuta con la cédula: " + fisioterapeuta.getCedula());
        }

        return fisioterapeutaRepository.save(fisioterapeuta);
    }

    /**
     * Obtiene un fisioterapeuta por su ID
     * @param id el ID del fisioterapeuta
     * @return un Optional que contiene el fisioterapeuta si existe
     */
    public Optional<Fisioterapeuta> obtenerFisioterapeutaPorId(String id) {
        return fisioterapeutaRepository.findById(id);
    }

    /**
     * Obtiene un fisioterapeuta por su cédula
     * @param cedula la cédula del fisioterapeuta
     * @return el Fisioterapeuta encontrado o null
     */
    public Fisioterapeuta obtenerFisioterapeutaPorCedula(String cedula) {
        return fisioterapeutaRepository.findByCedula(cedula);
    }

    /**
     * Obtiene todos los fisioterapeutas registrados
     * @return lista de todos los fisioterapeutas
     */
    public List<Fisioterapeuta> obtenerTodosFisioterapeutas() {
        return fisioterapeutaRepository.findAll();
    }

    /**
     * Obtiene todos los fisioterapeutas con una especialidad específica
     * @param especialidad la especialidad a buscar
     * @return lista de fisioterapeutas con esa especialidad
     */
    public List<Fisioterapeuta> obtenerFisioterapeutasPorEspecialidad(String especialidad) {
        return fisioterapeutaRepository.findByEspecialidad(especialidad);
    }

    /**
     * Actualiza los datos de un fisioterapeuta
     * @param fisioterapeuta el fisioterapeuta con datos actualizados
     * @return el fisioterapeuta actualizado
     */
    public Fisioterapeuta actualizarFisioterapeuta(Fisioterapeuta fisioterapeuta) {
        if (fisioterapeuta == null || fisioterapeuta.getId() == null) {
            throw new IllegalArgumentException("El fisioterapeuta y su ID no pueden ser nulos");
        }

        Fisioterapeuta existente = fisioterapeutaRepository.findById(fisioterapeuta.getId()).orElseThrow(() ->
                new IllegalStateException("No existe un fisioterapeuta con el ID: " + fisioterapeuta.getId())
        );

        existente.setCedula(fisioterapeuta.getCedula());
        existente.setNombre(fisioterapeuta.getNombre());
        existente.setEspecialidad(fisioterapeuta.getEspecialidad());

        return fisioterapeutaRepository.save(existente);
    }

    /**
     * Elimina un fisioterapeuta por su ID
     * @param id el ID del fisioterapeuta a eliminar
     */
    public void eliminarFisioterapeuta(String id) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("No existe un fisioterapeuta con el ID: " + id)
        );

        // Recuperar pacientes asignados y aplicar lógica de negocio para darlos de alta
        List<Paciente> pacientesAsignados = pacienteRepository.findByFisioterapeutaAsignado(fisioterapeuta);
        for (Paciente p : pacientesAsignados) {
            gestorPacientes.darAltaMedica(p); // quita asignación en memoria
            // persistir cambios en paciente
            pacienteRepository.save(p);
        }

        fisioterapeutaRepository.deleteById(id);
    }
}

