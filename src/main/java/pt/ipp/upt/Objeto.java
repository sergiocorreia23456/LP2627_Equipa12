
package pt.ipp.upt;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "objetos")
public class Objeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    private String localizacao;

    private LocalDate dataRegisto;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private boolean estado;

    @Enumerated(EnumType.STRING)
    private TipoObjeto tipo;

    // Construtor obrigatório para Hibernate
    protected Objeto() {
    }

    // Mantém o construtor utilizado no Main.java
    public Objeto(int id, String titulo, String descricao,
                  Marca marca, String localizacao,
                  Categoria categoria, TipoObjeto tipo) {

        // O ID é gerado automaticamente pelo MySQL
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

    public Marca getMarca() {
        return marca;
    }

    public void editar() {
        System.out.print("Novo título [" + titulo + "]: ");
        String texto = Main.sc.nextLine();

        if (!texto.isBlank()) {
            titulo = texto;
        }

        System.out.print("Nova descrição [" + descricao + "]: ");
        texto = Main.sc.nextLine();

        if (!texto.isBlank()) {
            descricao = texto;
        }

        System.out.print("Nova localização [" + localizacao + "]: ");
        texto = Main.sc.nextLine();

        if (!texto.isBlank()) {
            localizacao = texto;
        }

        System.out.print("Já foi resolvido? (sim/não): ");
        texto = Main.sc.nextLine();

        if (texto.equalsIgnoreCase("sim")) {
            estado = false;
        }

        if (Tabelas.atualizarObjeto(this)) {
            System.out.println("Objeto atualizado!");
        } else {
            System.out.println("Não foi possível atualizar o objeto.");
        }

        System.out.println("Objeto atualizado!");
    }

    public static void eliminar(Objeto o) {
        Tabelas.eliminarObjeto(o);
    }

    public void consultarDetalhes() {
        System.out.println("----------------------------");
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);

        System.out.println("Marca: " +
                (marca != null ? marca.getNome() : "Sem marca"));

        System.out.println("Categoria: " +
                (categoria != null ? categoria.getNome() : "Sem categoria"));

        System.out.println("Localização: " + localizacao);
        System.out.println("Data: " + dataRegisto);
        System.out.println("Estado: " +
                (estado ? "Ativo" : "Resolvido"));
    }

    public String getTextoParaPesquisa() {
        return titulo + " " + descricao + " "
                + (marca != null ? marca.getNome() : "") + " "
                + localizacao + " "
                + (categoria != null ? categoria.getNome() : "");
    }
}
