package banco.service;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import banco.dto.TransaccionAPIExternaDTO;
import banco.dto.TransaccionDTO;
import banco.dto.TransaccionDetalleDTO;
import banco.dto.TransaccionReporteDTO;
import banco.exception.CuentaNoEncontradaException;
import banco.exception.MontoInvalidoException;
import banco.exception.SaldoInsuficienteException;
import banco.exception.UsuarioNoEncontradoException;
import banco.mapper.TransaccionMapper;
import banco.model.TipoTransaccion;
import banco.model.Transaccion;
import banco.repository.CuentaRepository;
import banco.repository.TransaccionRepository;
import banco.repository.UsuarioRepository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class TransaccionService {
    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TransaccionRepository transaccionRepository;

    private final TransaccionMapper mapper;

    public TransaccionService(CuentaRepository cuenta, UsuarioRepository usuario,TransaccionRepository trasaccion){
        this.cuentaRepository = cuenta;
        this.usuarioRepository = usuario;
        this.transaccionRepository = trasaccion;
        this.mapper = new TransaccionMapper();
    }

    public void depositar(int cuentaId, double monto) {
        validarMonto(monto);
        CuentaBancaria cuenta = obtenerCuenta(cuentaId);
        cuenta.depositarSaldo(monto);
        transaccionRepository.save(new Transaccion(TipoTransaccion.DEPOSITO, monto, cuenta, null));
    }

    public void retirar(int cuentaId, double monto){
        validarMonto(monto);
        CuentaBancaria cuenta = obtenerCuenta(cuentaId);
        validarSaldo(cuenta,monto);
        cuenta.retirarSaldo(monto);
        transaccionRepository.save(new Transaccion(TipoTransaccion.RETIRO, monto, cuenta, null));
    }

    public void transferir(int cuentaIdOrigen, int cuentaIdDestino, double monto){
        validarMonto(monto);
        CuentaBancaria cuentaOrigen = obtenerCuenta(cuentaIdOrigen);
        CuentaBancaria cuentaDestino = obtenerCuenta(cuentaIdDestino);
        validarSaldo(cuentaOrigen, monto);
        cuentaOrigen.retirarSaldo(monto);
        cuentaDestino.depositarSaldo(monto);
        transaccionRepository.save(new Transaccion(TipoTransaccion.TRANSFERENCIA, monto, cuentaOrigen, cuentaDestino));
    }

    public Set<Transaccion> obtenerHistorialPorCuenta(int cuentaId){
        CuentaBancaria cuenta = obtenerCuenta(cuentaId);
        // valdiamos cuenta, regresamos datos con cuenta
        return transaccionRepository.findByCuenta(cuenta);
    }

    public Set<Transaccion> obtenerHistorialPorUsuario(int usuarioId){
        Usuario user = obtenerUsuario(usuarioId);
        return transaccionRepository.findByUser(user);
    }

    public List<Transaccion> obtenerTransaccionesPor(Predicate<Transaccion> condicion){
        return findAll().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }
    //"refactor: introduce Predicate-based filtering and improve stream usage in TransaccionService"

    public List<Transaccion> obtenerTransaccionesPorTipo(TipoTransaccion tipo){
        return obtenerTransaccionesPor(t -> t.getTipo() == tipo);
    }

    public List<Transaccion> obtenerTransaccionesPorTipoCuenta(TipoTransaccion tipo, int cuentaId){
        CuentaBancaria cuenta = obtenerCuenta(cuentaId);
        return obtenerTransaccionesPor(t -> {
            boolean esTipo = t.getTipo() == tipo;
            boolean esOrigen = t.getOrigen().equals(cuenta);
            boolean esDestino = t.getDestino() != null && t.getDestino().equals(cuenta);
            boolean perteneceUsuario = esOrigen || esDestino;
            return esTipo && perteneceUsuario;
        });
    }

    public List<Transaccion> obtenerTransaccionesPorTipoUsuario(TipoTransaccion tipo, int usuarioId){
        Usuario user = obtenerUsuario(usuarioId);
        return obtenerTransaccionesPor(t -> {
            boolean esTipoCorrecto = t.getTipo() == tipo;
            boolean esOrigen = t.getOrigen().getUsuario().equals(user);
            boolean esDestino = t.getDestino() != null && t.getDestino().getUsuario().equals(user);
            boolean perteneceUsuario = esOrigen || esDestino;
            return esTipoCorrecto && perteneceUsuario;
        });
    }

    public List<Transaccion> obtenerTransaccionesMayoresA(double monto){
        return obtenerTransaccionesPor(t -> t.getMonto() > monto);
    }

    public List<Transaccion> ordenarTransaccionesPorFechaAscendente(){
        return findAll().stream()
                .sorted(Comparator.comparing(Transaccion::getFecha))
                .collect(Collectors.toList());
    }

    public List<Transaccion> ordenarTransaccionesPorFechaDescendente(){
        return findAll().stream()
                .sorted(Comparator.comparing(Transaccion::getFecha).
                        reversed()).collect(Collectors.toList());
    }

    // Implementacion DTO
    public List<TransaccionDTO> getTransaccionesDTO(){
        return findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<TransaccionDTO> getTransaccionesFiltradasDTO(Predicate<Transaccion> condicion){
        return findAll().stream()
                .filter(condicion)
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<TransaccionDTO> getDepositosMayoresA(double monto){
        return findAll().stream()
                .filter(t -> t.getMonto() > monto && t.getTipo() == TipoTransaccion.DEPOSITO)
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Implementacion DetalleDTO
    public List<TransaccionDetalleDTO> getTransaccionesDetalleDTO(){
        return findAll().stream()
                .map(mapper::toDetalleDTO).collect(Collectors.toList());
    }

    public List<TransaccionDetalleDTO> getTransaccionesDetalleFiltradasDTO(Predicate<Transaccion> condicion){
        return findAll().stream()
                .filter(condicion).map(mapper::toDetalleDTO).collect(Collectors.toList());
    }

    // Implementacion ReporteDTO
    public List<TransaccionReporteDTO> getReporteTransacciones(){
        return findAll().stream()
                .map(mapper::toReporteDTO)
                .collect(Collectors.toList());
    }

    public List<TransaccionReporteDTO> getReporteTransaccionesFiltradas(Predicate<Transaccion> condicion){
        return findAll().stream().filter(condicion)
                .map(mapper::toReporteDTO)
                .collect(Collectors.toList());
    }

    // Implementacion APIExternaDTO
    public List<TransaccionAPIExternaDTO> getTransaccionesAPI(){
        return findAll().stream()
                .map(mapper::toAPI)
                .collect(Collectors.toList());
    }

    public List<TransaccionAPIExternaDTO> getTransaccionesAPIFiltradas(Predicate<Transaccion> condicion){
        return findAll().stream()
                .filter(condicion)
                .map(mapper::toAPI)
                .collect(Collectors.toList());
    }

    // Metodo aplicado en FrontEnd
    public List<String> getTiposTransacciones(){
        return findAll().stream()
                .map(t -> t.getTipo().name())
                .distinct().collect(Collectors.toList());
    }

    public Set<Transaccion> findAll(){
        return transaccionRepository.findAll();
    }

    public void validarMonto(double monto){
        if (monto <= 0){
            throw new MontoInvalidoException("Monto invalido");
        }

    }

    // hubo un pequeño cambio en main
    // sobreescribir los metodos de CuentaBancaria, Transaccion
    // del cambio del main, optimizar la obtencion de datos de Usuario

    public CuentaBancaria obtenerCuenta(int id){
        return cuentaRepository.findCuentaById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
    }

    public Usuario obtenerUsuario(int id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
    }

    public void validarSaldo(CuentaBancaria cuenta, double monto){
        if (cuenta.getSaldo() < monto){
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
    }
}
