package umu.tds.proyecto.negocio.importacion;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface InterfazImportador {
	
	List<CuentaGasto> importar(Path fichero) throws IOException;
	/**
     * Importa los gastos contenidos en un fichero.
     *
     * @param fichero ruta del fichero externo
     * @return lista de gastos importados junto con su cuenta destino
     * @throws IOException si ocurre un error leyendo el fichero
     */
}
