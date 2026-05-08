package umu.tds.proyecto.negocio.importacion;

import java.nio.file.Path;

/**
 * Factoría encargada de crear el importador adecuado según el tipo de fichero.
 *
 * Patrón Método Factoría:
 * GestionGastos solicita un importador para un fichero, pero no se necesita
 * conocer qué clase concreta de instancia. La decisión se centraliza aquí según
 * la extensión del fichero
 */
public class FactoriaImportadores {

    /**
     * Crea un importador en función de la extensión del fichero.
     *
     * @param fichero fichero que se desea importar
     * @return importador adecuado para ese formato
     */
    public InterfazImportador crearImportador(Path fichero) {
        String nombre = fichero.getFileName().toString().toLowerCase();

        if (nombre.endsWith(".csv")) {
            return new ImportadorCsv();
        }

        if (nombre.endsWith(".txt")) {
            return new ImportadorTxt();
        }

        throw new IllegalArgumentException("Formato de importación no soportado: " + nombre);
    }
}