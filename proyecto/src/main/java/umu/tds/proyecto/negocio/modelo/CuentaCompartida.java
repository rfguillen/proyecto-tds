package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cuenta compartida entre varios participantes
 * 
 * Patrón Experto (GRASP): esta clase conoce sus participantes y movimientos
 * por lo que es la responsable de calcular saldos pendientes
 */
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
	    GastoCompartido gasto = new GastoCompartido(
	            concepto,
	            cantidad,
	            LocalDateTime.now(),
	            GASTO,
	            pagador
	    );

	    registrarMovimientoGasto(gasto);
	}

	@Override
	public String toString() {
		return super.toString()+"[participantes=" + participantes + "]";
	}
	
	public double getImportePagadoPor(Participante p) {
	    Participante participanteReal = buscarParticipante(p);

	    return movimientos.stream()
	            .filter(m -> m instanceof GastoCompartido)
	            .map(m -> (GastoCompartido) m)
	            .filter(g -> g.getPagador().equals(participanteReal))
	            .mapToDouble(Movimiento::getCantidad)
	            .sum();
	}

	public double getImporteAsumidoPor(Participante p) {
	    Participante participanteReal = buscarParticipante(p);
	    double porcentaje = participanteReal.getPorcentajeParticipacion() / 100.0;

	    return movimientos.stream()
	            .filter(m -> m instanceof GastoCompartido)
	            .mapToDouble(m -> m.getCantidad() * porcentaje)
	            .sum();
	}

	public double getSaldoParticipante(Participante p) {
	    return getImportePagadoPor(p) - getImporteAsumidoPor(p);
	}

	public Map<Participante, Double> getSaldosParticipantes() {
	    Map<Participante, Double> saldos = new LinkedHashMap<>();
	    for (Participante p : participantes) {
	        saldos.put(p, getSaldoParticipante(p));
	    }
	    return saldos;
	}
	
	@Override
	public void registrarMovimientoGasto(Movimiento m) {
	    Objects.requireNonNull(m, "El movimiento no puede ser nulo");

	    if (!(m instanceof GastoCompartido gasto)) {
	        throw new IllegalArgumentException(
	            "Una cuenta compartida solo puede registrar gastos compartidos con pagador"
	        );
	    }

	    Participante pagadorReal = buscarParticipante(gasto.getPagador());
	    gasto.setPagador(pagadorReal);

	    movimientos.add(gasto);
	}
	
	private Participante buscarParticipante(Participante participante) {
	    Objects.requireNonNull(participante, "El participante no puede ser nulo");

	    return participantes.stream()
	            .filter(p -> p.equals(participante))
	            .findFirst()
	            .orElseThrow(() -> new IllegalArgumentException(
	                    "El participante no pertenece a esta cuenta compartida"
	            ));
	}
	
	}

