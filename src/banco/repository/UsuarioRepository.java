package banco.repository;

import banco.domain.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    // guardar,encontrarpor nombre,todos los usuarios, borrar

    void save(Usuario usuario);

    Usuario findByUserName(String nameUser);

    Optional<Usuario> findById(int idUser);

    List<Usuario> findAll();

    void delete(Usuario usuario);

}
