package main.java.repositories;

import main.java.modelo.Fisioterapeuta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio MongoDB para la entidad Fisioterapeuta.
 * Proporciona operaciones CRUD y consultas personalizadas para gestionar
 * los datos de fisioterapeutas en la base de datos.
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
@Repository
public interface FisioterapeutaRepository extends MongoRepository<Fisioterapeuta, String> {

    /**
     * Busca un fisioterapeuta por su número de cédula
     * @param cedula el número de cédula del fisioterapeuta
     * @return el Fisioterapeuta encontrado o null
     */
    Fisioterapeuta findByCedula(String cedula);

    /**
     * Busca todos los fisioterapeutas con una especialidad específica
     * @param especialidad la especialidad a buscar
     * @return lista de fisioterapeutas con esa especialidad
     */
    java.util.List<Fisioterapeuta> findByEspecialidad(String especialidad);
}

