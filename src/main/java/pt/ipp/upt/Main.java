package pt.ipp.upt;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {  // static pois o atributo pertence à classe, e não a cada objeto criado a partir dela.
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Utilizador> utilizadores = new ArrayList<>();
    static ArrayList<Objeto> objetos = new ArrayList<>();
    static ArrayList<Categoria> categorias = new ArrayList<>();
    static Utilizador sessaoAtual = null; // quem tem a sessão iniciada
    static int proximoIdUtilizador = 2;
    static int proximoIdObjeto = 1;
    static int proximoIdMarca = 1;

    public static void main(String[] args) {
        // Categorias iniciais
        categorias.add(new Categoria(1, "Eletrónica"));
        categorias.add(new Categoria(2, "Roupa"));
        categorias.add(new Categoria(3, "Documentos"));
        categorias.add(new Categoria(4, "Chaves"));
        //Administrador
        utilizadores.add(new Administrador(1, "Admin", "admin@upt.pt", "123", "999888777"));
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
        for (Categoria c : categorias) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    // Título dos menus
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
            Utilizador.criarConta();
        } else if (opcao == 2) {
            Utilizador.iniciarSessao();
        } else if (opcao == 0) {
            System.out.println("Até à próxima!");
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
            sessaoAtual.registarObjeto();
        } else if (opcao == 2) {
            sessaoAtual.editarObjeto();
        } else if (opcao == 3) {
            sessaoAtual.eliminarObjeto();
        } else if (opcao == 4) {
            sessaoAtual.pesquisarObjeto();
        } else if (opcao == 5) {
            sessaoAtual.consultarObjetosEncontrados();
        } else if (opcao == 6) {
            sessaoAtual.visualizarAnuncios();
        } else if (opcao == 7) {
            sessaoAtual.editarPerfil();
        } else if (opcao == 0) {
            sessaoAtual.terminarSessao();
        } else {
            System.out.println("Opção inválida.");
        }
    }

    public static void menuAdministrador() {
        Administrador admin = (Administrador) sessaoAtual;
        titulo("Administrador: " + admin.getNome());
        System.out.println("  1 - Ver todos os objetos");
        System.out.println("  2 - Ver estatísticas");
        System.out.println("  3 - Eliminar anúncio");
        System.out.println("  4 - Gerir tabelas (marcas, categorias, subcategorias)");
        System.out.println("  0 - Terminar sessão");
        System.out.print("Opção: ");
        int opcao = lerInt();
        if (opcao == 1) {
            admin.visualizarObjeto();
        } else if (opcao == 2) {
            admin.consultarEstatisticas();
        } else if (opcao == 3) {
            admin.eliminarAnuncio();
        } else if (opcao == 4) {
            Tabelas.menu();
        } else if (opcao == 0) {
            admin.terminarSessao();
        } else {
            System.out.println("Opção inválida.");
        }
    }
}