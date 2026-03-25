package banco.aplication;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;

public class Sesion {
    private Usuario usuario;
    private CuentaBancaria cuenta;

    public Sesion() {
    }

    public void iniciarSesion(Usuario usuarioActivo, CuentaBancaria cuentaActiva){
        if (usuarioActivo != null && cuentaActiva != null){
            this.usuario = usuarioActivo;
            this.cuenta = cuentaActiva;
        }else {
            throw new RuntimeException("Usuario o cuenta no validos");
        }
    }

    public Usuario getUsuarioActivo(){
        if (this.usuario != null){
            return this.usuario;
        }
        throw new IllegalArgumentException("Usuario no activo");
    }

    public CuentaBancaria getCuentaActiva(){
        if (this.cuenta != null){
            return this.cuenta;
        }
        throw new RuntimeException("Cuenta no activa");
    }

    public boolean isSesionActiva(){
        return this.usuario != null && this.cuenta != null;

    }

    public void cerrarSesion(){
        this.usuario = null;
        this.cuenta = null;
    }

}
