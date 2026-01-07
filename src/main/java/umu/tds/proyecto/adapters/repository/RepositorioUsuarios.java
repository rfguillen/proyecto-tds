package umu.tds.proyecto.adapters.repository;

import java.util.HashMap;
import java.util.Map;

import umu.tds.proyecto.negocio.modelo.Usuario;

public class RepositorioUsuarios {
	
	
	private Map<String, Usuario> usuarios;
	
	public RepositorioUsuarios() {
		this.usuarios = new HashMap<>();
	}
	
	//buscar usuario especifico
	public Usuario buscar(String nombre) {
		if (nombre == null) {
			return null;
		}
		return usuarios.get(nombre);
	}
	
	//añade usuario
	public void añadir(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("Usuario no valido");
		}
		usuarios.put(usuario.getNombre(), usuario);
		
	}
	//elemina usuarios
	public void eliminar(String nombre) {
		if (nombre == null) {
			throw new IllegalArgumentException("No se puede eliminar "+ nombre +", porque no existe");
		}
		usuarios.remove(nombre);
	}
}
