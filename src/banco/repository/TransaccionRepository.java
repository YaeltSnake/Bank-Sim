package banco.repository;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import banco.model.Transaccion;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface TransaccionRepository {

    void save(Transaccion t);

    void delete(Transaccion t);

    Optional<Transaccion> findById(int id);

    Set<Transaccion> findByCuenta(CuentaBancaria cuenta);

    Set<Transaccion> findByUser(Usuario user);

    Set<Transaccion> findAll();
}
