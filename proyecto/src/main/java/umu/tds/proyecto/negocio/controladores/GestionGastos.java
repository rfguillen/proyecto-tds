package umu.tds.proyecto.negocio.controladores;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import umu.tds.proyecto.adapters.repository.RepositorioCategorias;
import umu.tds.proyecto.adapters.repository.RepositorioUsuarios;
import umu.tds.proyecto.negocio.importacion.CuentaGasto;
import umu.tds.proyecto.negocio.importacion.Importador;
import umu.tds.proyecto.negocio.modelo.Alerta;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.CuentaCompartida;
import umu.tds.proyecto.negocio.modelo.CuentaPersonal;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.Notificacion;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Participante;
import umu.tds.proyecto.negocio.modelo.Usuario;
import umu.tds.proyecto.vista.VentanaPrincipalController;

public class GestionGastos {
    
    private static GestionGastos unicaInstancia;
    private Usuario usuarioActual;
    private RepositorioCategorias repositorioCategorias;
    private RepositorioUsuarios repositorioUsuarios;
    private Importador importador;
    
    public GestionGastos() {        
        this.repositorioCategorias = new RepositorioCategorias();
        this.repositorioUsuarios = new RepositorioUsuarios();
        this.importador = new Importador();
        this.usuarioActual = new Usuario("Prueba");
    }

    public static List<Movimiento> ordenarPorCategoria(Map<String, Movimiento> mapaGastos) {
        return mapaGastos.values().stream()
                .sorted(Comparator.comparing(m -> m.getCategoria().getNombre()))
                .collect(Collectors.toList());
    }

    public static GestionGastos getInstancia() {
        if (unicaInstancia == null) unicaInstancia = new GestionGastos(); 
        return unicaInstancia;
    }
    
    public CuentaPersonal getCuentaGlobal() {
        return usuarioActual.getCuentaPrincipal();
    }
    
    public List<Cuenta> getCuentasUsuario() {
        if (usuarioActual == null) return new ArrayList<>();
        return usuarioActual.getCuentas();
    }
    
    public Categoria confirmarCategoria(String categoria) {
        return repositorioCategorias.buscar(categoria);
    }

    public List<Movimiento> filtrarMovimientos(Cuenta cuenta, LocalDate inicio, LocalDate fin, Categoria categoria, Double min, Double max) {
        if (cuenta == null) return new ArrayList<>();
        
        return cuenta.getMovimientos().stream()
            .filter(m -> inicio == null || !m.getFecha().toLocalDate().isBefore(inicio))
            .filter(m -> fin == null || !m.getFecha().toLocalDate().isAfter(fin))
            .filter(m -> categoria == null || m.getCategoria().equals(categoria))
            .filter(m -> min == null || m.getCantidad() >= min)
            .filter(m -> max == null || m.getCantidad() <= max)
            .collect(Collectors.toList());
    }
    
    // REGISTRAR GASTO
    public void registrarGasto(Movimiento gasto, Cuenta cuenta) {
        if (cuenta == null) throw new IllegalArgumentException("Cuenta no valida");
        
        gasto.setCategoria(confirmarCategoria(gasto.getCategoria().getNombre()));
        
        // Registramos el movimiento (esto actualiza el saldo incrementalmente)
        cuenta.registrarMovimientoGasto(gasto);
        
        if (cuenta instanceof CuentaCompartida cc) {
            cc.calcularSaldos();
        }
        
        repositorioUsuarios.añadir(usuarioActual);
        comprobarAlertas(cuenta, gasto);
    }
    
    // MODIFICAR GASTO (Ahora usa recálculo total)
    public void modificarGasto(Cuenta cuenta, Movimiento gastoNuevo, Movimiento gastoViejo) {
        if (cuenta == null) throw new IllegalArgumentException("La cuenta no es valida");
        if (gastoNuevo == null || gastoViejo == null) throw new IllegalArgumentException("Datos inválidos");
        
        // 1. Aplicar cambios al objeto existente
        gastoViejo.setCantidad(gastoNuevo.getCantidad());
        gastoViejo.setConcepto(gastoNuevo.getConcepto());
        gastoViejo.setFecha(gastoNuevo.getFecha());
        gastoViejo.setCategoria(confirmarCategoria(gastoNuevo.getCategoria().getNombre()));
        
        if (cuenta instanceof CuentaCompartida cc && gastoNuevo instanceof GastoCompartido gNuevo
                && gastoViejo instanceof GastoCompartido gViejo) {
            gViejo.setPagador(gNuevo.getPagador());
        }

        // 2. FORZAR RECÁLCULO TOTAL DEL SALDO
        // Como hemos cambiado un movimiento que ya estaba en la lista, el saldo ha quedado desfasado.
        // Recorremos toda la lista para sumar de nuevo y obtener el saldo real.
        cuenta.recalcularSaldo();

        if (cuenta instanceof CuentaCompartida cc) {
            cc.calcularSaldos();
        }
        repositorioUsuarios.añadir(usuarioActual);
    }
    
