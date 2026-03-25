package banco.repository;
import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class CuentaRepositoryMemoria implements CuentaRepository{
    // Escribelo-visualizalo-crealo
    private final List<CuentaBancaria> cuentas;

    public CuentaRepositoryMemoria(){
        this.cuentas = new ArrayList<>();
    }

    @Override
    public void save(CuentaBancaria cuenta) {
        this.cuentas.add(cuenta);
    }

    @Override
    public Optional<CuentaBancaria> findCuentaByUser(Usuario usuario) {
        for (CuentaBancaria c : cuentas){
            if (c.getUsuario().equals(usuario)){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<CuentaBancaria> findCuentaById(int id) {
        for (CuentaBancaria c : cuentas){
            if (c.getID_CUENTA() == id){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(CuentaBancaria cuenta) {
        this.cuentas.remove(cuenta);
    }

    @Override
    public List<CuentaBancaria> findAll() {
        return new ArrayList<>(cuentas);
    }
}
