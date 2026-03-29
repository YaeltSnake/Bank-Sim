package banco.domain;

import java.util.Objects;

public class Usuario {
    private String nameUser;
    private String passUser;
    private int ageUser;
    private static int contador = 0;
    private final int ID_USER;

    public Usuario(String nameUser, String passUser, int ageUser) {
        if (ageUser < 18){
            throw new IllegalArgumentException("El usuario no puede ser menor de 18");
        }
        this.ID_USER = ++Usuario.contador;
        this.nameUser = nameUser;
        this.passUser = passUser;
        this.ageUser = ageUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(usuario.getID_USER(), ID_USER);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID_USER);
    }

    // Getters
    public String getNameUser() {
        return this.nameUser;
    }

    public void setNameUser(String nameUser){
        this.nameUser = nameUser;

    }

    public String getPassUser() {
        return this.passUser;
    }

    public void setPassUser(String passUser) {
        this.passUser = passUser;
    }

    public int getAgeUser() {
        return this.ageUser;
    }

    public int getID_USER() {
        return this.ID_USER;
    }
}
