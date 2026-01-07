package umu.tds.proyecto.negocio.importacion;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface InterfazImportador {
	
	List<CuentaGasto> importar(Path fichero) throws IOException;
	// importar 
	//		- lee fichero csv y obtendra una lista de clases cuentaGasto
	//		- cuentaGasto -> clase que asocia a una cuenta 
	//						 (individual o compartida) sus gastos
	
}
