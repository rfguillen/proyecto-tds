package umu.tds.proyecto.adapters.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import umu.tds.proyecto.negocio.modelo.*;

/**
 * Repositorio de categorías en memoria
 * 
 * Patrón Repositorio:
 * encapsula el acceso al listado de categorías y evita que el resto de la
 * aplicación depende de la estructura interna de almacenamiento
 */


public class RepositorioCategorias implements IRepositorioCategorias {
    
    private Map<String, Categoria> categorias;
    private static final List<String> CATEGORIAS_PREDEFINIDAS = List.of(
            "Alimentacion",
            "Transporte",
            "Entretenimiento",
            "Suministros",
            "Vivienda",
            "Ocio",
            "Ingreso",
            "Otros"
    );
    public RepositorioCategorias() {
        this.categorias = new HashMap<>();
    }

    public void añadir(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria no puede ser nulo");
        }
        categorias.put(categoria.getNombre(), categoria);
    }

    public Categoria buscar(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("Nombre de categoría no puede ser nulo");
        }
        Categoria categoria = categorias.get(nombre);
        
        if (categoria == null) {
            categoria = new Categoria(nombre);
            añadir(categoria);
        }
        return categoria;
    }
    
    public void eliminar(String nombre) { 
        if (nombre == null) {
            throw new IllegalArgumentException("No se puede eliminar "+ nombre +", porque no existe");
        }
        categorias.remove(nombre);
    }
    
    public List<Categoria> getTodas() {
        return new ArrayList<>(categorias.values());
    }

    public void añadirCategoriasPredefinidas() {
        for (String nombre : CATEGORIAS_PREDEFINIDAS) {
            buscar(nombre);  // buscar ya crea la categoría si no existe
        }
    }
}