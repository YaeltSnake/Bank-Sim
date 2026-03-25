package banco.aplication;
import banco.repository.UsuarioRepository;
import banco.repository.CuentaRepository;
import banco.service.ServicioCuenta;
import banco.service.ServicioUsuario;

public class SistemaBancario {
    private final ServicioUsuario servicioUsuario;
    private final ServicioCuenta servicioCuenta;


    public SistemaBancario(UsuarioRepository usuarioRepo, CuentaRepository cuentaRepo){
        this.servicioUsuario = new ServicioUsuario(usuarioRepo);
        this.servicioCuenta = new ServicioCuenta(cuentaRepo);
    }

    public ServicioUsuario getServicioUsuario() {
        return servicioUsuario;
    }

    public ServicioCuenta getServicioCuenta() {
        return servicioCuenta;
    }
}
