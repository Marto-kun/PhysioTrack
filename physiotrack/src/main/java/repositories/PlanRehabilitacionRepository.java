package main.java.repositories;

import main.java.modelo.Paciente;
import main.java.modelo.PlanRehabilitacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRehabilitacionRepository extends MongoRepository<PlanRehabilitacion, String> {
    List<PlanRehabilitacion> findByPaciente_Id(String id);
    List<PlanRehabilitacion> findByPaciente_Cedula(String cedula);
    List<PlanRehabilitacion> findByPaciente(Paciente paciente);
}

