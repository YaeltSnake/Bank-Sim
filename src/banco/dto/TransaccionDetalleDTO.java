package banco.dto;

public class TransaccionDetalleDTO {
    private final double monto;
    private final String tipo;
    private final String cuentaOrigen;
    private final String cuentaDestino;

    public TransaccionDetalleDTO(double monto, String tipo, String cuentaOrigen, String cuentaDestino){
        this.monto = monto;
        this.tipo = tipo;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

    // Getters
    public double getMonto() {
        return this.monto;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getCuentaOrigen() {
        return this.cuentaOrigen;
    }

    public String getCuentaDestino() {
        return this.cuentaDestino;
    }
}
