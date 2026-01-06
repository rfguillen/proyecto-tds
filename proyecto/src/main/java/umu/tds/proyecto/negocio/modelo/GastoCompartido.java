package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class GastoCompartido extends Movimiento {
	
	// 1. Inmutabilidad: Declaramos el atributo como final para evitar cambios post-construcción
	private final Participante pagador;
	
	public GastoCompartido(String concepto, double cantidad, LocalDateTime fecha, Categoria categoria, Participante pagador) {
		super(concepto, cantidad, fecha, categoria);
		
		// 2. Validación de precondiciones: Robustez en el diseño
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad del gasto debe ser positiva");
		}
		
		// 3. Evitar nulos
		this.pagador = Objects.requireNonNull(pagador, "El pagador no puede ser nulo");
	}
	
	// 4. Eliminamos setPagador: Para garantizar que el objeto sea inmutable (Sesión 3)
	
	public Participante getPagador() {
		return pagador;
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

	// 6. Sobrescritura de toString (Sugerencia Sesión 2)
	@Override
	public String toString() {
		return  this.getClass().getName()+"[pagador=" + pagador.getNombre() + ", concepto=" + getConcepto() 
				+ ", cantidad=" + getCantidad() + ", fecha=" + getFecha() + "]";
	}
}