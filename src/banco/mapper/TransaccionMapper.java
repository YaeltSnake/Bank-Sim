package banco.mapper;

import banco.dto.TransaccionAPIExternaDTO;
import banco.dto.TransaccionDTO;
import banco.dto.TransaccionDetalleDTO;
import banco.dto.TransaccionReporteDTO;
import banco.model.Transaccion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaccionMapper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TransaccionDTO toDTO(Transaccion t){
        return new TransaccionDTO(
                t.getMonto(),
                t.getTipo().name()
        );
    }

    public TransaccionReporteDTO toReporteDTO(Transaccion t){
        return new TransaccionReporteDTO(
                t.getMonto(),
                t.getTipo().name(),
                formatearFecha(t.getFecha())
        );
    }

    public TransaccionDetalleDTO toDetalleDTO(Transaccion t){
        String destino = t.getDestino() != null
                ? t.getDestino().toString()
                : "N/A";

        return new TransaccionDetalleDTO(
                t.getMonto(),
                t.getTipo().name(),
                t.getOrigen().toString(),
                destino
        );
    }

    public TransaccionAPIExternaDTO toAPI(Transaccion t){
        return new TransaccionAPIExternaDTO(
                t.getMonto(),
                t.getTipo().name()
        );
    }

    // Metodo para obtener fecha
    public String formatearFecha(LocalDateTime fecha){
        return fecha != null ? fecha.format(FORMATTER):"";
    }

}
