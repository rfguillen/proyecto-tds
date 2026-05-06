package umu.tds.proyecto;

import umu.tds.proyecto.negocio.controladores.GestionGastos;

public class ConfiguracionImpl extends Configuracion {
	private GestionGastos controlador;
	

	public ConfiguracionImpl() {
		this.controlador = new GestionGastos();

		// Guardar al cerrar la aplicación
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
				this.controlador.guardar()
		));
	}
	@Override
	public GestionGastos getControladorGastos() {
		return controlador;
	}
}
