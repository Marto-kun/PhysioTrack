package main.java.repositories;

import main.java.modelo.EquipoBiomedico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoBiomedicoRepository extends MongoRepository<EquipoBiomedico, String> {
    EquipoBiomedico findByNombre(String nombre);
    List<EquipoBiomedico> findByEstado(String estado);
}

