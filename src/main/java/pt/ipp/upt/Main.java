
package pt.ipp.upt;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    // Mantidas para compatibilidade com outras classes
    static ArrayList<Utilizador> utilizadores = new ArrayList<>();
    static ArrayList<Objeto> objetos = new ArrayList<>();
    static ArrayList<Categoria> categorias = new ArrayList<>();

    static Utilizador sessaoAtual = null;

    static int proximoIdUtilizador = 2;
    static int proximoIdObjeto = 1;
    static int proximoIdMarca = 1;

    public static void main(String[] args) {

        criarAdministradorInicial();

        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║    OBJETOS ACHADOS E PERDIDOS      ║");
        System.out.println("╚════════════════════════════════════╝");

        try {
            while (true) {

                if (sessaoAtual == null) {
                    menuInicial();

                } else if (sessaoAtual.getTipo()
                        == TipoUtilizador.ADMINISTRADOR) {
                    menuAdministrador();

                } else {
                    menuUtilizador();
                }
            }

        } finally {
            Tabelas.fechar();
            sc.close();
        }
    }
    public static void criarAdministradorInicial() {

        String emailAdmin = "admin@upt.pt";

        try {
            Utilizador existente =
                    Tabelas.procurarUtilizadorPorEmail(emailAdmin);

            if (existente == null) {

                Administrador admin = new Administrador(
                        0,
                        "Admin",
                        emailAdmin,
                        "123",
                        "999888777"
                );

                if (Tabelas.guardarUtilizador(admin)) {
                    System.out.println(
                        "Administrador inicial criado com sucesso!"
                    );
                }

            } else {
                System.out.println(
                    "Administrador inicial já existe na base de dados."
                );
            }

        } catch (Exception e) {
            System.out.println(
                "Erro ao verificar administrador: " + e.getMessage()
            );
        }
    }
    public static int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Número inválido, tenta outra vez: ");
            }
        }
    }

    public static Categoria procurarCategoria(int id) {
        return Tabelas.procurarCategoriaPorId(id);
    }

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("── " + texto + " "
                + "─".repeat(Math.max(0, 34 - texto.length())));
    }

    public static void menuInicial() {
        titulo("MENU INICIAL");

        System.out.println("  1 - Criar conta");
        System.out.println("  2 - Iniciar sessão");
        System.out.println("  0 - Sair");

        System.out.print("Opção: ");
        int opcao = lerInt();

        switch (opcao) {
            case 1 -> Utilizador.criarConta();
            case 2 -> Utilizador.iniciarSessao();
            case 0 -> {
                System.out.println("Até à próxima!");
                Tabelas.fechar();
                System.exit(0);
            }
            default -> System.out.println("Opção inválida.");
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

        switch (opcao) {
            case 1 -> registarObjeto();
            case 2 -> editarObjeto();
            case 3 -> eliminarObjeto();
            case 4 -> pesquisarObjeto();
            case 5 -> consultarObjetosEncontrados();
            case 6 -> visualizarAnuncios();
            case 7 -> sessaoAtual.editarPerfil();
            case 0 -> sessaoAtual.terminarSessao();
            default -> System.out.println("Opção inválida.");
        }
    }

    public static void menuAdministrador() {
        titulo("Administrador: " + sessaoAtual.getNome());

        System.out.println("  1 - Ver todos os objetos");
        System.out.println("  2 - Ver estatísticas");
        System.out.println("  3 - Eliminar anúncio");
        System.out.println("  4 - Gerir tabelas");
        System.out.println("  0 - Terminar sessão");

        System.out.print("Opção: ");
        int opcao = lerInt();

        switch (opcao) {
            case 1 -> visualizarAnuncios();
            case 2 -> consultarEstatisticas();
            case 3 -> eliminarObjeto();
            case 4 -> Tabelas.menu();
            case 0 -> sessaoAtual.terminarSessao();
            default -> System.out.println("Opção inválida.");
        }
    }

    // ========== OBJETOS ==========

    public static void registarObjeto() {
        titulo("REGISTAR OBJETO");

        System.out.print("Título: ");
        String titulo = sc.nextLine().trim();

        System.out.print("Descrição: ");
        String descricao = sc.nextLine().trim();

        System.out.print("Localização: ");
        String localizacao = sc.nextLine().trim();

        if (titulo.isEmpty() || localizacao.isEmpty()) {
            System.out.println("Título e localização são obrigatórios.");
            return;
        }

        System.out.println("1 - Perdido");
        System.out.println("2 - Encontrado");
        System.out.print("Tipo: ");
        int escolha = lerInt();

        TipoObjeto tipo;

        if (escolha == 1) {
            tipo = TipoObjeto.PERDIDO;
        } else if (escolha == 2) {
            tipo = TipoObjeto.ENCONTRADO;
        } else {
            System.out.println("Tipo inválido.");
            return;
        }

        Tabelas.readMarcas();
        System.out.print("ID da marca (0 = sem marca): ");
        int marcaId = lerInt();

        Marca marca = marcaId == 0
                ? null
                : Tabelas.procurarMarcaPorId(marcaId);

        if (marcaId != 0 && marca == null) {
            System.out.println("Marca não existe.");
            return;
        }

        Tabelas.readCategorias();
        System.out.print("ID da categoria (0 = sem categoria): ");
        int categoriaId = lerInt();

        Categoria categoria = categoriaId == 0
                ? null
                : Tabelas.procurarCategoriaPorId(categoriaId);

        if (categoriaId != 0 && categoria == null) {
            System.out.println("Categoria não existe.");
            return;
        }

        Objeto objeto = new Objeto(
                0, titulo, descricao, marca,
                localizacao, categoria, tipo
        );

        if (Tabelas.guardarObjeto(objeto)) {
            System.out.println("Objeto registado com sucesso!");
        }
    }

    public static void editarObjeto() {
        titulo("EDITAR OBJETO");

        visualizarAnuncios();

        System.out.print("ID do objeto: ");
        int id = lerInt();

        Objeto objeto = Tabelas.procurarObjetoPorId(id);

        if (objeto == null) {
            System.out.println("Objeto não encontrado.");
            return;
        }

        objeto.editar();
    }

    public static void eliminarObjeto() {
        titulo("ELIMINAR OBJETO");

        visualizarAnuncios();

        System.out.print("ID do objeto: ");
        int id = lerInt();

        Objeto objeto = Tabelas.procurarObjetoPorId(id);

        if (objeto == null) {
            System.out.println("Objeto não encontrado.");
            return;
        }

        if (Tabelas.eliminarObjeto(objeto)) {
            System.out.println("Objeto eliminado!");
        }
    }

    public static void pesquisarObjeto() {
        titulo("PESQUISAR OBJETO");

        System.out.print("Texto a pesquisar: ");
        String pesquisa = sc.nextLine().trim().toLowerCase();

        boolean encontrou = false;

        for (Objeto objeto : Tabelas.listarObjetos()) {
            if (objeto.getTextoParaPesquisa()
                    .toLowerCase().contains(pesquisa)) {

                objeto.consultarDetalhes();
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum objeto encontrado.");
        }
    }

    public static void consultarObjetosEncontrados() {
        titulo("OBJETOS ENCONTRADOS");

        boolean encontrou = false;

        for (Objeto objeto : Tabelas.listarObjetos()) {
            if (objeto.getTipo() == TipoObjeto.ENCONTRADO) {
                objeto.consultarDetalhes();
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Não existem objetos encontrados.");
        }
    }

    public static void visualizarAnuncios() {
        titulo("TODOS OS ANÚNCIOS");

        java.util.List<Objeto> lista = Tabelas.listarObjetos();

        if (lista.isEmpty()) {
            System.out.println("Ainda não existem anúncios.");
            return;
        }

        for (Objeto objeto : lista) {
            objeto.consultarDetalhes();
        }
    }

    public static void consultarEstatisticas() {
        titulo("ESTATÍSTICAS");

        java.util.List<Objeto> lista = Tabelas.listarObjetos();

        long perdidos = lista.stream()
                .filter(o -> o.getTipo() == TipoObjeto.PERDIDO)
                .count();

        long encontrados = lista.stream()
                .filter(o -> o.getTipo() == TipoObjeto.ENCONTRADO)
                .count();

        long resolvidos = lista.stream()
                .filter(o -> !o.isEstado())
                .count();

        System.out.println("Total de objetos: " + lista.size());
        System.out.println("Perdidos: " + perdidos);
        System.out.println("Encontrados: " + encontrados);
        System.out.println("Resolvidos: " + resolvidos);
    }
}
