package banco.repository;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import java.util.List;
import java.util.Optional;

public interface CuentaRepository {

    void save(CuentaBancaria cuenta);

    Optional<CuentaBancaria> findCuentaByUser(Usuario usuario);

    Optional<CuentaBancaria> findCuentaById(int id);

    void delete(CuentaBancaria cuenta);

    List<CuentaBancaria> findAll();


}
