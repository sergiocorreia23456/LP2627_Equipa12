package pt.ipp.upt;
import java.util.ArrayList;

public class Utilizador {
    private int id;
    private String nome;
    private String email;
    private String password;
    private String contacto;
    private TipoUtilizador tipo;
    private ArrayList<Objeto> objetos = new ArrayList<>();

    public Utilizador(int id, String nome, String email, String password,
                      String contacto, TipoUtilizador tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.contacto = contacto;
        this.tipo = tipo;
    }
    public String getNome() {
        return nome;
    }
    public TipoUtilizador getTipo() {
        return tipo;
    }
}