package banco.aplication;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import banco.repository.CuentaRepository;
import banco.repository.CuentaRepositoryMemoria;
import banco.repository.UsuarioRepository;
import banco.repository.UsuarioRepositoryMemoria;

import java.util.Scanner;

public class MenuBanco {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // Instanciamos Sesion
        Sesion sesion = new Sesion();
        boolean salir = false;


        // Meter los prints en donde sea necesario que se devuelva algun tipo Double,
        // Revisar opcion por opcion
        UsuarioRepository usuarioRepo = new UsuarioRepositoryMemoria();
        CuentaRepository cuentaRepo = new CuentaRepositoryMemoria();
        SistemaBancario sistem = new SistemaBancario(usuarioRepo,cuentaRepo);

        System.out.println("*** BANCO LUX ***");
        do{
            mostrarMenu();
            int opcion = leerEntero(scan,"Elige una opcion: ");
                switch (opcion){
                    case 1 -> {
                        try {
                            System.out.print("User: ");
                            String nameUser = scan.nextLine();
                            System.out.print("Password: ");
                            String passUser = scan.nextLine();

                            Usuario usuarioActivo = sistem.getServicioUsuario().autenticarUsuario(nameUser,passUser);
                            CuentaBancaria cuentaActiva = sistem.getServicioCuenta().buscarCuentaUser(usuarioActivo);
                            sesion.iniciarSesion(usuarioActivo,cuentaActiva);
                            System.out.println("Bienvenido " + usuarioActivo.getNameUser());

                            menuSesionActiva(sesion, scan);

                        }catch (RuntimeException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.print("Ingresa el nombre del usuario: ");
                        String nameUser = scan.nextLine().toUpperCase().trim();
                        int ageUser = leerEntero(scan,"Ingresa tu edad: ");
                        System.out.print("Crea tu constraseña de usuario: ");
                        String passUser = scan.nextLine();
                        Usuario usuarioNuevo = sistem.getServicioUsuario().crearUsuario(nameUser, ageUser, passUser);
                        sistem.getServicioCuenta().asignarCuenta(usuarioNuevo);
                        System.out.println("Usuario creado con exito");
                        System.out.println("Bienvenido a tu banco de confianza " + usuarioNuevo.getNameUser());



                    }
                    case 3 -> {
                        System.out.println("Gracias por tu preferencia, Hasta luego");
                        salir = true;
                    }
                    default -> {
                        System.out.println("Opcion no valida");
                    }
                }


        }while (!salir);

    }
    // Metodos para leer int, double
    // Los try-catch de excepciones del programa para entrada de datos de tipo numerico, se puede manejar
    // en el main creando metodos estaticos
    private static int leerEntero(Scanner scan, String mensaje){
        while (true){
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scan.nextLine());
            }catch (Exception e){
                System.out.println("Entrada no valida, intenta de nuevo");
            }
        }
    }
    private static double leerDouble(Scanner scan, String mensaje){
        while (true){
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scan.nextLine());
            }
            catch (Exception e){
                System.out.println("Entrada no valida, intentalo de nuevo");
            }
        }
    }

    // Metodos de simplificacion del menu
    private static void mostrarMenu(){
        System.out.println("""
                    SELECCIONA UNA OPCION:
                    1: Inicio de Sesion
                    2: Creacion de cuenta
                    3: Salir""");
    }
    private static void menuSesionActiva(Sesion sesion, Scanner scan){
        boolean salir = false;
        if (sesion.isSesionActiva()){
            while (!salir){
                System.out.println("""
                OPCIONES DEL BANCOLUX:
                1: Revisar saldo disponible
                2: Depositar
                3: Retirar
                4: Cerrar sesión""");
                int opcion = leerEntero(scan, "Selecciona una opción: ");
                switch (opcion){
                    case 1 -> {
                        double saldo = sesion.getCuentaActiva().revisarSaldo();
                        System.out.println("Tu saldo des de: " + saldo);
                    }
                    case 2 -> {
                        double monto = leerDouble(scan, "Ingresa tu monto a depositar: ");
                        double saldo = sesion.getCuentaActiva().depositarSaldo(monto);
                        System.out.println("Tu nuevo saldo es de: " + saldo);
                    }
                    case 3 -> {
                        double monto = leerDouble(scan,"Ingresa el monto a retirar: ");
                        double saldo = sesion.getCuentaActiva().retirarSaldo(monto);
                        System.out.println("Tu nuevo saldo es de: " + saldo);

                    }
                    case 4 -> {
                        System.out.println("Cerrando sesion...\n" +
                                "Gracias por tu preferencia :D");
                        sesion.cerrarSesion();
                        salir = true;
                    }
                    default -> System.out.println("Opcion no valida");

                }
            }
        }
    }


}
