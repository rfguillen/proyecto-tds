package umu.tds.proyecto.adapters.repository.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import umu.tds.proyecto.adapters.repository.json.dto.*;
import umu.tds.proyecto.negocio.modelo.*;
import umu.tds.proyecto.adapters.repository.RepositorioCategorias;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonRepositorioUsuarios {

    private static final String FICHERO = System.getProperty("user.home") + "/gastos_datos.json";
    private final ObjectMapper mapper;

    public JsonRepositorioUsuarios() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // ── GUARDAR ──────────────────────────────────────────────

    public void guardar(Usuario usuario, List<Alerta> alertas, List<Notificacion> notificaciones,
                        RepositorioCategorias repoCategorias) {
        try {
            UsuarioDTO dto = toDTO(usuario, alertas, notificaciones);
            File fichero = new File(FICHERO);
            System.out.println("Guardando en: " + fichero.getAbsolutePath());
            mapper.writeValue(fichero, dto);
            System.out.println("Guardado OK");
        } catch (Exception e) {
            System.err.println("Error guardando datos: " + e.getMessage());
            e.printStackTrace(); // <-- esto es lo importante, muestra el error completo
        }
    }

    private UsuarioDTO toDTO(Usuario usuario, List<Alerta> alertas, List<Notificacion> notificaciones) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.nombre = usuario.getNombre();
        dto.cuentas = new ArrayList<>();

        for (Cuenta cuenta : usuario.getCuentas()) {
            dto.cuentas.add(cuentaToDTO(cuenta));
        }

        dto.alertas = new ArrayList<>();
        for (Alerta a : alertas) {
            AlertaDTO ad = new AlertaDTO();
            ad.periodo = a.getPeriodo().name();
            ad.umbral = a.getUmbral();
            ad.categoria = a.getCategoria() == null ? null : a.getCategoria().getNombre();
            dto.alertas.add(ad);
        }

        dto.notificaciones = new ArrayList<>();
        for (Notificacion n : notificaciones) {
            NotificacionDTO nd = new NotificacionDTO();
            nd.mensaje = n.getMensaje();
            nd.fecha = n.getFecha();
            nd.leida = n.isLeida();
            dto.notificaciones.add(nd);
        }

        return dto;
    }

    private CuentaDTO cuentaToDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.nombre = cuenta.getNombre();
        dto.movimientos = new ArrayList<>();

        if (cuenta instanceof CuentaCompartida cc) {
            dto.tipo = "compartida";
            dto.porcentajes = new LinkedHashMap<>();
            for (Participante p : cc.getParticipantes()) {
                dto.porcentajes.put(p.getNombre(), p.getPorcentajeParticipacion());
            }
        } else {
            dto.tipo = "personal";
        }

        for (Movimiento m : cuenta.getMovimientos()) {
            MovimientoDTO md = new MovimientoDTO();
            md.concepto = m.getConcepto();
            md.cantidad = m.getCantidad();
            md.fecha = m.getFecha();
            md.categoria = m.getCategoria().getNombre();

            if (m instanceof GastoCompartido gc) {
                md.tipo = "compartido";
                md.pagador = gc.getPagador().getNombre();
            } else {
                md.tipo = "simple";
            }
            dto.movimientos.add(md);
        }

        return dto;
    }

    // ── CARGAR ──────────────────────────────────────────────

    public Optional<DatosRecuperados> cargar(RepositorioCategorias repoCategorias) {
        File fichero = new File(FICHERO);
        if (!fichero.exists()) return Optional.empty();

        try {
            UsuarioDTO dto = mapper.readValue(fichero, UsuarioDTO.class);
            return Optional.of(fromDTO(dto, repoCategorias));
        } catch (IOException e) {
            System.err.println("Error cargando datos: " + e.getMessage());
            return Optional.empty();
        }
    }

    private DatosRecuperados fromDTO(UsuarioDTO dto, RepositorioCategorias repoCategorias) {
        Usuario usuario = new Usuario(dto.nombre);

        // La cuenta principal ya existe (la crea el constructor de Usuario)
        // Solo añadimos los movimientos a la que ya existe
        CuentaDTO principalDTO = dto.cuentas.stream()
                .filter(c -> c.tipo.equals("personal"))
                .findFirst().orElse(null);

        if (principalDTO != null) {
            for (MovimientoDTO md : principalDTO.movimientos) {
                Movimiento m = movimientoFromDTO(md, repoCategorias, null);
                usuario.getCuentaPrincipal().registrarMovimientoGasto(m);
            }
        }

        // Reconstruir cuentas compartidas
        for (CuentaDTO cd : dto.cuentas) {
            if (cd.tipo.equals("compartida")) {
                Cuenta cuenta = cuentaFromDTO(cd, repoCategorias);
                usuario.addCuenta(cuenta);
            }
        }

        // Reconstruir alertas
        List<Alerta> alertas = new ArrayList<>();
        if (dto.alertas != null) {
            for (AlertaDTO ad : dto.alertas) {
                Categoria cat = ad.categoria == null ? null : repoCategorias.buscar(ad.categoria);
                alertas.add(new Alerta(Alerta.Periodo.valueOf(ad.periodo), ad.umbral, cat));
            }
        }

        // Reconstruir notificaciones
        List<Notificacion> notificaciones = new ArrayList<>();
        if (dto.notificaciones != null) {
            for (NotificacionDTO nd : dto.notificaciones) {
                Notificacion n = new Notificacion(nd.mensaje, nd.fecha, nd.leida);
                notificaciones.add(n);
            }
        }

        return new DatosRecuperados(usuario, alertas, notificaciones);
    }

    private Cuenta cuentaFromDTO(CuentaDTO dto, RepositorioCategorias repoCategorias) {
        if (dto.tipo.equals("compartida")) {
            List<Participante> participantes = new ArrayList<>();
            for (Map.Entry<String, Double> entry : dto.porcentajes.entrySet()) {
                participantes.add(new Participante(entry.getKey(), entry.getValue()));
            }
            CuentaCompartida cc = new CuentaCompartida(dto.nombre, participantes);
            for (MovimientoDTO md : dto.movimientos) {
                Participante pagador = participantes.stream()
                        .filter(p -> p.getNombre().equals(md.pagador))
                        .findFirst().orElseThrow();
                Movimiento m = movimientoFromDTO(md, repoCategorias, pagador);
                cc.registrarMovimientoGasto(m);
            }
            return cc;
        } else {
            CuentaPersonal cp = new CuentaPersonal(dto.nombre);
            // movimientos de la personal se añaden después
            return cp;
        }
    }

    private Movimiento movimientoFromDTO(MovimientoDTO dto, RepositorioCategorias repoCategorias,
                                         Participante pagador) {
        Categoria cat = repoCategorias.buscar(dto.categoria);
        if ("compartido".equals(dto.tipo) && pagador != null) {
            return new GastoCompartido(dto.concepto, dto.cantidad, dto.fecha, cat, pagador);
        }
        return new Movimiento(dto.concepto, dto.cantidad, dto.fecha, cat);
    }

    // ── CLASE AUXILIAR ───────────────────────────────────────

    public static class DatosRecuperados {
        public final Usuario usuario;
        public final List<Alerta> alertas;
        public final List<Notificacion> notificaciones;

        public DatosRecuperados(Usuario usuario, List<Alerta> alertas, List<Notificacion> notificaciones) {
            this.usuario = usuario;
            this.alertas = alertas;
            this.notificaciones = notificaciones;
        }
    }
}