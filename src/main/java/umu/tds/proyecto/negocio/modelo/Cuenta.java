package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Cuenta {
    
    private final String nombre; 
    private final LocalDate fechaCreacion; 
    protected List<Movimiento> movimientos;
    protected static final Categoria INGRESO = new Categoria("Ingreso");
    protected static final Categoria GASTO = new Categoria("Gasto");
    protected double saldo;
    
    public Cuenta(String nombre, double saldo) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no nulo").trim();
        if (this.nombre.isEmpty()) throw new IllegalArgumentException("Nombre vacío");
        this.fechaCreacion = LocalDate.now();
        this.movimientos = new ArrayList<>();
        this.saldo = saldo;
    }
    
    public Cuenta(String nombre) {
        this(nombre, 0.0);
    }
    
    public void ingresarDinero(double dinero) {
        if(dinero <= 0) throw new IllegalArgumentException("Dinero debe ser positivo");
        this.saldo += dinero;
        Movimiento ingreso = new Movimiento("Ingreso", dinero, LocalDateTime.now(), INGRESO);
        movimientos.add(ingreso);
    }
    
    public boolean sacarDinero(double dinero) {
        if(dinero <= 0) throw new IllegalArgumentException("Dinero debe ser positivo");
        this.saldo -= dinero;
        Movimiento retiro = new Movimiento("Retirada", dinero, LocalDateTime.now(), GASTO);
        movimientos.add(retiro);
        return true;
    }

    public void registrarMovimientoGasto(Movimiento m) {
        this.saldo -= m.getCantidad();
        this.movimientos.add(m);
    }

    // --- NUEVA LÓGICA: RECALCULAR TODO DESDE CERO ---

    /**
     * Recorre todos los movimientos y reconstruye el saldo real.
     * Se usa tras modificar o eliminar gastos.
     */
    public void recalcularSaldo() {
        this.saldo = 0.0; // Reiniciamos la hucha
        
        for (Movimiento m : this.movimientos) {
            // Si la categoría se llama "Ingreso" (sea el objeto del sistema o uno creado), suma
            if ("Ingreso".equalsIgnoreCase(m.getCategoria().getNombre())) {
                this.saldo += m.getCantidad();
            } else {
                // Cualquier otra cosa (Gasto, Comida, Ocio...) resta
                this.saldo -= m.getCantidad();
            }
        }
    }

    public void eliminarMovimiento(Movimiento m) {
        if (movimientos.remove(m)) {
            // Al borrar, simplemente recalculamos todo para evitar errores
            recalcularSaldo();
        }
    }

    // ------------------------------------------------

    public String getNombre() { return nombre; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public List<Movimiento> getMovimientos() { return Collections.unmodifiableList(movimientos); }
    public double getSaldo() { return saldo; }
    
    public abstract double getSaldoParaUsuario(String nombreUsuario);
    
    @Override
    public String toString() {
        return nombre;
    }

}