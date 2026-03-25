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

    public Set<Transaccion> obtenerHistorialPorUsuario(int idUser){
        Usuario usuario = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        return transaccionRepository.findByUser(usuario);
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

    // idea de codigo, crear transacciones por tipo y idUsuario
//    public Set<Transaccion> obtenerTransaccionesPorTipoUsuario(int usuarioId, TipoTransaccion tipo){
//        Set<Transaccion> resultado = new HashSet<>();
//        Usuario user = usuarioRepository
//                .findById(usuarioId)
//                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
//        for (Transaccion t: transaccionRepository.findAll()){
//            if (t.getTipo() == tipo &&
//                    t.getOrigen().getUsuario().equals(user) ||
//            ){
//                resultado.add(t);
//            }
//        }
//        return resultado;
//    }



    // Getters
    public CuentaRepository getCuentaRepository() {
        return this.cuentaRepository;
    }

    public TransaccionRepository getTrasaccionRepository() {
        return this.transaccionRepository;
    }
}
