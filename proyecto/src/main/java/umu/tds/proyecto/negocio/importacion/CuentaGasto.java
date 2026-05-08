package umu.tds.proyecto.negocio.importacion;

import java.util.Objects;

import umu.tds.proyecto.negocio.modelo.Movimiento;

/**
 * Objeto auxiliar usado durante la importación.
 *
 * Representa un gasto leído de un fichero externo junto con el nombre
 * de la cuenta en la que debe registrarse.
 */

/**
 * DTO interno de importación
 * 
 * Relaciona un movimiento importado cin el nombre de la cuenta en la que se debe registrar
 * Permite que los adaptadores no dependan directamente de Usuario ni de la estructura completa del dominio
 */
public class CuentaGasto {

    private final String cuenta;
    private final Movimiento gasto;

    public CuentaGasto(String cuenta, Movimiento gasto) {
        this.cuenta = Objects.requireNonNull(cuenta, "La cuenta no puede ser nula").trim();
        this.gasto = Objects.requireNonNull(gasto, "El gasto no puede ser nulo");

        if (this.cuenta.isBlank()) {
            throw new IllegalArgumentException("El nombre de la cuenta no puede estar vacío");
        }
    }

    public String getNombreCuenta() {
        return cuenta;
    }

    public Movimiento getGasto() {
        return gasto;
    }
}