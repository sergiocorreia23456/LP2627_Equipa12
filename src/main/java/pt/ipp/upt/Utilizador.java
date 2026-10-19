package pt.ipp.upt;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name = "Utilizador")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Utilizador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, length = 100)
	private String nome;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 20)
	private String contacto;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoUtilizador tipo;

	@jakarta.persistence.Transient
	private ArrayList<Objeto> objetos = new ArrayList<>();
	
	protected Utilizador() {
	}

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
    
    public static void criarConta() {
        System.out.print("Nome: ");
        String nome = Main.sc.nextLine();
        System.out.print("Email: ");
        String email = Main.sc.nextLine();
        System.out.print("Password: ");
        String password = Main.sc.nextLine();
        System.out.print("Contacto: ");
        String contacto = Main.sc.nextLine();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Utilizador existente = session.createQuery(
                "FROM Utilizador WHERE email = :email", Utilizador.class)
                .setParameter("email", email)
                .uniqueResult();
            if (existente != null) {
                System.out.println("Esse email já existe!");
                return;
            }
            Utilizador novo = new Utilizador(
                0, nome, email, password, contacto, TipoUtilizador.UTILIZADOR
            );
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(novo);
                transaction.commit();
                System.out.println("Conta criada com sucesso!");
            } catch (Exception e) {
                transaction.rollback();
                System.out.println("Erro ao criar conta: " + e.getMessage());
            }
        }
    }

    public static void iniciarSessao() {
        System.out.print("Email: ");
        String email = Main.sc.nextLine();
        System.out.print("Password: ");
        String password = Main.sc.nextLine();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Utilizador u = session.createQuery(
                "FROM Utilizador WHERE email = :email", Utilizador.class)
                .setParameter("email", email)
                .uniqueResult();
            if (u != null && u.password.equals(password)) {
                Main.sessaoAtual = u;
                System.out.println("Bem-vindo, " + u.nome + "!");
            } else {
                System.out.println("Email ou password errados!");
            }

        } catch (Exception e) {
            System.out.println("Erro ao iniciar sessão: " + e.getMessage());
        }
    }

    public void terminarSessao() {
        Main.sessaoAtual = null;
        System.out.println("Sessão terminada.");
    }
    
    public void editarPerfil() {
        System.out.print("Novo nome [" + nome + "]: ");
        String texto = Main.sc.nextLine();
        if (!texto.isEmpty()) {
            nome = texto;
        }
        System.out.print("Novo contacto [" + contacto + "]: ");
        texto = Main.sc.nextLine();
        if (!texto.isEmpty()) {
            contacto = texto;
        }
        System.out.print("Nova password (Enter para manter): ");
        texto = Main.sc.nextLine();
        if (!texto.isEmpty()) {
            password = texto;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(this);
            transaction.commit();
            System.out.println("Perfil atualizado com sucesso!");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Erro ao atualizar perfil: " + e.getMessage());
        }
    }
    
    public void visualizarAnuncios() {
        System.out.println("--- Todos os anúncios publicados ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto WHERE estado = true", Objeto.class
                ).getResultList()
            );
            if (objetos.isEmpty()) {
                System.out.println("Não há anúncios publicados.");
                return;
            }
            for (Objeto o : objetos) {
                o.consultarDetalhes();
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar anúncios: " + e.getMessage());
        }
    }

    public void registarObjeto() {
        System.out.print("Tipo (1 = Encontrado, 2 = Perdido): ");
        int opcao = Main.lerInt();
        if (opcao != 1 && opcao != 2) {
            System.out.println("Tipo inválido.");
            return;
        }
        TipoObjeto tipoObjeto = (opcao == 1)
                ? TipoObjeto.ENCONTRADO
                : TipoObjeto.PERDIDO;
        System.out.print("Título: ");
        String titulo = Main.sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = Main.sc.nextLine();
        System.out.print("Localização: ");
        String localizacao = Main.sc.nextLine();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Mostrar as marcas existentes
            System.out.println("Marcas:");
            for (Marca m : session.createQuery("FROM Marca", Marca.class).getResultList()) {
                System.out.println(m.getId() + " - " + m.getNome());
            }
            System.out.print("ID da marca: ");
            int idMarca = Main.lerInt();
            Marca marca = session.get(Marca.class, idMarca);
            if (marca == null) {
                System.out.println("Marca não existe.");
                return;
            }
            // Mostrar as categorias existentes
            System.out.println("Categorias:");
            for (Categoria c : session.createQuery("FROM Categoria", Categoria.class).getResultList()) {
                System.out.println(c.getId() + " - " + c.getNome());
            }
            System.out.print("ID da categoria: ");
            int idCategoria = Main.lerInt();
            Categoria categoria = session.get(Categoria.class, idCategoria);
            if (categoria == null) {
                System.out.println("Categoria não existe.");
                return;
            }
            Objeto novo = new Objeto(
                    0, titulo, descricao, marca,
                    localizacao, categoria, tipoObjeto
            );
            novo.setUtilizador(this);
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(novo);
                transaction.commit();
                System.out.println("Objeto registado com ID " + novo.getId());
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.out.println("Erro ao registar objeto: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Erro ao aceder à base de dados: " + e.getMessage());
        }
    }

    public void editarObjeto() {
        Objeto o = escolherObjeto();
        if (o != null) {
            o.editar();
        }
    }
    
    public void pesquisarObjeto() {
        System.out.print("Palavra a pesquisar: ");
        String palavra = Main.sc.nextLine().toLowerCase();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto o WHERE o.estado = true AND " +
                    "(LOWER(o.titulo) LIKE :palavra OR " +
                    "LOWER(o.descricao) LIKE :palavra OR " +
                    "LOWER(o.localizacao) LIKE :palavra OR " +
                    "LOWER(o.marca.nome) LIKE :palavra OR " +
                    "LOWER(o.categoria.nome) LIKE :palavra)",
                    Objeto.class
                )
                .setParameter("palavra", "%" + palavra + "%")
                .getResultList()
            );
            for (Objeto o : objetos) {
                o.consultarDetalhes();
            }
            System.out.println(objetos.size() + " resultado(s).");
        } catch (Exception e) {
            System.out.println("Erro ao pesquisar objetos: " + e.getMessage());
        }
    }
    
    public void consultarObjetosEncontrados() {
        System.out.println("--- Objetos encontrados ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto WHERE tipo = :tipo AND estado = true",
                    Objeto.class
                )
                .setParameter("tipo", TipoObjeto.ENCONTRADO)
                .getResultList()
            );
            if (objetos.isEmpty()) {
                System.out.println("Não há objetos encontrados.");
                return;
            }
            for (Objeto o : objetos) {
                o.consultarDetalhes();
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar objetos encontrados: " + e.getMessage());
        }
    }
    // o utilizador elimina o objeto
    public void eliminarObjeto() {
        Objeto o = escolherObjeto();
        if (o != null) {
            Objeto.eliminar(o);
            objetos.remove(o);
            System.out.println("Objeto eliminado!");
        }
    }
    // o admin remove o objeto se tiver informação incorreta
    public void removerObjeto(Objeto o) {
        objetos.remove(o);
    }
    
 // Mostra os objetos do utilizador e pede o ID
    private Objeto escolherObjeto() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto o WHERE o.utilizador.id = :id",
                    Objeto.class
                )
                .setParameter("id", this.id)
                .getResultList()
            );
            if (objetos.isEmpty()) {
                System.out.println("Ainda não registaste nenhum objeto.");
                return null;
            }
            System.out.println("Os teus objetos:");
            for (Objeto o : objetos) {
                System.out.println(o.getId() + " - " + o.getTitulo());
            }
            System.out.print("ID do objeto: ");
            int idObjeto = Main.lerInt();
            for (Objeto o : objetos) {
                if (o.getId() == idObjeto) {
                    return o;
                }
            }
            System.out.println("Esse objeto não foi criado por ti ou não existe.");
        } catch (Exception e) {
            System.out.println("Erro ao consultar objetos: " + e.getMessage());
        }
        return null;
    }
}