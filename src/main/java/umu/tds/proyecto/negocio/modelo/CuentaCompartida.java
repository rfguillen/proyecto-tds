package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CuentaCompartida extends Cuenta {
	
	private final List<Participante> participantes;
	private final static double CERO=0.0;
	
	/**
	 * Constructor principal: crea una cuenta compartida con participantes.
	 */
	public CuentaCompartida(String nombre, List<Participante> participantes) {
		this(nombre, CERO, participantes);
	}
	
	/**
	 * Constructor con saldo inicial.
	 */
	public CuentaCompartida(String nombre, double saldo, List<Participante> participantes) {
		super(nombre, saldo);
		
		// Validaciones (Sesión 3)
		Objects.requireNonNull(participantes, "La lista de participantes no puede ser nula");
		if (participantes.isEmpty()) {
			throw new IllegalArgumentException("Debe haber al menos un participante");
		}
		
		// Copia defensiva (Sesión 2): evitamos que alguien modifique la lista externa
		this.participantes = new ArrayList<>(participantes);
		
		// Validar que los porcentajes sumen 100%
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
	 * Calcula y actualiza los saldos de cada participante.
	 * Saldo positivo = le deben dinero | Saldo negativo = debe dinero.
	 */
	public void calcularSaldos() {
		// Calcular el total gastado del grupo
		double totalGrupo = movimientos.stream()
			.filter(m -> m.getCategoria().equals(GASTO)) // Solo gastos, no ingresos
			.mapToDouble(Movimiento::getCantidad)
			.sum();
		
		// Si no hay gastos, todos tienen saldo 0
		if (totalGrupo == 0) {
			participantes.forEach(p -> p.setSaldo(CERO));
			return;
		}
		
		// Calcular saldo de cada participante
		for (Participante p : participantes) {
			// Lo que ha pagado este participante
			double pagado = movimientos.stream()
				.mapToDouble(m -> m.getCantidadPagadaPor(p))
				.sum();
			
			// Lo que debería haber pagado según su porcentaje
			double debePagar = totalGrupo * (p.getPorcentajeParticipacion() / 100.0);
			
			// Saldo = pagado - lo que debía pagar
			// Si es positivo: le deben dinero
			// Si es negativo: debe dinero
			p.setSaldo(pagado - debePagar);
		}
	}
	
	@Override
	public double getSaldoParaUsuario(String nombreUsuario) {
		calcularSaldos();
		
		// Buscar al participante por nombre (ignorando mayúsculas)
		return participantes.stream()
			.filter(p -> p.getNombre().equalsIgnoreCase(nombreUsuario))
			.findFirst()
			.map(Participante::getSaldo)
			.orElse(0.0); // Si no está en el grupo, saldo = 0
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
		
		// Recalcular saldos
		calcularSaldos();
	}
	
	/**
	 * Ingresa dinero en la cuenta (aportación de un participante).
	 */
	public void ingresarDinero(double dinero, Participante participante) {
		// Validaciones
		if (dinero <= 0) {
			throw new IllegalArgumentException("El dinero a ingresar debe ser positivo");
		}
		Objects.requireNonNull(participante, "El participante no puede ser nulo");
		if (!participantes.contains(participante)) {
			throw new IllegalArgumentException("El participante no pertenece a esta cuenta");
		}
		
		// Actualizar saldo total de la cuenta
		this.saldo += dinero;
		
		// Registrar el movimiento como un ingreso pagado por ese participante
		GastoCompartido ingreso = new GastoCompartido(
			"Ingreso de " + participante.getNombre(), 
			dinero, 
			LocalDateTime.now(), 
			INGRESO, 
			participante
		);
		movimientos.add(ingreso);
		
		// Recalcular saldos
		calcularSaldos();
	}
	
	/**
	 * Retira dinero de la cuenta (gasto realizado por un participante).
	 */
	public boolean sacarDinero(double dinero, Participante participante) {
		// Validaciones
		if (dinero <= 0) {
			throw new IllegalArgumentException("El dinero a sacar debe ser positivo");
		}
		Objects.requireNonNull(participante, "El participante no puede ser nulo");
		if (!participantes.contains(participante)) {
			throw new IllegalArgumentException("El participante no pertenece a esta cuenta");
		}
		
		// Verificar si hay fondos suficientes
		if (dinero > saldo) {
			return false; // No hay fondos suficientes
		}
		
		// Actualizar el saldo
		this.saldo -= dinero;
		
		// Registrar el movimiento
		GastoCompartido retiro = new GastoCompartido(
			"Retirada de " + participante.getNombre(), 
			dinero, 
			LocalDateTime.now(), 
			GASTO, 
			participante
		);
		movimientos.add(retiro);
		
		// Recalcular saldos
		calcularSaldos();
		
		return true;
	}

	@Override
	public String toString() {
		return super.toString()+"[participantes=" + participantes + "]";
	}
	
	
	
	}

