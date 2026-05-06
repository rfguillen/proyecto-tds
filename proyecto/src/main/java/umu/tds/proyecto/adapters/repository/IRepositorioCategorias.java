package umu.tds.proyecto.adapters.repository;

import umu.tds.proyecto.negocio.modelo.Categoria;
import java.util.List;

public interface IRepositorioCategorias {
    void añadir(Categoria categoria);
    Categoria buscar(String nombre);
    void eliminar(String nombre);
    List<Categoria> getTodas();
    void añadirCategoriasPredefinidas();
}