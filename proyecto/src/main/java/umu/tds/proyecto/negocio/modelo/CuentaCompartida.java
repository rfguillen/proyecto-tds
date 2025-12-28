package umu.tds.proyecto.negocio.modelo;


import java.time.LocalDateTime;
import java.util.List;

public class CuentaCompartida extends Cuenta {
	
	private List<Participante> participantes;
	
	public CuentaCompartida(String nombre, List<Participante> participantes) {
		super(nombre);
		this.participantes = participantes;
	}
	
	public CuentaCompartida(String nombre,double saldo, List<Participante> participantes) {
		super(nombre,saldo);
		this.participantes = participantes;
	}
	
	public List<Participante> getParticipantes() {
		return participantes;
	}
	
	// Calcular y actualizar los saldos de cada participante
	public void calcularSaldos() {
		// Calculamos el total gastado del grupo
		double totalGrupo = 0;
		for (Movimiento m : movimientos) {
			totalGrupo += m.getCantidad();
		}
		
		if (totalGrupo == 0) {
			participantes.forEach(p -> p.setSaldo(0.0));

		}
		
		for (Participante p : participantes) {
			// Calcular lo que ha pagado el participante p
			double pagado = 0.0;
			for (Movimiento m : movimientos) {
				pagado += m.getCantidadPagadaPor(p);
			}
			
			// Calcular lo que debería haber pagado según su porcentaje
			double debePagar = totalGrupo * (p.getPorcentajeParticipacion() / 100.0);
			
			// Actualizar el saldo
			p.setSaldo(pagado - debePagar);}
		}
		

	public void ingresarDinero(double dinero, Participante participante) {
	    // Validaciones
	    if (dinero <= 0) {
	        throw new IllegalArgumentException("El dinero a ingresar debe ser positivo");
	    }
	    if (participante == null) {
	        throw new IllegalArgumentException("El participante no puede ser nulo");
	    }
	    
	    // Actualizar saldo total de la cuenta
	    this.saldo += dinero;
	    
	    // Crear y registrar el movimiento
	    Movimiento m = new Movimiento("Ingreso", dinero, LocalDateTime.now(),INGRESO);
	    movimientos.add(m);
	    
	    
	    double dineroActual = participante.getSaldo();
	    participante.setSaldo(dineroActual + dinero);
	}
	
	
	
	
	public boolean sacarDinero(double dinero,Participante participante) {
		
		if(dinero>saldo && dinero>0) {
			throw new IllegalArgumentException("El dinero a sacar debe ser positivo y mayor que el saldo");
			}
		
		if (participante == null) {
		    throw new IllegalArgumentException("El participante no puede ser nulo");
		    }
		    
		
		else {
			 double dineroActual = participante.getSaldo();
			    participante.setSaldo(dineroActual + dinero);
			Movimiento m= new Movimiento("Retirar", dinero, LocalDateTime.now(),GASTO);
			movimientos.add(m);
			return true;
		}
		
	}
}
