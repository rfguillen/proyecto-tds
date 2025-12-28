package umu.tds.proyecto.negocio.modelo;

public class ManejadorExcepciones {

	//conforme sigamos avanzando loa ampliare

	    public static void error(Exception e) {
	      
	        System.err.println("ERROR: " + e.getClass().getName() + " - " + e.getMessage());
	        e.printStackTrace();

	        // 2. Mensaje “amigable” según el tipo de excepción
	        if (e instanceof NullPointerException) {
	            mostrarMensajeUsuario("Se produjo un error interno. Faltan datos necesarios.");
	        } else if (e instanceof IllegalArgumentException) {
	            mostrarMensajeUsuario("Se recibieron datos inválidos. Revisa la información ingresada.");
	        } else {
	            mostrarMensajeUsuario("Ha ocurrido un error inesperado. Si persiste, contacta con soporte.");
	        }
	    }

	    private static void mostrarMensajeUsuario(String mensaje) {
	        
	        System.out.println("[MENSAJE AL USUARIO] " + mensaje);
	   
	    }
	}

