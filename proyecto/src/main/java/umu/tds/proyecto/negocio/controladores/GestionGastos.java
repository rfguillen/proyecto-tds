package umu.tds.proyecto.negocio.controladores;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import umu.tds.proyecto.adapters.repository.RepositorioCategorias;
import umu.tds.proyecto.adapters.repository.RepositorioUsuarios;
import umu.tds.proyecto.negocio.importacion.CuentaGasto;
import umu.tds.proyecto.negocio.importacion.Importador;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.CuentaCompartida;
import umu.tds.proyecto.negocio.modelo.CuentaPersonal;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Participante;
import umu.tds.proyecto.negocio.modelo.Usuario;

public class GestionGastos {
	
	private static GestionGastos unicaInstancia;
	
	
	
	private Usuario usuarioActual;
	
	private RepositorioCategorias repositorioCategorias;
	private RepositorioUsuarios repositorioUsuarios;
	
	private Importador importador;
	
	public GestionGastos() {		
		this.repositorioCategorias = new RepositorioCategorias();
		this.repositorioUsuarios = new RepositorioUsuarios();
		this.importador = new Importador();

		this.usuarioActual = new Usuario("Prueba");
	}
	
	//Singleton: Asegurar clase tiene instancia unica y punto de acceso unico
	public static GestionGastos getInstancia() {
		if (unicaInstancia == null)  unicaInstancia = new GestionGastos(); 
		return unicaInstancia;
	}
	
	//Cuenta Personal del usuarioActual
	public CuentaPersonal getCuentaGlobal() {
		return usuarioActual.getCuentaPersonal();
	}
	
	//Confirmar categoria -> Se busca si existe. Si existe se devuelve, si no, se crea y se devuelve
	
	public Categoria confirmarCategoria(String categoria) {
		return repositorioCategorias.buscar(categoria);
	}
	
	
	
	
	//Gastos------
	
	//REGISTRAR GASTO
	public void registrarGasto(Movimiento gasto, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException("Cuenta no valida");
		}
		//Añadimos la categoria al repositorio si es necesario
        gasto.setCategoria(confirmarCategoria(gasto.getCategoria().getNombre()));
        
        //añadir dinero
        cuenta.ingresarDinero(gasto.getCantidad());
        
        //En el caso de que sea una cuenta compartida recalcular los saldos
        if (cuenta instanceof CuentaCompartida cc) {
            cc.calcularSaldos();
        }
        
        //Actualizacion de datos en el repositorio
        repositorioUsuarios.añadir(usuarioActual);
		
	}
	
	//MODIFICAR GASTO
	
	public void modificarGasto(Cuenta cuenta, Movimiento gastoNuevo, Movimiento gastoViejo) {
		if (cuenta == null) {
			throw new IllegalArgumentException("La cuenta no es valida para la modificacion");
		}
		if (gastoNuevo == null || gastoViejo == null) {
			throw new IllegalArgumentException("Los gastos no son validos para modificacion");
		}
		//modificar el gasto viejo con los nuevos datos
		gastoViejo.setCantidad(gastoNuevo.getCantidad());
		gastoViejo.setConcepto(gastoNuevo.getConcepto());
		gastoViejo.setFecha(gastoNuevo.getFecha());
		gastoViejo.setCategoria(confirmarCategoria(gastoNuevo.getCategoria().getNombre()));
		
		
		/* Si la cuenta en la que trabajamos es una cuentaa compartida y 
		 los gastos son de tipo compartidos (Los que no asumen una sola persona)
		 Actualizamos el pagador*/ 
		if (cuenta instanceof CuentaCompartida cc && gastoNuevo instanceof GastoCompartido gNuevo
				&& gastoViejo instanceof GastoCompartido gViejo) {

            gViejo.setPagador(gNuevo.getPagador());
            
        }
		//En el caso de que sea una cuenta compartida (sin gastos compartidos) recalcular los saldos
		if (cuenta instanceof CuentaCompartida cc) {
            cc.calcularSaldos();
        }
		//Actualizacion de datos en el repositorio
		repositorioUsuarios.añadir(usuarioActual);
	}
	
	//ELIMINAR GASTO
	public void eliminarGasto(Cuenta cuenta, Movimiento gasto) {
		if (cuenta == null || gasto == null) {
			throw new IllegalArgumentException("Datos no válidos para eliminar");
		}
		// eliminar los gastos
		for (Movimiento m : cuenta.getMovimientos()) {
			if (gasto.equals(m)) {
				
				List<Movimiento> ListaCompleta = cuenta.getMovimientos();
				ListaCompleta.remove(gasto);
				cuenta.setMovimientos(ListaCompleta);
				
				break;
			}
		}
		//En el caso de que sea una cuenta compartida recalcular los saldos
		if (cuenta instanceof CuentaCompartida cc) {
			cc.calcularSaldos();
		}
		//Actualizacion de datos en el repositorio
		repositorioUsuarios.añadir(usuarioActual);
	}
	
	//Crear Cuenta Compartida
	
	public CuentaCompartida crearCuentaCompartida(String cuenta, List<Participante> participantes) {
		if (cuenta == null) {
			throw new IllegalArgumentException("El nombre de la cuenta compartida no es valida");
		}
		if (participantes == null) {
			throw new IllegalArgumentException("No se ha encontrado ningun participante");
		}
		
		
		List<Participante> listaParticipantes = new ArrayList<>(participantes);
		
		// Se comprueba si el reparto es 0.0 todo el rato.
		boolean comprobarReparto = participantes.stream()
												.allMatch(p -> p.getPorcentajeParticipacion() == 0.0);
		// Si lo es, el reparto es completamente equitativo
		if (comprobarReparto) {
			double reparto = 100.0/participantes.size();
			for (Participante p : listaParticipantes) {
				p.setProcentajeParticipacion(reparto);
			}
		} else { // si no lo es comprobar que la suma de porcentajes sea la de un 100%
			double suma = participantes.stream()
						.mapToDouble(Participante::getPorcentajeParticipacion)
						.sum();
			if (Math.abs(suma - 100.0) > 0.01) {
				throw new IllegalArgumentException( "Los porcentajes no llegan o superan un 100% -> ("+suma+")");
			}
		}
		
		CuentaCompartida cuentaFinal = new CuentaCompartida(cuenta, listaParticipantes);
		
		//Añadir al usuarioActual
		usuarioActual.addCuenta(cuentaFinal);
		
		// Actualizar los datos del repositorio
		repositorioUsuarios.añadir(usuarioActual);
		
		return cuentaFinal;
	}
	
	//Importar gastos
	public void importarGastos(Path fichero) throws IOException {
	
		List<CuentaGasto> lista = importador.importar(fichero); //importar los gastos a una lista

		for (CuentaGasto cuenta : lista) {
	
			Movimiento movimiento = cuenta.getGasto();	
    
			movimiento.setCategoria(confirmarCategoria(movimiento.getCategoria().getNombre()));

			// Resolver cuenta destino
			String nombreCuenta = cuenta.getNombreCuenta();

			Cuenta destino = null;
			
			if (nombreCuenta.equals(usuarioActual.getCuentaPersonal().getNombre())) {
	            destino = getCuentaGlobal();
	        } else { 
	        	for (Cuenta c : usuarioActual.getCuentas()) {
	        		if (c.getNombre().equals(nombreCuenta)) {
	        			destino = c;
	        		}
	        	}
	        	if (destino == null) {
	        		throw new IllegalArgumentException("La cuenta a la que se quiere importar datos no existe");
	        	}
	        	
	        }

	        registrarGasto(movimiento, destino);
	       
		}
	}
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

