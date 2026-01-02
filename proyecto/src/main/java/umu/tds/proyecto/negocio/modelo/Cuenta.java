package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Cuenta {
	
	private String nombre;
	private LocalDate fechaCreacion;
	protected List<Movimiento> movimientos;
	protected static final Categoria INGRESO = new Categoria("Ingreso");
    protected static final Categoria GASTO   = new Categoria("Gasto");
	protected double saldo;
	
	public Cuenta(String nombre,double saldo) {
		this.nombre = nombre;
		this.fechaCreacion = LocalDate.now();
		this.movimientos = new ArrayList<>();
		this.saldo=saldo;
	}
	
	public Cuenta(String nombre) {
		this.nombre = nombre;
		this.fechaCreacion = LocalDate.now();
		this.movimientos = new ArrayList<>();
		this.saldo=0;
	}
	
	public void ingresarDinero(double dinero) {
		 if (dinero <= 0) {
		        throw new IllegalArgumentException("El dinero a ingresar debe ser positivo");
		    }
		this.saldo+=dinero;
		Movimiento m= new Movimiento("Ingreso de " + dinero + "€", dinero, LocalDateTime.now(), INGRESO);
		movimientos.add(m);
	}
	
	public boolean sacarDinero(double dinero) {
		 if (dinero <= 0) {
		        throw new IllegalArgumentException("El dinero a sacar debe ser positivo");
		    }
		
		if(dinero>saldo) {
			return false;
			}
		
		else {
			saldo-=dinero;
			Movimiento m= new Movimiento("Retida de " + dinero + "€", dinero, LocalDateTime.now(), GASTO);
			movimientos.add(m);
			return true;
		}
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public abstract double getSaldoParaUsuario(String nombreUsuario);
}