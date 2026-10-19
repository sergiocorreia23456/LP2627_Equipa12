
package pt.ipp.upt;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Tabelas {

	private static final SessionFactory sessionFactory =
	        HibernateUtil.getSessionFactory();
    public static void menu() {
            while (true) {
                System.out.println();
                System.out.println("=== Gerir Tabelas ===");
                System.out.println("1 - Marcas");
                System.out.println("2 - Categorias");
                System.out.println("3 - Subcategorias");
                System.out.println("0 - Voltar");
                System.out.print("Opção: ");
                int tabela = Main.lerInt();
                if (tabela == 0) {
                    return;
                }
                if (tabela < 1 || tabela > 3) {
                    System.out.println("Opção inválida.");
                    continue;
                }
                System.out.println("1 - Criar");
                System.out.println("2 - Consultar");
                System.out.println("3 - Editar");
                System.out.println("4 - Eliminar");
                System.out.print("Opção: ");
                int operacao = Main.lerInt();
                if (tabela == 1) {
                    if (operacao == 1) {
                        criarMarca();
                    } else if (operacao == 2) {
                        readMarcas();
                    } else if (operacao == 3) {
                        editarMarca();
                    } else if (operacao == 4) {
                        eliminarMarca();
                    }
                    break;

                case 2:
                    switch (op) {
                        case 1 -> criarCategoria();
                        case 2 -> readCategorias();
                        case 3 -> editarCategoria();
                        case 4 -> eliminarCategoria();
                        default -> System.out.println("Opção inválida.");
                    }
                    break;

                case 3:
                    switch (op) {
                        case 1 -> criarSubCategoria();
                        case 2 -> readSubCategorias();
                        case 3 -> editarSubCategoria();
                        case 4 -> eliminarSubCategoria();
                        default -> System.out.println("Opção inválida.");
                    }
                    break;

                case 4:
                    switch (op) {
                        case 1 -> criarUtilizador();
                        case 2 -> readUtilizadores();
                        case 3 -> editarUtilizador();
                        case 4 -> eliminarUtilizadorMenu();
                        default -> System.out.println("Opção inválida.");
                    }
                    break;
            }
    }

    // ========== MARCAS ==========

    public static void criarMarca() {
        String nome = lerNome("Nome da marca: ");
        if (nome.isEmpty()) return;

        if (guardar(new Marca(nome)))
            System.out.println("Marca criada com sucesso!");
    }

    public static void readMarcas() {
        try {
            System.out.println("\n--- Marcas ---");
            List<Marca> lista = listar(Marca.class);

            if (lista.isEmpty()) System.out.println("Sem marcas.");

            for (Marca m : lista)
                System.out.println(m.getId() + " - " + m.getNome());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void editarMarca() {
        readMarcas();
        System.out.print("ID da marca: ");
        int id = Main.lerInt();

        String nome = lerNome("Novo nome: ");
        if (nome.isEmpty()) return;

        if (executar(s -> {
            Marca m = s.get(Marca.class, id);
            if (m == null)
                throw new IllegalArgumentException("Marca não existe.");
            m.setNome(nome);
        })) System.out.println("Marca atualizada!");
    }

    public static void eliminarMarca() {
        readMarcas();
        System.out.print("ID da marca: ");
        int id = Main.lerInt();

        if (eliminar(Marca.class, id))
            System.out.println("Marca eliminada!");
    }

    // ================= CATEGORIAS =================
    public static ArrayList<Categoria> obterCategorias() {
        try (Session session = sessionFactory.openSession()) {
            return new ArrayList<>(
                session.createQuery("from Categoria", Categoria.class)
                       .getResultList()
            );
        }
    }
    
    public static void criarCategoria() {
        String nome = lerNome("Nome da categoria: ");
        if (nome.isEmpty()) return;

        if (guardar(new Categoria(nome)))
            System.out.println("Categoria criada!");
    }

    public static void readCategorias() {
        try {
            System.out.println("\n--- Categorias ---");
            List<Categoria> lista = listar(Categoria.class);

            if (lista.isEmpty()) System.out.println("Sem categorias.");

            for (Categoria c : lista)
                System.out.println(c.getId() + " - " + c.getNome());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void editarCategoria() {
        readCategorias();
        System.out.print("ID da categoria: ");
        int id = Main.lerInt();

        String nome = lerNome("Novo nome: ");
        if (nome.isEmpty()) return;

        if (executar(s -> {
            Categoria c = s.get(Categoria.class, id);
            if (c == null)
                throw new IllegalArgumentException("Categoria não existe.");
            c.setNome(nome);
        })) System.out.println("Categoria atualizada!");
    }

    public static void eliminarCategoria() {
        readCategorias();
        System.out.print("ID da categoria: ");
        int id = Main.lerInt();

        if (executar(s -> {
            Categoria c = s.get(Categoria.class, id);

            if (c == null)
                throw new IllegalArgumentException("Categoria não existe.");

            if (!c.getSubcategorias().isEmpty())
                throw new IllegalArgumentException(
                        "Elimina primeiro as subcategorias.");

            s.remove(c);
        })) System.out.println("Categoria eliminada!");
    }

    // ========== SUBCATEGORIAS ==========

    public static void criarSubCategoria() {
        readCategorias();

        System.out.print("ID da categoria principal: ");
        int id = Main.lerInt();

        String nome = lerNome("Nome da subcategoria: ");
        if (nome.isEmpty()) return;

        if (executar(s -> {
            Categoria c = s.get(Categoria.class, id);

            if (c == null)
                throw new IllegalArgumentException("Categoria não existe.");

            SubCategoria sub = new SubCategoria(nome);
            c.addSubCategoria(sub);
            s.persist(sub);

        })) System.out.println("Subcategoria criada!");
    }

    public static void readSubCategorias() {
        try {
            System.out.println("\n--- Subcategorias ---");

            List<SubCategoria> lista = consultar(s ->
                    s.createQuery(
                            "select sub from SubCategoria sub "
                            + "join fetch sub.categoria c "
                            + "order by c.nome, sub.nome",
                            SubCategoria.class
                    ).getResultList()
            );

            if (lista.isEmpty()) System.out.println("Sem subcategorias.");

            for (SubCategoria sub : lista)
                System.out.println(
                        sub.getId() + " - "
                        + sub.getCategoria().getNome()
                        + " > " + sub.getNome());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void editarSubCategoria() {
        readSubCategorias();

        System.out.print("ID da subcategoria: ");
        int id = Main.lerInt();

        String nome = lerNome("Novo nome: ");
        if (nome.isEmpty()) return;

        if (executar(s -> {
            SubCategoria sub = s.get(SubCategoria.class, id);

            if (sub == null)
                throw new IllegalArgumentException("Subcategoria não existe.");

            sub.setNome(nome);
        })) System.out.println("Subcategoria atualizada!");
    }

    public static void eliminarSubCategoria() {
        readSubCategorias();

        System.out.print("ID da subcategoria: ");
        int id = Main.lerInt();

        if (eliminar(SubCategoria.class, id))
            System.out.println("Subcategoria eliminada!");
    }

    // ========== UTILIZADORES ==========

    public static boolean guardarUtilizador(Utilizador u) {
        return guardar(u);
    }

    public static List<Utilizador> listarUtilizadores() {
        return listar(Utilizador.class);
    }

    public static Utilizador procurarUtilizadorPorId(int id) {
        return procurar(Utilizador.class, id);
    }

    public static Utilizador procurarUtilizadorPorEmail(String email) {
        return consultar(s -> s.createQuery(
                "from Utilizador where email = :email",
                Utilizador.class
        ).setParameter("email", email).uniqueResult());
    }

    public static Utilizador autenticarUtilizador(
            String email, String password) {

        return consultar(s -> s.createQuery(
                "from Utilizador where email = :email "
                + "and password = :password",
                Utilizador.class
        )
        .setParameter("email", email)
        .setParameter("password", password)
        .uniqueResult());
    }

    public static boolean atualizarUtilizador(Utilizador u) {
        return executar(s -> s.merge(u));
    }

    public static boolean eliminarUtilizador(int id) {
        return eliminar(Utilizador.class, id);
    }

    public static void criarUtilizador() {
        String nome = lerNome("Nome: ");
        String email = lerNome("Email: ");
        String password = lerNome("Password: ");

        System.out.print("Contacto: ");
        String contacto = Main.sc.nextLine().trim();

        if (nome.isEmpty() || email.isEmpty() || password.isEmpty())
            return;

        try {
            if (procurarUtilizadorPorEmail(email) != null) {
                System.out.println("Esse email já existe.");
                return;
            }

            Utilizador u = new Utilizador(
                    0, nome, email, password, contacto,
                    TipoUtilizador.UTILIZADOR);

            if (guardarUtilizador(u)) {
                Main.utilizadores.add(u);
                System.out.println("Utilizador criado!");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void readUtilizadores() {
        try {
            System.out.println("\n--- Utilizadores ---");
            List<Utilizador> lista = listarUtilizadores();

            if (lista.isEmpty()) System.out.println("Sem utilizadores.");

            for (Utilizador u : lista)
                System.out.println(
                        u.getId() + " - "
                        + u.getNome() + " - "
                        + u.getEmail());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void editarUtilizador() {
        readUtilizadores();

        System.out.print("ID do utilizador: ");
        int id = Main.lerInt();

        String nome = lerNome("Novo nome: ");
        if (nome.isEmpty()) return;

        if (executar(s -> {
            Utilizador u = s.get(Utilizador.class, id);

            if (u == null)
                throw new IllegalArgumentException("Utilizador não existe.");

            u.setNome(nome);

        })) {
            for (Utilizador u : Main.utilizadores)
                if (u.getId() == id) u.setNome(nome);

            System.out.println("Utilizador atualizado!");
        }
    }

    public static void eliminarUtilizadorMenu() {
        readUtilizadores();

        System.out.print("ID do utilizador: ");
        int id = Main.lerInt();

        if (eliminarUtilizador(id)) {
            Main.utilizadores.removeIf(u -> u.getId() == id);

            if (Main.sessaoAtual != null
                    && Main.sessaoAtual.getId() == id)
                Main.sessaoAtual = null;

            System.out.println("Utilizador eliminado!");
        }
    }

    // ========== CONSULTAS AUXILIARES ==========

    public static Marca procurarMarcaPorId(int id) {
        return procurar(Marca.class, id);
    }

    public static Categoria procurarCategoriaPorId(int id) {
        return procurar(Categoria.class, id);
    }

    // ========== OBJETOS ==========

    public static boolean guardarObjeto(Objeto objeto) {
        return guardar(objeto);
    }

    public static boolean atualizarObjeto(Objeto objeto) {
        return executar(s -> s.merge(objeto));
    }

    public static boolean eliminarObjeto(Objeto objeto) {
        return eliminar(Objeto.class, objeto.getId());
    }

    public static Objeto procurarObjetoPorId(int id) {
        return procurar(Objeto.class, id);
    }

    public static List<Objeto> listarObjetos() {
        return consultar(s ->
                s.createQuery(
                    "select o from Objeto o "
                    + "left join fetch o.marca "
                    + "left join fetch o.categoria",
                    Objeto.class
                ).getResultList()
        );
    }

    // ========== FECHAR HIBERNATE ==========

    public static void fechar() {
        if (!factory.isClosed()) factory.close();
    }
}
