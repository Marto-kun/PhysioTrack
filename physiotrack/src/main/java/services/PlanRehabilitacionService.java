package main.java.services;

import main.java.modelo.Paciente;
import main.java.modelo.PlanRehabilitacion;
import main.java.negocio.EvaluadorRutinas;
import main.java.repositories.PlanRehabilitacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanRehabilitacionService {

    private final PlanRehabilitacionRepository planRepository;
    private final EvaluadorRutinas evaluador = new EvaluadorRutinas();

    public PlanRehabilitacionService(PlanRehabilitacionRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<PlanRehabilitacion> findByPacienteId(String id) {
        return planRepository.findByPaciente_Id(id);
    }

    public PlanRehabilitacion asignarPlan(Paciente paciente, PlanRehabilitacion plan, int nivelLesion) {
        if (paciente == null || paciente.getCedula() == null) throw new IllegalArgumentException("Paciente inválido");
        if (plan == null) throw new IllegalArgumentException("Plan inválido");

        // Delegar reglas críticas al evaluador
        evaluador.asignarPlan(paciente.getCedula(), plan, nivelLesion);

        // Persistir
        PlanRehabilitacion sanitized = PersistenceSanitizer.sanitize(plan);
        return planRepository.save(sanitized);
    }

    public void registrarDiaCompletado(String cedulaPaciente, String idPlan) {
        evaluador.registrarDiaCompletado(cedulaPaciente, idPlan);
    }
}

