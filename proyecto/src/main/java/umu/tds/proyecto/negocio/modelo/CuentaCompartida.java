package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CuentaCompartida extends Cuenta {
	
	private final List<Participante> participantes;
	
	/**
	 * Constructor principal: crea una cuenta compartida con participantes.
	 */
	public CuentaCompartida(String nombre, List<Participante> participantes) {
		super(nombre);
		
		Objects.requireNonNull(participantes, "La lista de participantes no puede ser nula");
		if (participantes.isEmpty()) {
			throw new IllegalArgumentException("Debe haber al menos un participante");
		}
		
		this.participantes = new ArrayList<>(participantes);
		// Comprobar que los porcentajes suman 100
		validarPorcentajes();
		
	}
	
	/**
	 * Valida que los porcentajes de participación sumen 100%.
	 */
	private void validarPorcentajes() {
		double sumaPorcentajes = participantes.stream()
			.mapToDouble(Participante::getPorcentajeParticipacion)
			.sum();
		
		// Tolerancia para errores de redondeo en punto flotante
		if (Math.abs(sumaPorcentajes - 100.0) > 0.01) {
			throw new IllegalArgumentException(
				String.format("Los porcentajes deben sumar 100%% (actual: %.2f%%)", sumaPorcentajes)
			);
		}
	}
	
	/**
	 * Devuelve una lista no modificable de participantes .
	 */
	public List<Participante> getParticipantes() {
		return Collections.unmodifiableList(participantes);
	}
	
	
	/**
	 * Registra un gasto compartido pagado por un participante.
	 */
	public void registrarGasto(String concepto, double cantidad, Participante pagador) {
		// Validaciones
		Objects.requireNonNull(concepto, "El concepto no puede ser nulo");
		Objects.requireNonNull(pagador, "El pagador no puede ser nulo");
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser positiva");
		}
		if (!participantes.contains(pagador)) {
			throw new IllegalArgumentException("El pagador no pertenece a esta cuenta");
		}
		
		// Crear y registrar el gasto
		GastoCompartido gasto = new GastoCompartido(
			concepto, 
			cantidad, 
			LocalDateTime.now(), 
			GASTO, 
			pagador
		);
		movimientos.add(gasto);
	}
	

	@Override
	public String toString() {
		return super.toString()+"[participantes=" + participantes + "]";
	}
	
	
	
	}

