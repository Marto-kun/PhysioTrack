package main.java.repositories;

import main.java.modelo.Insumo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends MongoRepository<Insumo, String> {
    Insumo findByNombre(String nombre);
}

