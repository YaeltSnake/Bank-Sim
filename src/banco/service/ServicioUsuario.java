package banco.service;
import banco.domain.Usuario;
import banco.repository.UsuarioRepository;

public class ServicioUsuario {
    private final UsuarioRepository usuarios;

    public ServicioUsuario(UsuarioRepository usuarioRepository){
        this.usuarios = usuarioRepository;
    }


    public Usuario crearUsuario(String nombre, int edad, String passUser){
        if (usuarios.findByUserName(nombre) != null){
            throw new RuntimeException("Nombre de Usuario ya ocupado");
        }
        Usuario u = new Usuario(nombre,passUser,edad);
        this.usuarios.save(u);
        return u;
//        for (Usuario u: usuarios.findAll()){
//            if (u.getNameUser().equals(nombre)){
//                throw new RuntimeException("User ya ocupado");
//            }
//        }
//        Usuario u = new Usuario(nombre,passUser,edad);
//        this.usuarios.save(u);
//        return u;
    }

    public Usuario autenticarUsuario(String nombre, String passUser){
        for (Usuario u : usuarios.findAll()){
            if (u.getNameUser().equals(nombre) && u.getPassUser().equals(passUser)){
                return u;
            }
        }
        throw new RuntimeException("Algun dato es incorrecto");
    }

    public void eliminarUsuario(Usuario usuario){
        usuarios.delete(usuario);
    }

}
