package main.java.repositories;

import main.java.modelo.SesionTratamiento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionTratamientoRepository extends MongoRepository<SesionTratamiento, String> {
    List<SesionTratamiento> findByPaciente_Id(String id);
    List<SesionTratamiento> findByFisioterapeutaResponsable_Id(String id);
}

