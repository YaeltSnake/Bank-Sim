package banco.dto;

public class TransaccionDTO {
    private final double monto;
    private final String tipo;

    public TransaccionDTO(double monto, String tipo){
        this.monto = monto;
        this.tipo = tipo;
    }

    // Getters
    public double getMonto() {
        return this.monto;
    }

    public String getTipo() {
        return this.tipo;
    }
}
