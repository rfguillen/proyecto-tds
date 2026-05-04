package umu.tds.proyecto.negocio.controladores;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

public class GestionGastos {

    private Usuario usuarioActual;
    private RepositorioCategorias repositorioCategorias;
    private RepositorioUsuarios repositorioUsuarios;
    private Importador importador;
    
    public GestionGastos() {        
        this.repositorioCategorias = new RepositorioCategorias();
        this.repositorioUsuarios = new RepositorioUsuarios();
        this.importador = new Importador();
        this.repositorioCategorias.añadirCategoriasPredefinidas();
        this.usuarioActual = new Usuario("Personal");
    }

    public static List<Movimiento> ordenarPorCategoria(Map<String, Movimiento> mapaGastos) {
        return mapaGastos.values().stream()
                .sorted(Comparator.comparing(m -> m.getCategoria().getNombre()))
                .collect(Collectors.toList());
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

        repositorioUsuarios.añadir(usuarioActual);
    }
    
    // ELIMINAR GASTO
    public void eliminarGasto(Cuenta cuenta, Movimiento gasto) {
        if (cuenta == null || gasto == null) throw new IllegalArgumentException("Datos no válidos");
        
        // El método de Cuenta ya elimina
        cuenta.eliminarMovimiento(gasto);
        
        repositorioUsuarios.añadir(usuarioActual);
    }
    
    public CuentaCompartida crearCuentaCompartida(String cuenta, List<Participante> participantes) {
        if (cuenta == null) throw new IllegalArgumentException("Nombre no valido");
        if (participantes == null) throw new IllegalArgumentException("Sin participantes");
        
        
        Set<String> nombres1 = new HashSet<>();
        for (Participante p : participantes) {
        	String nombres2 = p.getNombre().toLowerCase();
        	if (!nombres1.add(nombres2)) {
        		throw new IllegalArgumentException("Participante duplicado: " + p.getNombre());
        	}
        }
        
        
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
        List<CuentaGasto> items = importador.importar(fichero);

        Map<String, List<CuentaGasto>> porCuenta = items.stream()
                .collect(Collectors.groupingBy(cg -> cg.getNombreCuenta().trim()));

        crearCuentasCompartidasNecesarias(porCuenta);

        for (CuentaGasto cg : items) {
            String nombreCuenta = cg.getNombreCuenta().trim();
            Movimiento movimiento = cg.getGasto();

            Cuenta destino = obtenerCuentaDestino(nombreCuenta);

            if (destino instanceof CuentaCompartida cuentaCompartida) {
                if (!(movimiento instanceof GastoCompartido gastoCompartido)) {
                    throw new IllegalArgumentException(
                            "La cuenta " + nombreCuenta + " es compartida, pero el movimiento no tiene pagador"
                    );
                }

                Participante pagadorReal = buscarParticipanteEnCuenta(
                        cuentaCompartida,
                        gastoCompartido.getPagador().getNombre()
                );

                gastoCompartido.setPagador(pagadorReal);
                registrarGasto(gastoCompartido, cuentaCompartida);

            } else {
                registrarGasto(movimiento, destino);
            }
        }
    }
    
    private void crearCuentasCompartidasNecesarias(Map<String, List<CuentaGasto>> porCuenta) {
        for (Map.Entry<String, List<CuentaGasto>> entry : porCuenta.entrySet()) {
            String nombreCuenta = entry.getKey();

            if (esCuentaPersonal(nombreCuenta)) {
                continue;
            }

            Cuenta existente = buscarCuentaPorNombre(nombreCuenta);
            if (existente != null) {
                continue;
            }

            List<Participante> participantes = entry.getValue().stream()
                    .map(CuentaGasto::getGasto)
                    .filter(g -> g instanceof GastoCompartido)
                    .map(g -> (GastoCompartido) g)
                    .map(g -> g.getPagador().getNombre())
                    .distinct()
                    .map(nombre -> new Participante(nombre, 0.0))
                    .collect(Collectors.toList());

            if (participantes.isEmpty()) {
                throw new IllegalArgumentException(
                        "No se puede crear la cuenta compartida " + nombreCuenta + " sin participantes"
                );
            }

            crearCuentaCompartida(nombreCuenta, participantes);
        }
    }
    
    private Cuenta obtenerCuentaDestino(String nombreCuenta) {
        if (esCuentaPersonal(nombreCuenta)) {
            return getCuentaGlobal();
        }

        Cuenta cuenta = buscarCuentaPorNombre(nombreCuenta);

        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta destino no existe: " + nombreCuenta);
        }

        return cuenta;
    }
    
    private boolean esCuentaPersonal(String nombreCuenta) {
        return nombreCuenta.equalsIgnoreCase("Personal")
                || nombreCuenta.equalsIgnoreCase(usuarioActual.getCuentaPrincipal().getNombre());
    }
    
    private Cuenta buscarCuentaPorNombre(String nombreCuenta) {
        return usuarioActual.getCuentas().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombreCuenta))
                .findFirst()
                .orElse(null);
    }
    
    private Participante buscarParticipanteEnCuenta(CuentaCompartida cuenta, String nombreParticipante) {
        return cuenta.getParticipantes().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreParticipante))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "El participante " + nombreParticipante
                                + " no pertenece a la cuenta " + cuenta.getNombre()
                ));
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

    public void eliminarAlerta(Alerta alerta) {
        alertas.remove(alerta);
    }

    private void comprobarAlertas(Cuenta cuenta, Movimiento gasto) {
        List<Movimiento> movimientos = cuenta.getMovimientos().stream().toList();

        for (Alerta alerta : alertas) {
            if (alerta.estaSuperad(movimientos)) {
                String cat = alerta.getCategoria() == null ? "general" : alerta.getCategoria().getNombre();
                String msg = "¡Alerta! Límite " + alerta.getPeriodo().toString().toLowerCase()
                        + " de " + alerta.getUmbral() + "€ superado en categoría: " + cat;
                Notificacion n = new Notificacion(msg);
                notificaciones.add(n);
            }
        }
    }
}
