package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class GastoCompartido extends Movimiento {
	
	private Participante pagador;
	
	public GastoCompartido(String concepto, double cantidad, LocalDateTime fecha, Categoria categoria, Participante pagador) {
		super(concepto, cantidad, fecha, categoria);
		
		//  Validación de precondiciones: Robustez en el diseño
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad del gasto debe ser positiva");
		}
		
		// Evitar nulos
		this.pagador = Objects.requireNonNull(pagador, "El pagador no puede ser nulo");
	}
	
	
	
	public Participante getPagador() {
		return pagador;
	}
	
	public void setPagador(Participante pagador) {
		this.pagador = pagador;
	}
	
	@Override
	public double getCantidadPagadaPor(Participante p) {
		if (p == null) return 0.0;
		
		// Comparación segura 
		if (this.pagador.equals(p)) {
			return this.getCantidad();
		}
		return 0.0;
	}

	// Sobrescritura de toString
	@Override
	public String toString() {
		return  this.getClass().getName()+"[pagador=" + pagador.getNombre() + ", concepto=" + getConcepto() 
				+ ", cantidad=" + getCantidad() + ", fecha=" + getFecha() + "]";
	}
}