package banco.repository;

import banco.domain.Usuario;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class UsuarioRepositoryMemoria implements UsuarioRepository{
    private final List<Usuario> usuarios;

    public UsuarioRepositoryMemoria() {
        this.usuarios = new ArrayList<>();
    }

    @Override
    public void save(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public Usuario findByUserName(String nameUser) {
        for (Usuario u : usuarios){
            if (u.getNameUser().equals(nameUser)){
                return u;
            }
        }
        return null;
    }

    @Override
    public Optional<Usuario> findById(int idUser) {
        for (Usuario u: usuarios){
            if (u.getIdUser() == idUser){
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public void delete(Usuario usuario) {
        usuarios.remove(usuario);
    }
}
