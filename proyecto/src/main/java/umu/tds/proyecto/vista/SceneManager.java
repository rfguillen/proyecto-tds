package umu.tds.proyecto.vista;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import umu.tds.proyecto.App;

public class SceneManager {
	private Stage stagePrincipal;
	
	public SceneManager() {
	}
	
	public void inicializar(Stage stage) {
		this.stagePrincipal = stage;
	}
	
	// Métodos para llamar desde el controlador
	
	public void showVentanaPrincipal() {
		cambiarEscena("VentanaPrincipal", "Gestion Gastos");
	}
	
	public void showVistaGasto() {
		abrirVentana("VistaGasto");
	}
	
	public void showVistaGrupo() {
		abrirVentana("VistaGrupo");
	}
	
	public void showVistaImportador() {
		abrirVentana("VistaImportador");
	}
	
	public void showVistaEstadistica() {
		abrirVentana("VistaEstadistica");
	}
	
	public void showVistaAlerta() {
		abrirVentana("VistaAlerta");
	}
	
	public void showVistaFiltrar() {
		abrirVentana("VistaFiltrar");
	}
	
	// Cambia la ventana
	private void cambiarEscena(String fxml, String titulo) {
		try {
			Parent root = loadFXML(fxml);
			Scene scene = new Scene(root);
			stagePrincipal.setScene(scene);
			stagePrincipal.setTitle(titulo);
			stagePrincipal.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Abrir ventana sin cerrar la principal
	private void abrirVentana(String fxml) {
		try {
			Parent root = loadFXML(fxml);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("vista/" + fxml + ".fxml"));
		return fxmlLoader.load();
	}
	
}
