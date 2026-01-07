package umu.tds.proyecto;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        // Inicializar la configuración
        Configuracion configuracion = new ConfiguracionImpl();
        Configuracion.setInstancia(configuracion);
        
        // Pasar el stage al SceneManager para inicializarlo
        configuracion.getSceneManager().inicializar(stage);
        
        // Iniciar la aplicación
        configuracion.getSceneManager().showVentanaPrincipal();
    }

    public static void main(String[] args) {
        launch(args);
    }
}