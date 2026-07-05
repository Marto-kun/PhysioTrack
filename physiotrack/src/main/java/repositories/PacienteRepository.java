package main.java.repositories;

import main.java.modelo.Paciente;
import main.java.modelo.Fisioterapeuta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio MongoDB para la entidad Paciente.
 * Proporciona operaciones CRUD y consultas personalizadas para gestionar
 * los datos de pacientes en la base de datos.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String> {

    /**
     * Busca un paciente por su número de cédula
     * @param cedula el número de cédula del paciente
     * @return el Paciente encontrado o null
     */
    Paciente findByCedula(String cedula);

    /**
     * Busca todos los pacientes asignados a un fisioterapeuta específico
     * @param fisioterapeuta el fisioterapeuta cuyo lista de pacientes se desea obtener
     * @return lista de pacientes asignados al fisioterapeuta
     */
    java.util.List<Paciente> findByFisioterapeutaAsignado(Fisioterapeuta fisioterapeuta);

    /**
     * Busca todos los pacientes que requieren atención urgente
     * @return lista de pacientes con prioridad Urgente
     */
    java.util.List<Paciente> findByPrioridad(String prioridad);
}

