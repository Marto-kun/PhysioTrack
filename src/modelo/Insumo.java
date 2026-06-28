package modelo;

/**
 * Representa los materiales consumibles y desechables de la clínica (ej. electrodos, geles).
 * Controla las existencias físicas en el inventario y dispara alertas automáticas
 * cuando el stock actual disminuye por debajo del límite mínimo permitido.
 *
 * @author [Roberto Cordero]
 * @version 1.0
 * @since 2026-05-19
 */

public class Insumo {

    private String id;
    private String nombre;
    private String fechaCaducidad;
    private int stock;
    private int stockMinimo;

    public Insumo(String id, String nombre,
                  String fechaCaducidad,
                  int stock,
                  int stockMinimo) {

        this.id = id;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void utilizar(int cantidad) {

        if (cantidad > stock) {
            System.out.println("No hay suficiente stock.");
            return;
        }

        stock -= cantidad;

        System.out.println("Se utilizaron "
                + cantidad + " unidades.");

        System.out.println("Stock restante: "
                + stock);

        if (stock <= stockMinimo) {
            System.out.println("⚠ ALERTA: Stock bajo.");
        }
    }

    public void mostrarInsumo() {

        System.out.println("\n===== INSUMO =====");
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);

        if (fechaCaducidad != null) {
            System.out.println("Caducidad: "
                    + fechaCaducidad);
        } else {
            System.out.println("Caducidad: No aplica");
        }

        System.out.println("Stock: " + stock);
    }
}
