package umu.tds.proyecto.negocio.importacion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.Participante;

/**
 * Adaptador para importar gastos desde ficheros CSV con formato:
 * Date,Account,Category,Subcategory,Note,Payer,Amount,Currency
 *
 * El adaptador convierte ese formato en objetos CuentaGasto,
 * que son los que entiende el resto de la aplicación.
 */
public class ImportadorCsv implements InterfazImportador {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    @Override
    public List<CuentaGasto> importar(Path fichero) throws IOException {
        return Files.readAllLines(fichero).stream()
                .skip(1) // Se ignora la cabecera del fichero
                .filter(linea -> !linea.isBlank())
                .map(this::parsearLinea)
                .toList();
    }

    /**
     * Parsea una línea con estructura:
     * Date,Account,Category,Subcategory,Note,Payer,Amount,Currency
     */
    private CuentaGasto parsearLinea(String linea) {
        String[] campos = linea.split(",", -1);

        if (campos.length < 8) {
            throw new IllegalArgumentException("Línea CSV incorrecta: " + linea);
        }

        LocalDateTime fecha = LocalDateTime.parse(campos[0].trim(), FORMATO_FECHA);
        String nombreCuenta = campos[1].trim();

        /*
         * En el fichero externo, Category representa el método de pago
         * o tipo externo de operación: con tarjeta, efectivo, on line...
         *
         * Para nuestra aplicación, la categoría útil del gasto es Subcategory:
         * Comida, Gasolina, Ocio, Transporte Público, etc.
         */
        Categoria categoria = new Categoria(campos[3].trim());

        /*
         * Note contiene la descripción concreta del gasto.
         */
        String concepto = campos[4].trim();

        String pagador = campos[5].trim();
        double cantidad = Double.parseDouble(campos[6].trim());

        Movimiento movimiento;

        if (esCuentaCompartida(nombreCuenta)) {
            /*
             * El participante creado aquí es provisional.
             * GestionGastos debe sustituirlo por el participante real
             * de la CuentaCompartida correspondiente.
             */
            Participante participante = new Participante(pagador, 0.0);
            movimiento = new GastoCompartido(concepto, cantidad, fecha, categoria, participante);
        } else {
            /*
             * En una cuenta personal no hace falta tener en cuenta el pagador,
             * aunque el fichero externo incluya un campo Payer.
             */
            movimiento = new Movimiento(concepto, cantidad, fecha, categoria);
        }

        return new CuentaGasto(nombreCuenta, movimiento);
    }

    private boolean esCuentaCompartida(String nombreCuenta) {
        return nombreCuenta.toLowerCase().startsWith("compartida");
    }
}