package umu.tds.proyecto.adapters.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import umu.tds.proyecto.negocio.modelo.*;

public class RepositorioCategorias {
    
    private Map<String, Categoria> categorias;
    
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
}