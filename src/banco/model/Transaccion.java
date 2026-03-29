package banco.model;

import banco.domain.CuentaBancaria;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaccion {
    private static int contador = 0;

    private final int id;
    private final TipoTransaccion tipo;
    private final double monto;
    private final LocalDateTime fecha;
    private final CuentaBancaria origen;
    private final CuentaBancaria destino;

    // Validar transaccion, origen y montos

    public Transaccion(TipoTransaccion tipo, double monto,
                       CuentaBancaria origen, CuentaBancaria destino){
        if (tipo == null){
            throw new IllegalArgumentException("Tipo de transaccion no valida");
        }

        if (origen == null){
            throw new IllegalArgumentException("Cuenta de origen obligatoria");
        }
        if (monto <= 0){
            throw new IllegalArgumentException("Monto no valido");
        }
        this.id = ++Transaccion.contador;
        this.tipo = tipo;
        this.monto = monto;
        this.origen = origen;
        this.destino = destino;
        this.fecha = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", monto=" + monto +
                ", fecha=" + fecha +
                '}';
    }
    // Sobreescribir metodos equals/hashcode

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())return false;
        Transaccion transaccion = (Transaccion) o;
        return Objects.equals(transaccion.getId(), this.id);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.id);
    }


    // Getters
    public int getId() {
        return this.id;
    }

    public TipoTransaccion getTipo() {
        return this.tipo;
    }

    public double getMonto() {
        return this.monto;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public CuentaBancaria getOrigen() {
        return this.origen;
    }

    public CuentaBancaria getDestino() {
        return this.destino;
    }
}
