
package pt.ipp.upt;

import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "utilizadores",
       uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_entidade")
@DiscriminatorValue("UTILIZADOR")
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String contacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUtilizador tipo;

    @Transient
    private ArrayList<Objeto> objetos = new ArrayList<>();

    protected Utilizador() {
    }

    public Utilizador(int id, String nome, String email,
                      String password, String contacto,
                      TipoUtilizador tipo) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.contacto = contacto;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getContacto() {
        return contacto;
    }

    public TipoUtilizador getTipo() {
        return tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // CRIAR CONTA
    public static void criarConta() {
        System.out.print("Nome: ");
        String nome = Main.sc.nextLine().trim();

        System.out.print("Email: ");
        String email = Main.sc.nextLine().trim();

        System.out.print("Password: ");
        String password = Main.sc.nextLine();

        System.out.print("Contacto: ");
        String contacto = Main.sc.nextLine().trim();

        if (nome.isEmpty() || email.isEmpty()
                || password.isEmpty()) {
            System.out.println("Preenche os campos obrigatórios.");
            return;
        }

        Utilizador novo = new Utilizador(
            0, nome, email, password, contacto,
            TipoUtilizador.UTILIZADOR
        );

        if (Tabelas.guardarUtilizador(novo)) {
            Main.utilizadores.add(novo);
            System.out.println("Conta criada com sucesso!");
        }
    }

    // INICIAR SESSÃO
    public static void iniciarSessao() {
        System.out.print("Email: ");
        String email = Main.sc.nextLine().trim();

        System.out.print("Password: ");
        String password = Main.sc.nextLine();

        Utilizador u = Tabelas.autenticarUtilizador(
            email, password
        );

        if (u != null) {
            Main.sessaoAtual = u;
            System.out.println("Bem-vindo, " + u.getNome() + "!");
        } else {
            System.out.println("Email ou password errados!");
        }
    }

    // TERMINAR SESSÃO
    public void terminarSessao() {
        Main.sessaoAtual = null;
        System.out.println("Sessão terminada.");
    }

    // EDITAR PERFIL
    public void editarPerfil() {
        System.out.print("Novo nome [" + nome + "]: ");
        String novoNome = Main.sc.nextLine().trim();

        System.out.print("Novo contacto [" + contacto + "]: ");
        String novoContacto = Main.sc.nextLine().trim();

        System.out.print("Nova password (Enter para manter): ");
        String novaPassword = Main.sc.nextLine();

        String nomeAntigo = nome;
        String contactoAntigo = contacto;
        String passwordAntiga = password;

        if (!novoNome.isEmpty()) {
            nome = novoNome;
        }
        if (!novoContacto.isEmpty()) {
            contacto = novoContacto;
        }
        if (!novaPassword.isEmpty()) {
            password = novaPassword;
        }

        if (Tabelas.atualizarUtilizador(this)) {
            System.out.println("Perfil atualizado!");
        } else {
            nome = nomeAntigo;
            contacto = contactoAntigo;
            password = passwordAntiga;
        }
    }

    // VISUALIZAR ANÚNCIOS
    public void visualizarAnuncios() {
        System.out.println("--- Todos os anúncios publicados ---");
        boolean encontrou = false;

        for (Objeto o : Main.objetos) {
            if (o.isEstado()) {
                o.consultarDetalhes();
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Não há anúncios publicados.");
        }
    }

    // REGISTAR OBJETO
    public void registarObjeto() {
        System.out.print("Tipo (1 = Encontrado, 2 = Perdido): ");
        int opcao = Main.lerInt();

        TipoObjeto tipoObjeto;

        if (opcao == 1) {
            tipoObjeto = TipoObjeto.ENCONTRADO;
        } else if (opcao == 2) {
            tipoObjeto = TipoObjeto.PERDIDO;
        } else {
            System.out.println("Tipo inválido.");
            return;
        }

        System.out.print("Título: ");
        String titulo = Main.sc.nextLine();

        System.out.print("Descrição: ");
        String descricao = Main.sc.nextLine();

        System.out.print("Marca: ");
        String nomeMarca = Main.sc.nextLine();

        System.out.print("Localização: ");
        String localizacao = Main.sc.nextLine();

        System.out.println("Categorias:");
        for (Categoria c : Tabelas.obterCategorias()) {
            System.out.println(c.getId() + " - " + c.getNome());
        }

        System.out.print("ID da categoria: ");
        int idCategoria = Main.lerInt();

        Categoria categoria = Main.procurarCategoria(idCategoria);

        if (categoria == null) {
            System.out.println("Categoria não existe.");
            return;
        }

        Marca marca = new Marca(
            Main.proximoIdMarca, nomeMarca
        );
        Main.proximoIdMarca++;

        Objeto novo = new Objeto(
            Main.proximoIdObjeto,
            titulo,
            descricao,
            marca,
            localizacao,
            categoria,
            tipoObjeto
        );

        Main.proximoIdObjeto++;

        Main.objetos.add(novo);
        objetos.add(novo);

        System.out.println(
            "Objeto registado com ID " + novo.getId()
        );
    }

    // EDITAR OBJETO
    public void editarObjeto() {
        Objeto o = escolherObjeto();
        if (o != null) {
            o.editar();
        }
    }

    // PESQUISAR OBJETO
    public void pesquisarObjeto() {
        System.out.print("Palavra a pesquisar: ");
        String palavra = Main.sc.nextLine().toLowerCase();

        int contador = 0;

        for (Objeto o : Main.objetos) {
            if (o.isEstado()
                    && o.getTextoParaPesquisa()
                        .toLowerCase().contains(palavra)) {
                o.consultarDetalhes();
                contador++;
            }
        }

        System.out.println(contador + " resultado(s).");
    }

    // CONSULTAR OBJETOS ENCONTRADOS
    public void consultarObjetosEncontrados() {
        System.out.println("--- Objetos encontrados ---");
        boolean encontrou = false;

        for (Objeto o : Main.objetos) {
            if (o.getTipo() == TipoObjeto.ENCONTRADO
                    && o.isEstado()) {
                o.consultarDetalhes();
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Não há objetos encontrados.");
        }
    }

    // ELIMINAR OBJETO
    public void eliminarObjeto() {
        Objeto o = escolherObjeto();

        if (o != null) {
            Objeto.eliminar(o);
            objetos.remove(o);
            System.out.println("Objeto eliminado!");
        }
    }

    // REMOVER OBJETO
    public void removerObjeto(Objeto o) {
        objetos.remove(o);
    }

    // ESCOLHER OBJETO
    private Objeto escolherObjeto() {
        if (objetos.isEmpty()) {
            System.out.println(
                "Ainda não registaste nenhum objeto."
            );
            return null;
        }

        System.out.println("Os teus objetos:");

        for (Objeto o : objetos) {
            System.out.println(
                o.getId() + " - " + o.getTitulo()
            );
        }

        System.out.print("ID do objeto: ");
        int id = Main.lerInt();

        for (Objeto o : objetos) {
            if (o.getId() == id) {
                return o;
            }
        }

        System.out.println(
            "Esse objeto não foi criado por ti ou não existe."
        );
        return null;
    }
}
