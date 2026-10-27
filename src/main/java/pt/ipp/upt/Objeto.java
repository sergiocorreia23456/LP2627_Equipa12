package pt.ipp.upt;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;

@Entity
@Table(name = "Objeto")
public class Objeto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String titulo;

	@Column(length = 1000)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "marca_id")
	private Marca marca;

	private String localizacao;

	private LocalDate dataRegisto;
	
	@ManyToOne
	@JoinColumn(name = "subcategoria_id")
	private SubCategoria subcategoria;

	private boolean estado;

	@Enumerated(EnumType.STRING)
	private TipoObjeto tipo;

	@ManyToOne
	@JoinColumn(name = "utilizador_id")
	private Utilizador utilizador;
	
	protected Objeto() {
	}

    public Objeto(int id, String titulo, String descricao, Marca marca,
                  String localizacao, SubCategoria subcategoria, TipoObjeto tipo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.marca = marca;
        this.localizacao = localizacao;
        this.subcategoria = subcategoria;
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
        if (subcategoria == null) {
            return null;
        }
        return subcategoria.getCategoria();
    }
    
    public SubCategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubCategoria subcategoria) {
        this.subcategoria = subcategoria;
    }
    
    public Utilizador getUtilizador() {
        return utilizador;
    }
    
    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }
    
    public void setTitulo(String titulo) {
    	this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
    	this.descricao = descricao;
    }

    public void setLocalizacao(String localizacao) {
    	this.localizacao = localizacao;
    }

    public void setEstado(boolean estado) {
    	this.estado = estado;
    }
    
    public void consultarDetalhes() {
        System.out.println("----------------------------");
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Marca: " + marca.getNome());
        if (getCategoria() != null) {
            System.out.println("Categoria: " + getCategoria().getNome());
        } else {
            System.out.println("Categoria: Não definida");
        }
        if (subcategoria != null) {
            System.out.println("Subcategoria: " + subcategoria.getNome());
        } else {
            System.out.println("Subcategoria: Não definida");
        }
        System.out.println("Localização: " + localizacao);
        System.out.println("Data: " + dataRegisto);
        if (estado) {
            System.out.println("Estado: Ativo");
        } else {
            System.out.println("Estado: Resolvido");
        }
    }
    
    // Usado na pesquisa: junta todo o texto do objeto numa só String
    public String getTextoParaPesquisa() {
        return titulo + " " + descricao + " " + marca.getNome() + " "
                + localizacao + " "
                + (getCategoria() != null ? getCategoria().getNome() : "");
    }
}