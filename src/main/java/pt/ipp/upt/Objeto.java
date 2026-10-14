package pt.ipp.upt;
import java.time.LocalDate;

public class Objeto {
    private int id;
    private String titulo;
    private String descricao;
    private Marca marca;
    private String localizacao;
    private LocalDate dataRegisto;
    private Categoria categoria;
    private boolean estado; // true = ativo, false = resolvido
    private TipoObjeto tipo;

    public Objeto(int id, String titulo, String descricao, Marca marca,
                  String localizacao, Categoria categoria, TipoObjeto tipo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.marca = marca;
        this.localizacao = localizacao;
        this.categoria = categoria;
        this.tipo = tipo;
        this.dataRegisto = LocalDate.now();
        this.estado = true;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isEstado() {
        return estado;
    }

    public TipoObjeto getTipo() {
        return tipo;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}