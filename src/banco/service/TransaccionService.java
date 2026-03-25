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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        if (monto <= 0){
            throw new MontoInvalidoException("Monto no valido");
        }
        CuentaBancaria cuenta = cuentaRepository
                .findCuentaById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));

        cuenta.depositarSaldo(monto);
        transaccionRepository.save(new Transaccion(TipoTransaccion.DEPOSITO, monto, cuenta, null));
    }

    public void retirar(int cuentaId, double monto){
        if (monto <= 0){
            throw new MontoInvalidoException("Monto no valido");
        }
        CuentaBancaria cuenta = cuentaRepository
                .findCuentaById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
        if(cuenta.getSaldo() < monto){
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }

        cuenta.retirarSaldo(monto);

        transaccionRepository.save(new Transaccion(TipoTransaccion.RETIRO, monto, cuenta, null));
    }

    public void transferir(int cuentaIdOrigen, int cuentaIdDestino, double monto){
        if (monto <= 0){
            throw new MontoInvalidoException("Monto no valido");
        }

        CuentaBancaria cuentaOrigen = cuentaRepository
                .findCuentaById(cuentaIdOrigen)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta origen no encontrada"));
        CuentaBancaria cuentaDestino = cuentaRepository
                .findCuentaById(cuentaIdDestino)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta destino no encontrada"));

        if (cuentaOrigen.getSaldo() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
        cuentaOrigen.retirarSaldo(monto);
        cuentaDestino.depositarSaldo(monto);
        transaccionRepository.save(new Transaccion(TipoTransaccion.TRANSFERENCIA, monto, cuentaOrigen, cuentaDestino));

    }

    public Set<Transaccion> obtenerHistorialPorCuenta(int cuentaId){
        CuentaBancaria cuenta = cuentaRepository
                .findCuentaById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
        // valdiamos cuenta, regresamos datos con cuenta
        return transaccionRepository.findByCuenta(cuenta);
    }

    public Set<Transaccion> obtenerHistorialPorUsuario(int usuarioId){
        Usuario user = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        return transaccionRepository.findByUser(user);
    }

    public Set<Transaccion> obtenerTransaccionesPorTipo(TipoTransaccion transaccion){
        Set<Transaccion> resultado = new HashSet<>();
        for (Transaccion t: transaccionRepository.findAll()){
            if (t.getTipo() == transaccion){
                resultado.add(t);
            }
        }
        return resultado;
    }
    public Set<Transaccion> obtenerTransaccionesPorTipoCuenta(TipoTransaccion tipo, int cuentaId){
        Set<Transaccion> resultado = new HashSet<>();
        CuentaBancaria cuenta = cuentaRepository.findCuentaById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));
        for (Transaccion t: transaccionRepository.findAll()){
            boolean esTipoCorrecto = t.getTipo() == tipo;
            boolean perteneceUsuario = t.getOrigen().equals(cuenta) || t.getDestino() != null && t.getDestino().equals(cuenta);

            if (esTipoCorrecto && perteneceUsuario){
                resultado.add(t);
            }
        }
        return resultado;
    }

    public Set<Transaccion> obtenerTransaccionesPorTipoUsuario(TipoTransaccion tipo, int usuarioId){
        Set<Transaccion> resultado = new HashSet<>();
        Usuario user = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        for (Transaccion t: transaccionRepository.findAll()){
            boolean esTipoCorrecto = t.getTipo() == tipo;
            boolean esOrigen = t.getOrigen().getUsuario().equals(user);
            boolean esDestino = t.getDestino() != null && t.getDestino().getUsuario().equals(user);
            boolean perteneceUsuario = esDestino || esDestino;
//            boolean pertenceUsuario = t.getOrigen().getUsuario().equals(user) ||
//                                      t.getDestino() != null && t.getDestino().getUsuario().equals(user);

            if (esTipoCorrecto && perteneceUsuario){
                resultado.add(t);
            }
        }
        return resultado;
    }

    public Set<Transaccion> obtenerTransaccionesMayoresA(double monto){
        Set<Transaccion> resultado = new HashSet<>();
        for (Transaccion t: transaccionRepository.findAll()){
            if (t.getMonto() > monto){
                resultado.add(t);
            }
        }
        return resultado;
    }

//    public Set<Transaccion> ordenarTransaccionesPorFecha(LocalDateTime fecha){
//
//    }



    // Getters
    public CuentaRepository getCuentaRepository() {
        return this.cuentaRepository;
    }

    public TransaccionRepository getTrasaccionRepository() {
        return this.transaccionRepository;
    }
}
