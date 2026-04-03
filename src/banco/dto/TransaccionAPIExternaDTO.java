package banco.dto;

public class TransaccionAPIExternaDTO {
    private final double monto;
    private final String tipo;

    public TransaccionAPIExternaDTO(double monto, String tipo){
        this.monto = monto;
        this.tipo = tipo;
    }

    // Getters
    public double getMonto() {
        return monto;
    }

    public String getTipo() {
        return tipo;
    }
}
