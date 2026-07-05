package main.java.services;

import main.java.modelo.Paciente;
import main.java.modelo.Fisioterapeuta;
import main.java.negocio.GestorPacientes;
import main.java.repositories.PacienteRepository;
import main.java.repositories.FisioterapeutaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con Pacientes.
 * Utiliza el repositorio MongoDB para persistencia de datos.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final GestorPacientes gestorPacientes = new GestorPacientes();

    public PacienteService(PacienteRepository pacienteRepository, FisioterapeutaRepository fisioterapeutaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.fisioterapeutaRepository = fisioterapeutaRepository;
    }

    /**
     * Registra un nuevo paciente en la base de datos y asigna un fisioterapeuta
     * de forma bidireccional y sincrónica en MongoDB Atlas.
     *
     * @param paciente      el paciente a registrar
     * @param fisioterapeuta el fisioterapeuta seleccionado para la asignación
     * @return el paciente guardado
     */
    public Paciente registrarPaciente(Paciente paciente, Fisioterapeuta fisioterapeuta) {
        // Validaciones básicas
        if (paciente == null || paciente.getCedula() == null) {
            throw new IllegalArgumentException("El paciente y su cédula no pueden ser nulos");
        }
        if (fisioterapeuta == null) {
            throw new IllegalArgumentException("El fisioterapeuta no puede ser nulo");
        }

        // Verificar que no exista ya un paciente con la misma cédula
        Paciente existente = pacienteRepository.findByCedula(paciente.getCedula());
        if (existente != null) {
            throw new IllegalStateException("Ya existe un paciente con la cédula: " + paciente.getCedula());
        }

        // Asegurar la instancia persistente del fisioterapeuta
        Fisioterapeuta fisioPersist = null;
        if (fisioterapeuta.getId() != null) {
            fisioPersist = fisioterapeutaRepository.findById(fisioterapeuta.getId()).orElse(null);
        }
        if (fisioPersist == null && fisioterapeuta.getCedula() != null) {
            fisioPersist = fisioterapeutaRepository.findByCedula(fisioterapeuta.getCedula());
        }
        if (fisioPersist == null) {
            throw new IllegalArgumentException("Fisioterapeuta no encontrado en la base de datos");
        }

        gestorPacientes.registrarAsignacion(paciente, fisioPersist);

        Fisioterapeuta fisioEmbebido = new Fisioterapeuta();
        fisioEmbebido.setId(fisioPersist.getId());
        fisioEmbebido.setCedula(fisioPersist.getCedula());
        fisioEmbebido.setNombre(fisioPersist.getNombre());
        fisioEmbebido.setEspecialidad(fisioPersist.getEspecialidad());

        paciente.setFisioterapeutaAsignado(fisioEmbebido);

        // Persistencia secuencial estricta para asegurar sincronización en MongoDB Atlas
        Paciente pacienteGuardado = pacienteRepository.save(paciente);



        // Tras guardar el paciente (y generar su ID), persistir el fisioterapeuta
        // para que su lista interna 'pacientesAsignados' se actualice en Atlas
        fisioterapeutaRepository.save(fisioPersist);

        return pacienteGuardado;
    }

    /**
     * Obtiene un paciente por su ID
     *
     * @param id el ID del paciente
     * @return un Optional que contiene el paciente si existe
     */
    public Optional<Paciente> obtenerPacientePorId(String id) {
        return pacienteRepository.findById(id);
    }

    /**
     * Obtiene un paciente por su cédula
     *
     * @param cedula la cédula del paciente
     * @return el Paciente encontrado o null
     */
    public Paciente obtenerPacientePorCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula);
    }

    /**
     * Obtiene todos los pacientes registrados
     *
     * @return lista de todos los pacientes
     */
    public List<Paciente> obtenerTodosPacientes() {
        return pacienteRepository.findAll();
    }

    /**
     * Obtiene todos los pacientes asignados a un fisioterapeuta específico
     *
     * @param fisioterapeuta el fisioterapeuta cuyo lista de pacientes se desea obtener
     * @return lista de pacientes asignados al fisioterapeuta
     */
    public List<Paciente> obtenerPacientesPorFisioterapeuta(Fisioterapeuta fisioterapeuta) {
        if (fisioterapeuta == null) {
            throw new IllegalArgumentException("El fisioterapeuta no puede ser nulo");
        }

        // Resolver entidad persistente
        Fisioterapeuta fisio = null;
        if (fisioterapeuta.getId() != null) {
            fisio = fisioterapeutaRepository.findById(fisioterapeuta.getId()).orElse(null);
        }
        if (fisio == null && fisioterapeuta.getCedula() != null) {
            fisio = fisioterapeutaRepository.findByCedula(fisioterapeuta.getCedula());
        }
        if (fisio == null) {
            throw new IllegalArgumentException("Fisioterapeuta no encontrado en la base de datos");
        }

        return pacienteRepository.findByFisioterapeutaAsignado_Id(fisio.getId());
    }

    /**
     * Obtiene todos los pacientes con una prioridad específica
     *
     * @param prioridad la prioridad a buscar (Urgente, Seguimiento, Preventivo)
     * @return lista de pacientes con esa prioridad
     */
    public List<Paciente> obtenerPacientesPorPrioridad(String prioridad) {
        return pacienteRepository.findByPrioridad(prioridad);
    }

    /**
     * Asigna un paciente a un fisioterapeuta
     *
     * @param pacienteId     el ID del paciente
     * @param fisioterapeuta el fisioterapeuta a asignar
     * @return el paciente actualizado
     */
    public Paciente asignarPacienteAFisioterapeuta(String pacienteId, Fisioterapeuta fisioterapeuta) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(pacienteId);
        if (pacienteOpt.isEmpty()) {
            throw new IllegalStateException("No existe un paciente con el ID: " + pacienteId);
        }

        if (fisioterapeuta == null) {
            throw new IllegalArgumentException("El fisioterapeuta no puede ser nulo");
        }

        // Cargar fisioterapeuta persistente
        Fisioterapeuta fisioPersist = null;
        if (fisioterapeuta.getId() != null) {
            fisioPersist = fisioterapeutaRepository.findById(fisioterapeuta.getId()).orElse(null);
        }
        if (fisioPersist == null && fisioterapeuta.getCedula() != null) {
            fisioPersist = fisioterapeutaRepository.findByCedula(fisioterapeuta.getCedula());
        }
        if (fisioPersist == null) {
            throw new IllegalArgumentException("Fisioterapeuta no encontrado para asignar");
        }

        Paciente paciente = pacienteOpt.get();

        // Asegurar que el objeto paciente también conozca a su nuevo fisioterapeuta
        paciente.setFisioterapeutaAsignado(fisioPersist);

        // Delegar validaciones y asignación a la lógica del negocio
        gestorPacientes.registrarAsignacion(paciente, fisioPersist);

        // Delegar validaciones y asignación a la lógica del negocio
        gestorPacientes.registrarAsignacion(paciente, fisioPersist);

        // Persistir ambos objetos actualizados
        pacienteRepository.save(paciente);
        fisioterapeutaRepository.save(fisioPersist);

        return paciente;
    }

    /**
     * Actualiza los datos de un paciente
     *
     * @param paciente el paciente con datos actualizados
     * @return el paciente actualizado
     */
    public Paciente actualizarPaciente(Paciente paciente) {
        if (paciente == null || paciente.getId() == null) {
            throw new IllegalArgumentException("El paciente y su ID no pueden ser nulos");
        }

        Paciente existente = pacienteRepository.findById(paciente.getId()).orElseThrow(() ->
                new IllegalStateException("No existe un paciente con el ID: " + paciente.getId())
        );

        existente.setNombre(paciente.getNombre());
        existente.setEdad(paciente.getEdad());
        existente.setTipoLesion(paciente.getTipoLesion());
        existente.setNivelLesion(paciente.getNivelLesion());

        existente.setFisioterapeutaAsignado(paciente.getFisioterapeutaAsignado());

        return pacienteRepository.save(existente);
    }

    /**
     * Elimina un paciente por su ID
     *
     * @param id el ID del paciente a eliminar
     */
    public void eliminarPaciente(String id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("No existe un paciente con el ID: " + id)
        );

        // Aplicar lógica de negocio para dar de alta si procede
        gestorPacientes.darAltaMedica(paciente);

        // Persistir cambios en fisioterapeuta si fue modificado
        if (paciente.getFisioterapeutaAsignado() != null) {
            fisioterapeutaRepository.save(paciente.getFisioterapeutaAsignado());
        }

        pacienteRepository.deleteById(id);
    }
}

