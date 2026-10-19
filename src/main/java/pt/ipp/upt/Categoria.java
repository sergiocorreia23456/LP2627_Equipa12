package pt.ipp.upt;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    // Lado inverso: quem guarda a chave estrangeira é a SubCategoria
    @OneToMany(mappedBy = "categoria")
    private List<SubCategoria> subcategorias = new ArrayList<>();

    public Categoria() {
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    // Usado nas categorias que ficam em memória (Main)
    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<SubCategoria> getSubcategorias() {
        return subcategorias;
    }

    // Mantém os dois lados da relação coerentes
    public void addSubCategoria(SubCategoria subcategoria) {
        subcategorias.add(subcategoria);
        subcategoria.setCategoria(this);
    }
}