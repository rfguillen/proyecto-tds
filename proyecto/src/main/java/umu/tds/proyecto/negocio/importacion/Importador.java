package umu.tds.proyecto.negocio.importacion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Participante;

public class Importador implements InterfazImportador {
	
	@Override
	
	public List<CuentaGasto> importar(Path fichero) throws IOException {
		
		
		if (fichero == null || !Files.exists(fichero)) {
			throw new IllegalArgumentException("El fichero no existe");
		}
		
		List<CuentaGasto> resultado = new ArrayList<>();
		
		Files.readAllLines(fichero).stream()
		     .skip(1) 														//saltar cabecera
		     .forEach(linea -> {
		    	 String[] campos = linea.split(",");						//divide la linea respecto ","
		    	 
		    	 String fecha1 = campos[0];
		    	 String cuenta = campos[1];									//tipo / nombre de la cuenta
		    	 Categoria categoria = new Categoria(campos[2]);			// categoria
		    	 
		    	 ///
		    	 /// CONCEPTO EN GASTOS ES NOTA O SUBCATEGORIA???
		    	 /// EN EL CODIGO AHORA MISMMO ES SUBCATEGORIA
		    	 ///
		    	 String subcategoria = campos[3];							//subcategoria / concepto
		    	 String nota = campos[4];									// nota / concepto
		    	 ///
		    	 ///
		    	 ///
		    	
		    	 String pagador = campos[5];								// pagador
		    	 double cantidad = Double.parseDouble(campos[6]);			// cantidad
		    	 //TODO:?? saber la concurrencia es necesario??
		    	 //String concurrencia = campos[7];
		    	 
		    	 LocalDateTime f = LocalDateTime.parse(fecha1, DateTimeFormatter.ofPattern("M/d/yyyy H:mm"));
		    	 
		    	 Movimiento gasto;
		    	 
		    	 // Si la cuenta es compartida
		    	 if (cuenta.startsWith("Compartida")) {						
		    		 Participante p = new Participante(pagador, 0.0);
		    		 gasto = new GastoCompartido(subcategoria, cantidad, f, categoria, p);
		    		 
		    	 }
		    	 // Si la cuenta no lo es 
		    	 else {
		    		 gasto = new Movimiento(subcategoria, cantidad, f, categoria);
		    	 }
		    	 
		    	 //añadir dato
		    	 resultado.add(new CuentaGasto(cuenta, gasto));
		    	 
		     });
		
		return resultado;
	}

}
