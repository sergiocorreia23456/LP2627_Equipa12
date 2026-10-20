package pt.ipp.upt;

import jakarta.persistence.Entity;

@Entity
public class Administrador extends Utilizador {
	
	protected Administrador() {
	    super();
	}

    public Administrador(int id, String nome, String email, String password, String contacto) {
        super(id, nome, email, password, contacto, TipoUtilizador.ADMINISTRADOR);
    }
}