package banco.repository;

import banco.domain.CuentaBancaria;
import banco.domain.Usuario;
import banco.model.Transaccion;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TransaccionRepositoryMemoria implements  TransaccionRepository{
    private Set<Transaccion> transaccions;

    public TransaccionRepositoryMemoria(){
        this.transaccions = new HashSet<>();
    }

    @Override
    public void save(Transaccion t) {
        transaccions.add(t);
    }

    @Override
    public void delete(Transaccion t) {
        try {
            transaccions.remove(t);
        }catch (Exception e){
            System.out.println("e = " + e);
        }
    }

    @Override
    public Optional<Transaccion> findById(int id) {
        for (Transaccion t: transaccions){
            if (t.getId() == id){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    @Override
    public Set<Transaccion> findByCuenta(CuentaBancaria cuenta) {
        Set<Transaccion> resultado = new HashSet<>();
        for (Transaccion t: transaccions){
            if (t.getOrigen().equals(cuenta) ||
                    (t.getDestino() != null && t.getDestino().equals(cuenta))){
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public Set<Transaccion> findByUser(Usuario user) {
        Set<Transaccion> resultado = new HashSet<>();
        for (Transaccion t: transaccions){
            if (t.getOrigen().getUsuario().equals(user) ||
                    (t.getDestino() != null && t.getDestino().getUsuario().equals(user))){
                resultado.add(t);
            }
        }
        return resultado;
    }

    @Override
    public Set<Transaccion> findAll() {
        return new HashSet<>(transaccions);
    }
}
