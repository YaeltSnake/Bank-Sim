package banco.aplication;

import banco.dto.TransaccionDTO;
import banco.repository.CuentaRepository;
import banco.repository.TransaccionRepository;
import banco.repository.UsuarioRepository;
import banco.service.TransaccionService;

import java.util.List;

public class TransaccionController {
    private final TransaccionService transaccionService;

    public TransaccionController(CuentaRepository cuentaRepository,
                                 UsuarioRepository usuarioRepository,
                                 TransaccionRepository transaccionRepository){
        this.transaccionService = new TransaccionService(cuentaRepository,
                usuarioRepository, transaccionRepository);
    }

    public List<TransaccionDTO> obtenerTransacciones(){
        return transaccionService.getTransaccionesDTO();
    }

}
