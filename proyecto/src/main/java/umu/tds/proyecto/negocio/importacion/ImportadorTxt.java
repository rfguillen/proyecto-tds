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
 * Adaptador para importar gastos desde ficheros TXT separados por punto y coma.
 *
 * Usa el mismo esquema lógico que el CSV:
 * Date;Account;Category;Subcategory;Note;Payer;Amount;Currency
 */
public class ImportadorTxt implements InterfazImportador {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    @Override
    public List<CuentaGasto> importar(Path fichero) throws IOException {
        return Files.readAllLines(fichero).stream()
                .skip(1)
                .filter(linea -> !linea.isBlank())
                .map(this::parsearLinea)
                .toList();
    }

    private CuentaGasto parsearLinea(String linea) {
        String[] campos = linea.split(";", -1);

        if (campos.length < 8) {
            throw new IllegalArgumentException("Línea TXT incorrecta: " + linea);
        }

        LocalDateTime fecha = LocalDateTime.parse(campos[0].trim(), FORMATO_FECHA);
        String nombreCuenta = campos[1].trim();

        // Igual que en CSV: Subcategory es la categoría real de la app.
        Categoria categoria = new Categoria(campos[3].trim());

        // Note es el concepto/descripción del gasto.
        String concepto = campos[4].trim();

        String pagador = campos[5].trim();
        double cantidad = Double.parseDouble(campos[6].trim());

        Movimiento movimiento;

        if (esCuentaCompartida(nombreCuenta)) {
            Participante participante = new Participante(pagador, 0.0);
            movimiento = new GastoCompartido(concepto, cantidad, fecha, categoria, participante);
        } else {
            movimiento = new Movimiento(concepto, cantidad, fecha, categoria);
        }

        return new CuentaGasto(nombreCuenta, movimiento);
    }

    private boolean esCuentaCompartida(String nombreCuenta) {
        return nombreCuenta.toLowerCase().startsWith("compartida");
    }
}