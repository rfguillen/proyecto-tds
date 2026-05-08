package umu.tds.proyecto.adapters.repository;

import umu.tds.proyecto.negocio.modelo.Categoria;
import java.util.List;

/**
 * Interfaz del repositorio de categorías
 * 
 * Define las operaciones necesarias para consultar y mantener las categorías
 */
public interface IRepositorioCategorias {
    void añadir(Categoria categoria);
    Categoria buscar(String nombre);
    void eliminar(String nombre);
    List<Categoria> getTodas();
    void añadirCategoriasPredefinidas();
}