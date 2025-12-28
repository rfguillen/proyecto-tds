package umu.tds.proyecto.negocio.controladores;

import java.time.LocalDateTime;

import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.Participante;

public class GestionGastos {

	
	/* 	Metodos de sacar e ingresar dinero de cuenta compartida, hay tambien en la clase cuenta, si lo ves mejor borralos de sus clases y dejalos aqui
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
		
	}*/
}
