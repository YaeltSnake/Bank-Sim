package banco.domain;


import java.util.Objects;

public class CuentaBancaria{
    private double saldo;
    private static int contador = 0;
    private final int ID_CUENTA;
    private Usuario usuario;

    public CuentaBancaria(double saldo, Usuario usuario) {
        if (saldo < 0){
            throw new IllegalArgumentException("No puedes crear cuentas con saldos menores a 0");
        }
        this.ID_CUENTA = ++CuentaBancaria.contador;
        this.saldo = saldo;
        this.usuario = usuario;
    }

    public double revisarSaldo(){
        return this.saldo;
    }

    public double depositarSaldo(double monto){
        if (monto < 0){
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0");
        }
        saldo += monto;
        return this.saldo;
    }

    public double retirarSaldo(double monto){
        if (monto <= 0) {
            throw new IllegalArgumentException("No puedes retirar montos menores o iguales a 0");
        }
        if (monto > saldo) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        this.saldo -= monto;
        return this.saldo;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CuentaBancaria cuenta = (CuentaBancaria) o;
        return Objects.equals(cuenta.getID_CUENTA(), ID_CUENTA);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID_CUENTA);
    }

    // Encapsulamiento de datos necesarios
    public double getSaldo() {
        return this.saldo;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public int getID_CUENTA(){
        return this.ID_CUENTA;
    }
}
