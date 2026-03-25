package banco.domain;

public class Usuario {
    private String nameUser;
    private String passUser;
    private int ageUser;
    private static int contador = 0;
    private final int idUser;

    public Usuario(String nameUser, String passUser, int ageUser) {
        if (ageUser < 18){
            throw new IllegalArgumentException("El usuario no puede ser menor de 18");
        }
        this.idUser = ++Usuario.contador;
        this.nameUser = nameUser;
        this.passUser = passUser;
        this.ageUser = ageUser;
    }


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

    public int getIdUser(){
        return this.idUser;
    }

}
