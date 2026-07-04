package main.java.negocio;

import main.java.modelo.PlanRehabilitacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controlador especializado en la verificación y seguridad de las rutinas físicas.
 * Aplica las reglas de negocio críticas del sistema para asegurar que la intensidad
 * de un ejercicio no represente un riesgo para la escala de dolor y lesión del paciente.
 *
 * @author [Martin Vozmediano]
 * @version 1.0
 * @since 2026-05-19
 */
public class EvaluadorRutinas {

	/**
	 * Mapa que guarda, por cédula de paciente, la lista de planes asignados.
	 */
	private final Map<String, List<PlanRehabilitacion>> planesPorPaciente = new HashMap<>();

	/**
	 * Mapa auxiliar que lleva, por cédula de paciente, un mapa de idPlan -> diasCompletados.
	 * Mantener separado este contador permite no modificar el modelo {@link PlanRehabilitacion}.
	 */
	private final Map<String, Map<String, Integer>> diasCompletadosPorPaciente = new HashMap<>();

	/**
	 * Asigna un nuevo plan a un paciente aplicando las reglas de negocio.
	 *
	 * @param cedulaPaciente
	 * @param nuevoPlan
	 * @param nivelLesion
	 */
	public void asignarPlan(String cedulaPaciente, PlanRehabilitacion nuevoPlan, int nivelLesion) {
		if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
			throw new IllegalArgumentException("La cédula del paciente no puede ser nula o vacía.");
		}
		Objects.requireNonNull(nuevoPlan, "Error. El plan a asignar no puede ser nulo.");

		if (nivelLesion < 1 || nivelLesion > 10) {
			throw new IllegalArgumentException("El nivel de lesión debe estar entre 1 y 10.");
		}

		if (nivelLesion >= 7) {
			if (nuevoPlan.getSeries() > 3 || nuevoPlan.getRepeticiones() > 12) {
				throw new IllegalStateException("Para niveles de lesión >=7 no se permiten más de 3 series ni más de 12 repeticiones.");
			}
		}

		// Obtener o crear la lista de planes del paciente
		List<PlanRehabilitacion> lista = planesPorPaciente.computeIfAbsent(cedulaPaciente, k -> new ArrayList<>());
		lista.add(nuevoPlan);

		// Inicializar el contador de días completados para este plan
		String idPlan = nuevoPlan.getIdPlan();
		Map<String, Integer> mapaDias = diasCompletadosPorPaciente.computeIfAbsent(cedulaPaciente, k -> new HashMap<>());
		mapaDias.putIfAbsent(idPlan, 0);
	}

	/**
	 * Realiza el seguimiento semanal del paciente sumando los días asignados y los días completados
	 * de todos sus planes y devuelve un mensaje con el porcentaje alcanzado.
	 *
	 * @param cedulaPaciente
	 * @return texto formateado "[porcentaje]% de la meta alcanzada."; "0%..." si no tiene planes
	 */
	public String seguimientoSemanal(String cedulaPaciente) {
		if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
			throw new IllegalArgumentException("La cédula del paciente no puede ser nula o vacía.");
		}

		List<PlanRehabilitacion> lista = planesPorPaciente.get(cedulaPaciente);
		if (lista == null || lista.isEmpty()) {
			return "0% de la meta alcanzada.";
		}

		int totalDiasAsignados = 0;
		int totalDiasCompletados = 0;

		Map<String, Integer> mapaDias = diasCompletadosPorPaciente.getOrDefault(cedulaPaciente, Collections.emptyMap());

		for (PlanRehabilitacion p : lista) {
			int diasAsignados = Math.max(0, p.getDiasPorSemana());
			totalDiasAsignados += diasAsignados;

			String idPlan = p.getIdPlan();
			int completados = mapaDias.getOrDefault(idPlan, 0);
			totalDiasCompletados += Math.max(0, Math.min(completados, diasAsignados));
		}

		if (totalDiasAsignados == 0) {
			return "0% de la meta alcanzada.";
		}

		int porcentaje = (int) Math.round((double) totalDiasCompletados * 100.0 / totalDiasAsignados);
		return porcentaje + "% de la meta alcanzada.";
	}

	/**
	 * Registra la finalización de un día de ejercicio para un plan específico. No permite
	 * que el contador de días completados supere los días asignados para ese plan.
	 *
	 * @param cedulaPaciente
	 * @param idPlan
	 */
	public void registrarDiaCompletado(String cedulaPaciente, String idPlan) {
		if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
			throw new IllegalArgumentException("La cédula del paciente no puede ser nula o vacía.");
		}
		if (idPlan == null || idPlan.isEmpty()) {
			throw new IllegalArgumentException("El id del plan no puede ser nulo o vacío.");
		}

		List<PlanRehabilitacion> lista = planesPorPaciente.get(cedulaPaciente);
		if (lista == null || lista.isEmpty()) {
			throw new IllegalArgumentException("El paciente no tiene planes asignados.");
		}

		PlanRehabilitacion encontrado = null;
		for (PlanRehabilitacion p : lista) {
			if (idPlan.equals(p.getIdPlan())) {
				encontrado = p;
				break;
			}
		}

		if (encontrado == null) {
			throw new IllegalArgumentException("No se encontró un plan con el id proporcionado para el paciente.");
		}

		int diasAsignados = Math.max(0, encontrado.getDiasPorSemana());
		Map<String, Integer> mapaDias = diasCompletadosPorPaciente.computeIfAbsent(cedulaPaciente, k -> new HashMap<>());
		int actuales = mapaDias.getOrDefault(idPlan, 0);

		if (actuales >= diasAsignados) {
			throw new IllegalStateException("No es posible registrar más días completados que los días asignados para este plan.");
		}

		mapaDias.put(idPlan, actuales + 1);
	}

	/**
	 * Devuelve una copia no modificable de la lista de planes asignados a un paciente.
	 *
	 * @param cedulaPaciente
	 * @return lista inmutable de planes; lista vacía si no existen planes
	 */
	public List<PlanRehabilitacion> getPlanesPorPaciente(String cedulaPaciente) {
		if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
			throw new IllegalArgumentException("La cédula del paciente no puede ser nula o vacía.");
		}

		List<PlanRehabilitacion> lista = planesPorPaciente.get(cedulaPaciente);
		if (lista == null) {
			return Collections.emptyList();
		}
		return lista;
	}

}
