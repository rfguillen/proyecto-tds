package umu.tds.proyecto;

import umu.tds.proyecto.negocio.controladores.GestionGastos;

public class ConfiguracionImpl extends Configuracion {
	private GestionGastos controlador;
	
	public ConfiguracionImpl() {
		this.controlador = new GestionGastos();
	}
	
	@Override
	public GestionGastos getControladorGastos() {
		return controlador;
	}
}
