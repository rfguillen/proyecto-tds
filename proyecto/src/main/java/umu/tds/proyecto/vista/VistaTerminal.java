package umu.tds.proyecto.vista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import umu.tds.proyecto.negocio.controladores.GestionGastos;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.Participante;
import umu.tds.proyecto.negocio.modelo.CuentaCompartida;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Movimiento;

public class VistaTerminal {

	private static final DateTimeFormatter FECHA_CORRECTA = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

	private final GestionGastos gestor;
	private final Cuenta cuentaActual;
	private final Scanner scanner;

	public VistaTerminal() {
		this(GestionGastos.getInstancia(), GestionGastos.getInstancia().getCuentaGlobal());
	}

	public VistaTerminal(GestionGastos gestor, Cuenta cuentaActual) {
		this.gestor = gestor;
		this.cuentaActual = cuentaActual;
		this.scanner = new Scanner(System.in);
	}

	// Codigo principal
	public void codigo() {
		// Imprime ayuda inicial
		imprimirHelp();
		System.out.println("Gestionando cuenta: " + cuentaActual.getNombre());

		while (true) {
			// Leer entradas
			System.out.print("[" + cuentaActual.getNombre() + "]: ");
			String linea = scanner.nextLine().trim();

			if (linea.isBlank()) {
				continue;
			}
			// Dividir entradas en orden y opciones p.j: "modificar" y "-h"
			String[] partes = linea.split("\\s+");
			String opciones = null;
			String orden = partes[0];
			if (partes.length > 1) {
				opciones = partes[1];
			}
			// Llamar a la funcion segun cada caso
			try {
				switch (orden) {
				case "help": {
					imprimirHelp();
					break;
				}
				case "registrar": {
					if (opciones == null) {
						registrar();
					} else if (opciones.equals("-h")) {
						registrarHelp(opciones);
					} else {
						System.out.println("Opcion invalida. Solo posible 'registrar' y 'registrar -h'");
					}
					break;
				}
				case "modificar": {
					if (opciones == null) {
						modificar();
					} else if (opciones.equals("-h")) {
						modificarHelp(opciones);
					} else {
						System.out.println("Opcion invalida. Solo posible 'modificar' y 'modificar -h'");
					}
					break;
				}
				case "eliminar": {
					if (opciones == null) {
						eliminar();
					} else if (opciones.contains("-h")) {
						eliminarHelp(opciones);
					} else {
						System.out.println("Opcion invalida. Solo posible 'eliminar' y 'eliminar -h'");
					}

					break;
				}
				case "exit": {
					return;
				}
				default:
					System.out.println("Orden erronea. Use 'help' para ver ordenes permitidas.");

				}

			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
	}

	// FUNCION PARA REGISTRAR GASTOS/MOVIMIENTOS
	public void registrar() {
		String concepto;
		double cantidad;
		LocalDateTime fecha;
		Categoria categoria;
		Participante pagador;

		Movimiento gasto;

		try {
			// Concepto
			while (true) {
				System.out.print("> Concepto: ");
				concepto = scanner.nextLine();

				if (concepto.equalsIgnoreCase("cancelar")) {
					System.out.println("Operación cancelada.");
					return;
				}
				if (concepto.isEmpty()) {
					System.out.println("Error: Debes escribir un concepto");

				} else
					break;
			}

			// Cantidad
			while (true) {
				System.out.print("> Cantidad: ");
				String cant = scanner.nextLine();

				if (cant.equals("cancelar")) {
					System.out.println("Operación cancelada.");
					return;
				}

				try {
					cantidad = Double.parseDouble(cant);
					if (cantidad <= 0) {
						System.out.println("Error: Cantidad ha de ser mayor que 0");
					} else {
						break;
					}
				} catch (NumberFormatException e) {
					System.out.println("Error: Formato de cantidad inválido");
				}
			}

			// Fecha
			while (true) {
				System.out.print("> Fecha: ");
				String fecha1 = scanner.nextLine();

				if (fecha1.equals("cancelar")) {
					System.out.println("Operación cancelada.");
					return;

				} else if (fecha1.equals("ahora") || fecha1.isEmpty()) {
					fecha = LocalDateTime.now();
					break;
				}
				try {
					fecha = LocalDateTime.parse(fecha1, FECHA_CORRECTA);
					break;
				} catch (DateTimeParseException e) {
					System.out.println("Error: Formato inválido. Use M/d/yyyy H:mm (ejemplo: 1/15/2026 14:30)");
				}
			}

			// Categoria
			while (true) {
				System.out.print("> Categoría: ");
				String categoriaNombre = scanner.nextLine();

				if (categoriaNombre.isEmpty()) {
					System.out.println("Error: La categoría no puede estar vacía.");

				} else if (categoriaNombre.equals("cancelar")) {
					System.out.println("Operación cancelada.");
					return;

				} else {
					categoria = new Categoria(categoriaNombre);
					break;
				}
			}

			// Si la cuenta es una cuentaCompartida
			if (cuentaActual instanceof CuentaCompartida cuentaComp) {

				System.out.println("Participantes disponibles:");
				List<Participante> participantes = cuentaComp.getParticipantes();
				// Imprimir los participantes disponibles
				for (int i = 0; i < participantes.size(); i++) {
					System.out.println("  " + (i + 1) + ". " + participantes.get(i).getNombre());
				}

				int indice;

				while (true) {
					System.out.print("> Seleccione pagador : ");
					String indiPagador = scanner.nextLine();

					if (indiPagador.equals("cancelar")) {
						System.out.println("Operación cancelada.");
						return;
					} else if (indiPagador.isEmpty()) {
						System.out.println("Error: Debe seleccionar un pagador.");
					} else {
						try {
							indice = Integer.parseInt(indiPagador);

							if (indice < 1 || indice > participantes.size()) {
								System.out.println("Error: Número de participante inválido.");
							} else {
								break;
							}
						} catch (NumberFormatException e) {
							System.out.println("Error: Debe ingresar un número.");
						}
					}
				}

				pagador = participantes.get(indice - 1);

				// Declarar Gasto (Compartido)
				gasto = new GastoCompartido(concepto, cantidad, fecha, categoria, pagador);
			} else {
				// Declarar Gasto
				gasto = new Movimiento(concepto, cantidad, fecha, categoria);
			}

			// Registrar gasto
			gestor.registrarGasto(gasto, cuentaActual);
			System.out.println("Gasto registrado");

		} catch (Exception e) {
			System.out.println("Error al registrar el gasto: " + e.getMessage());
		}
	}

	// FUNCION PARA MODIFICAR GASTOS/MOVIMIENTOS
	public void modificar() {

		// Obtener e imprimir los gastos que se tienen hasta el momento
		List<Movimiento> gastoViejo = cuentaActual.getMovimientos();
		if (gastoViejo.isEmpty()) {
			System.out.println("No hay movimientos en la cuenta.");
			return;
		}
		imprimirGastos(gastoViejo);

		// Tomar el gasto que se quiere modificar a traves de una lista enumerada
		int n;
		while (true) {
			System.out.print("Numero a modificar : ");
			String numero = scanner.nextLine();

			if (numero.equals("cancelar")) {
				System.out.println("Operacion cancelada");
				return;
			}
			try {
				n = Integer.parseInt(numero);
				if (n < 1 || n > gastoViejo.size()) {
					System.out.println("Numero tomado como indice es invalido");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Introduce un número válido.");
			}
		}
		// Movimiento/ Gasto seleccionado
		Movimiento viejo = gastoViejo.get(n - 1);

		// Concepto
		System.out.print("Concepto [" + viejo.getConcepto() + "]: ");
		String nuevoConcepto = scanner.nextLine();

		if (nuevoConcepto.equals("cancelar")) {
			System.out.println("Operación cancelada.");
			return;
		}

		if (nuevoConcepto.isEmpty()) {
			nuevoConcepto = viejo.getConcepto();
		}

		// Cantidad
		double nuevaCantidad;
		while (true) {
			System.out.print("Cantidad [" + viejo.getCantidad() + "]: ");
			String nC = scanner.nextLine();
			if (nC.equals("cancelar")) {
				System.out.println("Operación cancelada.");
				return;
			}
			if (nC.isEmpty()) { // Se mantiene el dato antiguo
				nuevaCantidad = viejo.getCantidad();
				break;
			}
			try {
				nuevaCantidad = Double.parseDouble(nC);
				if (nuevaCantidad <= 0) {
					System.out.println("Error: Cantidad ha de ser mayor que 0");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Error: Formato de cantidad inválido");
			}
		}

		// Fecha
		LocalDateTime nuevaFecha = null;
		while (true) {
			System.out.print("Fecha [" + viejo.getFecha().format(FECHA_CORRECTA) + "]: ");

			String fecha = scanner.nextLine();

			if (fecha.equals("cancelar")) {
				System.out.println("Operación cancelada.");
				return;
			}
			if (fecha.isEmpty()) { // Se mantiene el dato antiguo
				nuevaFecha = viejo.getFecha();
				break;
			}
			if (fecha.equals("ahora")) {
				nuevaFecha = LocalDateTime.now();
				break;
			} else {
				try {
					nuevaFecha = LocalDateTime.parse(fecha, FECHA_CORRECTA);
					break;
				} catch (DateTimeParseException e) {
					System.out.println("Error: Formato de fecha inválido. Use M/d/yyyy H:mm");
				}
			}
		}

		// Categoria
		System.out.print("Categoría [" + viejo.getCategoria() + "]: ");

		String cat = scanner.nextLine();
		Categoria nuevaCategoria;
		if (cat.equals("cancelar")) {
			System.out.println("Operación cancelada.");
			return;
		}
		if (cat.isEmpty()) { // Se mantiene el dato antiguo
			nuevaCategoria = viejo.getCategoria();
		} else {
			nuevaCategoria = new Categoria(cat);
		}

		// Participante
		Movimiento nuevo = null;
		if (cuentaActual instanceof CuentaCompartida cuentaComp && viejo instanceof GastoCompartido gastoComp) {
			System.out.println("Participantes disponibles:");
			List<Participante> participantes = cuentaComp.getParticipantes();
			for (int i = 0; i < participantes.size(); i++) {
				System.out.println("  " + (i + 1) + ". " + participantes.get(i).getNombre());
			}
			int indice = -1;
			Participante nuevoPagador = gastoComp.getPagador();

			while (true) {
				System.out.print("Pagador [" + gastoComp.getPagador().getNombre() + "]: ");
				String pagadorStr = scanner.nextLine().trim();

				if (pagadorStr.isEmpty()) { // Se mantiene el dato antiguo
					nuevoPagador = gastoComp.getPagador();
					break;
				} else {
					try {
						indice = Integer.parseInt(pagadorStr) - 1;
						if (indice < 0 || indice >= participantes.size()) {
							System.out.println("Error: Número de participante inválido.");
						} else {
							nuevoPagador = participantes.get(indice);
							break;
						}
					} catch (NumberFormatException e) {
						System.out.println("Error: Introduce un número válido.");
					}
				}
			}
			nuevo = new GastoCompartido(nuevoConcepto, nuevaCantidad, nuevaFecha, nuevaCategoria, nuevoPagador);

		} else {
			nuevo = new Movimiento(nuevoConcepto, nuevaCantidad, nuevaFecha, nuevaCategoria);
		}

		// Modificar el gasto
		gestor.modificarGasto(cuentaActual, nuevo, viejo);
		System.out.println("OK: movimiento modificado.");
	}

	// FUNCION PARA ELIMINAR GASTOS/MOVIMIENTOS
	public void eliminar() {

		// Obtener e imprimir los gastos que se tienen hasta el momento
		List<Movimiento> lista = cuentaActual.getMovimientos();
		if (lista.isEmpty()) {
			System.out.println("No hay movimientos en la cuenta.");
			return;
		}
		imprimirGastos(lista);

		// Tomar el gasto que se quiere modificar a traves de una lista enumerada
		int n;
		while (true) {
			System.out.print("Numero a eliminar : ");
			String numero = scanner.nextLine();

			if (numero.equals("cancelar")) {
				System.out.println("Operacion cancelada");
				return;
			}
			try {
				n = Integer.parseInt(numero);
				if (n < 1 || n > lista.size()) {
					System.out.println("Numero tomado como indice es invalido");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Introduce un número válido.");
			}
		}

		// Confirmar o cancelar eliminacion
		while (true) {
			System.out.println("¿Confirmar eliminación? (s/n): ");
			String confirmacion = scanner.nextLine();

			if (confirmacion.equalsIgnoreCase("n")) {
				System.out.println("Cancelado.");
				return;
			} else if (confirmacion.equalsIgnoreCase("s")) {
				gestor.eliminarGasto(cuentaActual, lista.get(n - 1));
				System.out.println("Movimiento eliminado");
				break;
			} else {
				System.out.println("Respuesta invalida debe de ser 's' (Si) o 'n' (No)");
			}
		}
	}

	// Tutorial de eliminar
	public void eliminarHelp(String help) {
		System.out.println("      ELIMINAR");
		System.out.println("Con esta orden se podra eliminar gastos.");
		System.out.println("Se mostrara una lista enumerada de todos los gastos.");
		System.out.println("Si se ingresa el numero del gasto asociado se eliminara.");
		System.out.println("Se podra cancelar escribiendo cancelar");
	}

	// Tutorial de modificar
	public void modificarHelp(String help) {
		System.out.println("      MODIFICAR");
		System.out.println("Con esta orden se pueden editar gastos ya registrados.");
		System.out.println("Se mostrara una lista enumerada de todos los gastos.");
		System.out.println("Si se ingresa el numero del gasto asociado se podra modificar.");
		System.out.println("Se podra cancelar escribiendo cancelar.");
		System.out.println("Si no se escibe nada al modificar un dato se quedara igual.");
	}

	// Tutorial de registrar
	public void registrarHelp(String help) {
		System.out.println("      REGISTRAR");
		System.out.println("Con esta orden se puede registrar gastos.");
		System.out.println("> Concepto");
		System.out.println("> Cantidad");
		System.out.println("> Fecha (Formato M/d/yyyy H:mm) -> Si escibes 'ahora', se pondra la fecha actual");
		System.out.println("> Categoria");
		System.out.println("> Pagador (En el caso de que sea una cuenta compartida) ");
		System.out.println("Se podra cancelar escribiendo cancelar.");

	}

	// Tutorial genera de ordenes
	public void imprimirHelp() {
		System.out.println("      AYUDA");
		System.out.println("  help -> Muestra todos los comandos");
		System.out.println("  exit  -> Salir de la terminal");
		System.out.println("  registrar -> registrar un gasto");
		System.out.println("  modificar -> modificar un gasto");
		System.out.println("  eliminar -> eliminar un gasto");
		System.out.println(
				"Para registrar, modificar y eliminar se podra usar -h para obtener informacion de las ordenes");
	}

	// Funcion para imprimir lista de gastos.
	private void imprimirGastos(List<Movimiento> lista) {
		int i = 0;
		for (Movimiento m : lista) {
			if (m instanceof GastoCompartido gastoCompartido) {
				System.out.printf("%d: %s, %.2f, %s, %s, %s%n", i + 1, m.getConcepto(), m.getCantidad(),
						m.getFecha().format(FECHA_CORRECTA), m.getCategoria().getNombre(),
						gastoCompartido.getPagador().getNombre());
			} else {
				System.out.printf("%d: %s, %.2f, %s, %s%n", i + 1, m.getConcepto(), m.getCantidad(),
						m.getFecha().format(FECHA_CORRECTA), m.getCategoria().getNombre());
			}
			i++;
		}
	}
}