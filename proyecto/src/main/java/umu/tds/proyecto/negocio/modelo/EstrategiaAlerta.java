package umu.tds.proyecto.negocio.modelo;

import java.util.List;

/**
 * Estrategia para comprobar alertas
 * 
 * Cada implementación define cómo se calcula el gasto acumulado
 * según el periodo temporal
 */
public interface EstrategiaAlerta {
    boolean superaUmbral(List<Movimiento> movimientos, double umbral, Categoria categoria);
}