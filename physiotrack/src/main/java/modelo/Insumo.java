package main.java.modelo;

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
    private String fechaElaboracion;
    private String fechaCaducidad;
    private int stock;
    private int stockMinimo;

    public Insumo(String id, String nombre,
                  String fechaElaboracion,
                  String fechaCaducidad,
                  int stock,
                  int stockMinimo) {

        this.id = id;
        this.nombre = nombre;
        this.fechaElaboracion = fechaElaboracion;
        this.fechaCaducidad = fechaCaducidad;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaElaboracion() {
        return fechaElaboracion;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    // Agregar unidades al inventario
    public void agregarStock(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        this.stock += cantidad;
    }

    // Descontar unidades del inventario
    public void utilizarInsumo(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        if (cantidad > this.stock) {
            throw new IllegalArgumentException("Stock insuficiente.");
        }

        this.stock -= cantidad;
    }

    // Verificar si el stock está por debajo del mínimo
    public boolean stockBajo() {
        return stock <= stockMinimo;
    }

    // Verificar si el insumo tiene fecha de caducidad
    public boolean tieneCaducidad() {
        return fechaCaducidad != null && !fechaCaducidad.trim().isEmpty();
    }

    // Verificar si el insumo tiene fecha de elaboración
    public boolean tieneFechaElaboracion() {
        return fechaElaboracion != null && !fechaElaboracion.trim().isEmpty();
    }
}

