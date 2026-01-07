package umu.tds.proyecto.vista;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.controladores.GestionGastos;
import umu.tds.proyecto.negocio.importacion.CuentaGasto;
import umu.tds.proyecto.negocio.importacion.Importador;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.CuentaCompartida;
import umu.tds.proyecto.negocio.modelo.CuentaPersonal;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.Participante;

public class VistaImportadorController {

    @FXML
    private TextField textoFichero;

    @FXML
    void actionCancelar(ActionEvent event) {
    	cerrarVentana();
    }

    @FXML
    void actionExaminar(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Seleccionar archivo de datos");
    	
    	fileChooser.getExtensionFilters().addAll(
    			new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"),
    			new FileChooser.ExtensionFilter("Archivos JSON", "*.json"),
    			new FileChooser.ExtensionFilter("Otro Tipo", "*.*"));
    	
    	Stage stage = (Stage) textoFichero.getScene().getWindow();
    	File file = fileChooser.showOpenDialog(stage);
    	
    	if (file != null) {
    		textoFichero.setText(file.getAbsolutePath());
    	}
    }

   
    @FXML
    void actionImportar(ActionEvent event) {
        String ruta = textoFichero.getText();
        if (ruta == null || ruta.trim().isEmpty()) return;

        try {
            GestionGastos gg = Configuracion.getInstancia().getControladorGastos();
            Importador importador = new Importador();
            List<CuentaGasto> items = importador.importar(Path.of(ruta.trim()));
            
            System.out.println("--- INICIO IMPORTACIÓN ---");

            // 1. AGRUPAR movimientos por cuenta para identificar a TODOS los participantes
            Map<String, List<CuentaGasto>> porCuenta = items.stream()
                .collect(Collectors.groupingBy(cg -> cg.getNombreCuenta().trim()));

            for (String nombreCSV : porCuenta.keySet()) {
                if (nombreCSV.equalsIgnoreCase("Personal")) continue;

                boolean existe = gg.getCuentasUsuario().stream()
                        .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombreCSV));

                if (!existe) {
                    // Extraer TODOS los nombres de pagadores únicos que aparecen en el CSV para esta cuenta
                    Set<String> nombresParticipantes = new HashSet<>();
                    for (CuentaGasto cg : porCuenta.get(nombreCSV)) {
                        if (cg.getGasto() instanceof GastoCompartido gc) {
                            nombresParticipantes.add(gc.getPagador().getNombre());
                        }
                    }

                    if (!nombresParticipantes.isEmpty()) {
                        System.out.println("Creando cuenta '" + nombreCSV + "' con participantes: " + nombresParticipantes);
                        
                        // Crear la lista de participantes (porcentaje 0 para que tu controlador reparta equitativamente)
                        List<Participante> listaP = nombresParticipantes.stream()
                            .map(nombre -> new Participante(nombre, 0.0))
                            .collect(Collectors.toList());
                        
                        gg.crearCuentaCompartida(nombreCSV, listaP);
                    }
                }
            }

            // 2. IMPORTAR los movimientos ahora que las cuentas tienen a todos sus miembros
            int importados = 0;
            for (CuentaGasto cg : items) {
                String nombreCSV = cg.getNombreCuenta().trim();
                Movimiento mov = cg.getGasto();
                
                Cuenta destino = nombreCSV.equalsIgnoreCase("Personal") ? gg.getCuentaGlobal() :
                    gg.getCuentasUsuario().stream().filter(c -> c.getNombre().equalsIgnoreCase(nombreCSV)).findFirst().orElse(null);

                if (destino == null) continue;

                if (destino instanceof CuentaCompartida cc && mov instanceof GastoCompartido gc) {
                    // Ahora el pagador SÍ debería estar en la cuenta
                    Participante pagadorReal = cc.getParticipantes().stream()
                        .filter(p -> p.getNombre().equalsIgnoreCase(gc.getPagador().getNombre()))
                        .findFirst().orElse(null);

                    if (pagadorReal != null) {
                        cc.registrarGasto(gc.getConcepto(), gc.getCantidad(), pagadorReal);
                        importados++;
                    } else {
                        System.out.println("Error: " + gc.getPagador().getNombre() + " sigue sin estar en " + nombreCSV);
                    }
                } else if (destino instanceof CuentaPersonal cp) {
                    gg.registrarGasto(mov, cp);
                    importados++;
                }
            }

            System.out.println("--- FIN IMPORTACIÓN ---");
            System.out.println("Total importados: " + importados + " de " + items.size());
            cerrarVentana();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoFichero.getScene().getWindow();
    	stage.close();
    }

}
