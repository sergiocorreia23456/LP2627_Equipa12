package pt.ipp.upt;

public class Categoria {
    private int id;
    private String nome;
    private Categoria categoriaPrincipal;

    public Categoria(int id, String nome, Categoria categoriaPrincipal) {
        this.id = id;
        this.nome = nome;
        this.categoriaPrincipal = categoriaPrincipal;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeCompleto() {
        if (categoriaPrincipal == null) {
            return nome;
        }
        return categoriaPrincipal.getNome() + " > " + nome;
    }
}