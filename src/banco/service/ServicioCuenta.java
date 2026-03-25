package banco.service;
import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import banco.repository.CuentaRepository;
public class ServicioCuenta {
    private final CuentaRepository cuentas;


    public ServicioCuenta(CuentaRepository cuentaRepository){
        this.cuentas = cuentaRepository;
    }

    public CuentaBancaria asignarCuenta(Usuario usuario){
        if (usuario != null){
            CuentaBancaria c = new CuentaBancaria(0.0, usuario);
            this.cuentas.save(c);
            return c;
        }
        throw new RuntimeException("Usuario no valido");
    }

    public CuentaBancaria buscarCuentaUser(Usuario usuario){
        if (cuentas.findCuentaByUser(usuario) != null){
            throw new RuntimeException("Usuario con cuenta no asiganda");
        }
        return cuentas.findCuentaByUser(usuario);
    }

    public void eliminarCuenta(CuentaBancaria c){
        this.cuentas.delete(c);
    }
}
