public class Paciente {

    private String nombre;
    private int edad;
    private int tipoLesion;
    private String prioridad;

    public Paciente(String nombre, int edad, int tipoLesion) {
        this.nombre = nombre;
        this.edad = edad;
        this.tipoLesion = tipoLesion;
        calcularPrioridad();
    }

    private void calcularPrioridad() {

        if (tipoLesion >= 8 || edad >= 65) {
            prioridad = "Urgente";
        } else if (tipoLesion >= 5) {
            prioridad = "Seguimiento";
        } else {
            prioridad = "Preventivo";
        }
    }

    public void mostrarPaciente() {
        System.out.println("\n===== PACIENTE =====");
        System.out.println("Nombre: " + nombre);
        System.out.println("Edad: " + edad);
        System.out.println("Nivel Lesión: " + tipoLesion);
        System.out.println("Prioridad: " + prioridad);
    }

    public String getNombre() {
        return nombre;
    }
}
