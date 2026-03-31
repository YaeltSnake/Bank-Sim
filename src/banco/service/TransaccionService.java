package banco.service;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import banco.exception.CuentaNoEncontradaException;
import banco.exception.MontoInvalidoException;
import banco.exception.SaldoInsuficienteException;
import banco.exception.UsuarioNoEncontradoException;
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

    public TransaccionService(CuentaRepository cuenta, UsuarioRepository usuario,TransaccionRepository trasaccion){
        this.cuentaRepository = cuenta;
        this.usuarioRepository = usuario;
        this.transaccionRepository = trasaccion;
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
        return obtenerTodas().stream()
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
        return obtenerTodas().stream()
                .sorted(Comparator.comparing(Transaccion::getFecha))
                .collect(Collectors.toList());
    }

    public List<Transaccion> ordenarTransaccionesPorFechaDescendente(){
        return obtenerTodas().stream()
                .sorted(Comparator.comparing(Transaccion::getFecha).
                        reversed()).collect(Collectors.toList());
    }

    public Set<Transaccion> obtenerTodas(){
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
        CuentaBancaria cuenta = cuentaRepository.findCuentaById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
        return cuenta;
    }

    public Usuario obtenerUsuario(int id){
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        return user;
    }

    public void validarSaldo(CuentaBancaria cuenta, double monto){
        if (cuenta.getSaldo() < monto){
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
    }

    // Getters
    public CuentaRepository getCuentaRepository() {
        return this.cuentaRepository;
    }

    public UsuarioRepository getUsuarioRepository() {
        return this.usuarioRepository;
    }

    public TransaccionRepository getTransaccionRepository() {
        return this.transaccionRepository;
    }
}
