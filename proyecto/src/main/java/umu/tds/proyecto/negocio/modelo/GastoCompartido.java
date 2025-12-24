package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;

public class GastoCompartido extends Gasto {
	
	private Participante pagador;
	
	public GastoCompartido(String concepto, double cantidad, LocalDate fecha, Categoria categoria, Participante pagador) {
		super(concepto, cantidad, fecha, categoria);
		this.pagador = pagador;
	}
	
	public Participante getPagador() {
		return pagador;
	}
	
	public void setPagador(Participante pagador) {
		this.pagador = pagador;
	}
	
	@Override
	public double getCantidadPagadaPor(Participante p) {
		// Si el participante p es el que pagó, devolvemos la cantidad
		if (this.pagador.equals(p)) {
			return this.getCantidad();
		}
		return 0.0;
	}
}
