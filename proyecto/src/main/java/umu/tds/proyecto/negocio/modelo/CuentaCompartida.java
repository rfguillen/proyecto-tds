package umu.tds.proyecto.negocio.modelo;

import java.util.List;

public class CuentaCompartida extends Cuenta {
	
	private List<Participante> participantes;
	
	public CuentaCompartida(String nombre, List<Participante> participantes) {
		super(nombre);
		this.participantes = participantes;
	}
	
	public List<Participante> getParticipantes() {
		return participantes;
	}
	
	// Calcular y actualizar los saldos de cada participante
	public void calcularSaldos() {
		// Calculamos el total gastado del grupo
		double totalGrupo = 0;
		for (Gasto g : gastos) {
			totalGrupo += g.getCantidad();
		}
		
		if (totalGrupo == 0) {
			participantes.forEach(p -> p.setSaldo(0.0));
			return;
		}
		
		for (Participante p : participantes) {
			// Calcular lo que ha pagado el participante p
			double pagado = 0.0;
			for (Gasto g : gastos) {
				pagado += g.getCantidadPagadaPor(p);
			}
			
			// Calcular lo que debería haber pagado según su porcentaje
			double debePagar = totalGrupo * (p.getPorcentajeParticipacion() / 100.0);
			
			// Actualizar el saldo
			p.setSaldo(pagado - debePagar);
		}
	}

}
