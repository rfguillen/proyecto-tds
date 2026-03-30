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
    protected static final Categoria GASTO = new Categoria("Gasto");
    
    public Cuenta(String nombre, double saldo) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no nulo").trim();
        if (this.nombre.isEmpty()) throw new IllegalArgumentException("Nombre vacío");
        this.fechaCreacion = LocalDate.now();
        this.movimientos = new ArrayList<>();
    }
    
    public Cuenta(String nombre) {
        this(nombre, 0.0);
    }
    
    public void registrarMovimientoGasto(Movimiento m) {
        this.movimientos.add(m);
    }

    // --- NUEVA LÓGICA: RECALCULAR TODO DESDE CERO ---

    public void eliminarMovimiento(Movimiento m) {
    	movimientos.remove(m);
    }

    // ------------------------------------------------

    public String getNombre() { return nombre; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public List<Movimiento> getMovimientos() { return Collections.unmodifiableList(movimientos); }
    
    @Override
    public String toString() {
        return nombre;
    }

}