package negocio;

import modelo.Paciente;
import modelo.Fisioterapeuta;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de la lógica de negocio para la administración de pacientes.
 * Gestiona el registro, persistencia en colecciones y el proceso automatizado
 * de categorización y asignación de prioridades médicas según la gravedad de la lesión.
 *
 * @author [Martin Vozmediano]
 * @version 1.0
 * @since 2026-05-19
 */

public class GestorPacientes {

    private List<Paciente> listaPacientes = new ArrayList();

    public GestorPacientes(){
        //Constructor vacio
    }

    public void agregarPaciente(Paciente nuevo){

        if(nuevo == null){
            throw new IllegalArgumentException("Error: El Paciente no puede ser nulo");
        }
        if(!listaPacientes.contains(nuevo)){
            listaPacientes.add(nuevo);
        }else{
            System.out.println("El paciente ya ha sido registrado. Intente con uno nuevo.");
        }

    }




}
