package umu.tds.proyecto;

import java.util.Objects;

import umu.tds.proyecto.negocio.controladores.GestionGastos;
import umu.tds.proyecto.vista.SceneManager;

/**
 * Configuración global de la aplicación.
 *
 * Aplica la idea de Singleton como punto de acceso único a la configuración
 * activa del sistema. La instancia solo puede establecerse una vez.
 */
public abstract class Configuracion {

    private static Configuracion instancia;

    private final SceneManager sceneManager = new SceneManager();

    public static void setInstancia(Configuracion impl) {
        if (instancia != null) {
            throw new IllegalStateException("La configuración ya ha sido inicializada");
        }
        Configuracion.instancia = Objects.requireNonNull(
                impl,
                "La configuración no puede ser nula"
        );
    }

    public static Configuracion getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("La configuración todavía no ha sido inicializada");
        }
        return Configuracion.instancia;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public abstract GestionGastos getControladorGastos();
}