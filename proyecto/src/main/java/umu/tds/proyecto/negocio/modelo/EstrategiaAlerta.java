package umu.tds.proyecto.negocio.modelo;

import java.util.List;

public interface EstrategiaAlerta {
    boolean superaUmbral(List<Movimiento> movimientos, double umbral, Categoria categoria);
}