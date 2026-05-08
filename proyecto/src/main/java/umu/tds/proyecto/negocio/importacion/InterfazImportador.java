package umu.tds.proyecto.negocio.importacion;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interfaz objetivo del sistema de importación
 * 
 * Patrón Adaptador:
 * cada importador concreto adapta un formato externo de fichero al
 * formato interno que espera la aplicación (una lista de CuentaGasto)
 */
public interface InterfazImportador {
	
	/**
     * Importa los gastos contenidos en un fichero.
     *
     * @param fichero ruta del fichero externo
     * @return lista de gastos importados junto con su cuenta destino
     * @throws IOException si ocurre un error leyendo el fichero
     */
	List<CuentaGasto> importar(Path fichero) throws IOException;
}
