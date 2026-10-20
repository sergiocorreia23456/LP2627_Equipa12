package pt.ipp.upt;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {  // static pois o atributo pertence à classe, e não a cada objeto criado a partir dela.
	static Scanner sc = new Scanner(System.in);
	static Utilizador sessaoAtual = null;
    
	public static void main(String[] args) {
        //Administrador
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    	    Utilizador admin = session.createQuery(
    	        "FROM Utilizador WHERE email = :email", Utilizador.class)
    	        .setParameter("email", "admin@upt.pt")
    	        .uniqueResult();
    	    if (admin == null) {
    	        Transaction transaction = session.beginTransaction();
    	        try {
    	        	Utilizador novoAdmin = new Utilizador(
    	        		    0, "Administrador da UPT", "admin@upt.pt", "123",
    	        		    "999888777", TipoUtilizador.ADMINISTRADOR
    	        		);
    	            session.persist(novoAdmin);
    	            transaction.commit();
    	            System.out.println("Administrador criado com sucesso!");
    	        } catch (Exception e) {
    	            transaction.rollback();
    	            System.out.println("Erro ao criar administrador: " + e.getMessage());
    	        }
    	    }
    	}
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║    OBJETOS ACHADOS E PERDIDOS      ║");
        System.out.println("╚════════════════════════════════════╝");
        while (true) {
            if (sessaoAtual == null) {
                menuInicial();
            } else if (sessaoAtual.getTipo() == TipoUtilizador.ADMINISTRADOR) {
                menuAdministrador();
            } else {
                menuUtilizador();
            }
        }
    }
    // quando o sistema pede valores se for digitado letras ele volta a pedir denovo
    public static int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Número inválido, tenta outra vez: ");
            }
        }
    }

    public static Categoria procurarCategoria(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Categoria.class, id);
        }
    }

    // Título dos menus ( aspeto visual )
    public static void titulo(String texto) {
        System.out.println();
        System.out.println("── " + texto + " " + "─".repeat(Math.max(0, 34 - texto.length())));
    }

    public static void menuInicial() {
        titulo("MENU INICIAL");
        System.out.println("  1 - Criar conta");
        System.out.println("  2 - Iniciar sessão");
        System.out.println("  0 - Sair");
        System.out.print("Opção: ");
        int opcao = lerInt();
        if (opcao == 1) {
            GereUtilizadores.criarConta();
        } else if (opcao == 2) {
            GereUtilizadores.iniciarSessao();
        } else if (opcao == 0) {
            System.out.println("Até à próxima!");
            HibernateUtil.shutdown();
            System.exit(0);
        } else {
            System.out.println("Opção inválida.");
        }
    }

    public static void menuUtilizador() {
        titulo("Olá, " + sessaoAtual.getNome());
        System.out.println("  1 - Registar objeto");
        System.out.println("  2 - Editar objeto");
        System.out.println("  3 - Eliminar objeto");
        System.out.println("  4 - Pesquisar objeto");
        System.out.println("  5 - Ver objetos encontrados");
        System.out.println("  6 - Ver todos os anúncios");
        System.out.println("  7 - Editar perfil");
        System.out.println("  0 - Terminar sessão");
        System.out.print("Opção: ");
        int opcao = lerInt();
        if (opcao == 1) {
        	GereObjetos.registarObjeto(sessaoAtual);
        } else if (opcao == 2) {
        	GereObjetos.editarObjeto(sessaoAtual);
        } else if (opcao == 3) {
            GereObjetos.eliminarObjeto(sessaoAtual);
        } else if (opcao == 4) {
        	GereObjetos.pesquisarObjeto();
        } else if (opcao == 5) {
        	GereObjetos.consultarObjetosEncontrados();
        } else if (opcao == 6) {
        	GereObjetos.visualizarAnuncios();
        } else if (opcao == 7) {
        	GereUtilizadores.editarPerfil(sessaoAtual);
        } else if (opcao == 0) {
        	GereUtilizadores.terminarSessao();
        } else {
            System.out.println("Opção inválida.");
        }
    }

    public static void menuAdministrador() {
    	titulo("Administrador: " + sessaoAtual.getNome());
        System.out.println("  1 - Ver todos os objetos");
        System.out.println("  2 - Ver estatísticas");
        System.out.println("  3 - Eliminar anúncio");
        System.out.println("  4 - Gerir tabelas (marcas, categorias, subcategorias)");
        System.out.println("  0 - Terminar sessão");
        System.out.print("Opção: ");
        int opcao = lerInt();
        if (opcao == 1) {
        	GereObjetos.visualizarObjeto();
        } else if (opcao == 2) {
        	GereObjetos.consultarEstatisticas(sessaoAtual);
        } else if (opcao == 3) {
        	GereObjetos.eliminarAnuncio();
        } else if (opcao == 4) {
            Tabelas.menu();
        } else if (opcao == 0) {
        	GereUtilizadores.terminarSessao();
        } else {
            System.out.println("Opção inválida.");
        }
    }
}