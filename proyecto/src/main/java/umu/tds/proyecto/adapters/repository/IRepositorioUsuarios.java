package umu.tds.proyecto.adapters.repository;

import umu.tds.proyecto.negocio.modelo.Usuario;

public interface IRepositorioUsuarios {
    Usuario buscar(String nombre);
    void añadir(Usuario usuario);
    void eliminar(String nombre);
}