    // ELIMINAR GASTO
    public void eliminarGasto(Cuenta cuenta, Movimiento gasto) {
        if (cuenta == null || gasto == null) throw new IllegalArgumentException("Datos no válidos");
        
        // El método de Cuenta ya elimina y llama a recalcularSaldo()
        cuenta.eliminarMovimiento(gasto);
        
        if (cuenta instanceof CuentaCompartida cc) {
            cc.calcularSaldos();
        }
        repositorioUsuarios.añadir(usuarioActual);
    }
    
    public CuentaCompartida crearCuentaCompartida(String cuenta, List<Participante> participantes) {
        if (cuenta == null) throw new IllegalArgumentException("Nombre no valido");
        if (participantes == null) throw new IllegalArgumentException("Sin participantes");
        
        List<Participante> listaParticipantes = new ArrayList<>(participantes);
        boolean comprobarReparto = participantes.stream().allMatch(p -> p.getPorcentajeParticipacion() == 0.0);

        if (comprobarReparto) {
            double reparto = 100.0/participantes.size();
            for (Participante p : listaParticipantes) p.setPorcentajeParticipacion(reparto);
        } else { 
            double suma = participantes.stream().mapToDouble(Participante::getPorcentajeParticipacion).sum();
            if (Math.abs(suma - 100.0) > 0.01) throw new IllegalArgumentException("Porcentajes != 100%");
        }
        
        CuentaCompartida cuentaFinal = new CuentaCompartida(cuenta, listaParticipantes);
        usuarioActual.addCuenta(cuentaFinal);
        repositorioUsuarios.añadir(usuarioActual);
        
        return cuentaFinal;
    }
    
    public void importarGastos(Path fichero) throws IOException {
        List<CuentaGasto> lista = importador.importar(fichero); 
        for (CuentaGasto cuenta : lista) {
            Movimiento movimiento = cuenta.getGasto();    
            movimiento.setCategoria(confirmarCategoria(movimiento.getCategoria().getNombre()));
            String nombreCuenta = cuenta.getNombreCuenta();
            Cuenta destino = null;
            
            if (nombreCuenta.equals(usuarioActual.getCuentaPrincipal().getNombre())) {
                destino = getCuentaGlobal();
            } else { 
                for (Cuenta c : usuarioActual.getCuentas()) {
                    if (c.getNombre().equals(nombreCuenta)) destino = c;
                }
                if (destino == null) throw new IllegalArgumentException("Cuenta destino no existe");
            }
            registrarGasto(movimiento, destino);
        }
    }
    
    public List<Categoria> getCategoriasDisponibles() {
        return repositorioCategorias.getTodas();
    }
    
    
    
    private final List<Alerta> alertas = new ArrayList<>();
    private final List<Notificacion> notificaciones = new ArrayList<>();

    public void crearAlerta(Alerta.Periodo periodo, double umbral, Categoria categoria) {
        alertas.add(new Alerta(periodo, umbral, categoria));
    }

    public List<Alerta> getAlertas() {
        return List.copyOf(alertas);
    }

    public List<Notificacion> getNotificaciones() {
        return List.copyOf(notificaciones);
    }

    public void marcarNotificacionLeida(Notificacion n) {
        n.marcarLeida();
    }
    
    
    private void comprobarAlertas(Cuenta cuenta, Movimiento gasto) {
        // Solo comprobamos si es un gasto
        if (gasto.getCategoria().getNombre().equalsIgnoreCase("Ingreso")) return;

        for (Alerta alerta : alertas) {
            // Lógica simplificada: ¿El gasto actual supera el umbral de la alerta?
            // (O puedes usar la lógica de suma semanal/mensual que te pasé antes)
            if (alerta.getCategoria() == null || alerta.getCategoria().equals(gasto.getCategoria())) {
                if (gasto.getCantidad() >= alerta.getUmbral()) {
                    String msg = "¡Alerta! Gasto de " + gasto.getCantidad() + "€ supera el límite de " + alerta.getUmbral() + "€";
                    Notificacion n = new Notificacion(msg);
                    notificaciones.add(n);
                    
                    // Avisar a la vista si está abierta
                    VentanaPrincipalController.mostrarNotificacion(msg);
                }
            }
        }
    }
}