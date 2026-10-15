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

    public void editarPerfil() {
            System.out.print("Novo nome [" + nome + "]: ");
            String texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                nome = texto;
            }

            System.out.print("Novo contacto [" + contacto + "]: ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                contacto = texto;
            }

            System.out.print("Nova password (Enter para manter): ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                password = texto;
            }

            System.out.println("Perfil atualizado!");
        }
    
    public void visualizarAnuncios() {
        System.out.println("--- Todos os anúncios ---");
        boolean encontrou = false;
        for (Objeto o : Main.objetos) {
            if (o.isEstado()) {
                o.consultarDetalhes();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Não há anúncios.");
        }
    }
}