package umu.tds.proyecto;

import umu.tds.proyecto.negocio.controladores.GestionGastos;
import umu.tds.proyecto.vista.SceneManager;

public abstract class Configuracion {
	private static Configuracion instancia;
	
	private final SceneManager sceneManager = new SceneManager();
	
	public static void setInstancia(Configuracion impl) {
		Configuracion.instancia = impl;
	}
	
	public static Configuracion getInstancia() {
		return Configuracion.instancia;
	}
	
	public SceneManager getSceneManager() {
		return sceneManager;
	}
	
	public abstract GestionGastos getControladorGastos();
}
