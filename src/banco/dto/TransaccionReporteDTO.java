package banco.dto;

public class TransaccionReporteDTO {
    private final double monto;
    private final String tipo;
    private final String fecha;

    public TransaccionReporteDTO(double monto, String tipo, String fecha){
        this.monto = monto;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    // Getters
    public double getMonto(){
        return this.monto;
    }
    public String getTipo(){
        return this.tipo;
    }
    public String getFecha(){
        return this.fecha;
    }
}
