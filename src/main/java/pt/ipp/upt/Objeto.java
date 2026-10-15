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
    
    public void editar() {
            System.out.print("Novo título [" + titulo + "]: ");
            String texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                titulo = texto;
            }
            System.out.print("Nova descrição [" + descricao + "]: ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                descricao = texto;
            }
            System.out.print("Nova marca [" + marca.getNome() + "]: ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                marca = new Marca(Main.proximoIdMarca, texto);
                Main.proximoIdMarca++;
            }
            System.out.print("Nova localização [" + localizacao + "]: ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                localizacao = texto;
            }
            System.out.print("Já foi resolvido? (sim/não): ");
            texto = Main.sc.nextLine();
            if (texto.equals("sim")) {
                estado = false;
            }
            System.out.println("Objeto atualizado!");
        }
    
    public static void eliminar(Objeto o) {
        Main.objetos.remove(o);
    }
    
    public void consultarDetalhes() {
        System.out.println("----------------------------");
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Marca: " + marca.getNome());
        System.out.println("Categoria: " + categoria.getNome());
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
                + localizacao + " " + categoria.getNome();
    }
}