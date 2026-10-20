package pt.ipp.upt;

import java.util.ArrayList;
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
                } else if (tabela == 2) {
                    if (operacao == 1) {
                        criarCategoria();
                    } else if (operacao == 2) {
                        readCategorias();
                    } else if (operacao == 3) {
                        editarCategoria();
                    } else if (operacao == 4) {
                        eliminarCategoria();
                    }
                } else {
                    if (operacao == 1) {
                        criarSubCategoria();
                    } else if (operacao == 2) {
                        readSubCategorias();
                    } else if (operacao == 3) {
                        editarSubCategoria();
                    } else if (operacao == 4) {
                        eliminarSubCategoria();
                    }
                }
            }
    }
    // ================= MARCAS =================
    public static void criarMarca() {
    String nome;
    do {
        System.out.print("Nome da marca: ");
        nome = Main.sc.nextLine().trim();
        if (nome.isBlank()) {
            System.out.println("O nome da marca não pode estar vazio!");
        }
    } while (nome.isBlank());
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
        // Verificar se a marca já existe
        Marca existente = session.createQuery(
                "FROM Marca WHERE LOWER(nome) = :nome",
                Marca.class)
                .setParameter("nome", nome.toLowerCase(java.util.Locale.ROOT))
                .setMaxResults(1)
                .uniqueResult();
        if (existente != null) {
            System.out.println("Essa marca já existe na base de dados!");
            return;
        }
        transaction = session.beginTransaction();
        session.persist(new Marca(nome));
        transaction.commit();
        System.out.println("Marca criada com sucesso!");
    } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
        System.out.println("Erro na base de dados: " + e.getMessage());
    }
}

    public static void readMarcas() {
        System.out.println("--- Marcas ---");
        try (Session session = sessionFactory.openSession()) {
            ArrayList<Marca> marcas = new ArrayList<>(session
                    .createQuery("from Marca", Marca.class)
                    .getResultList());
            for (Marca m : marcas) {
                System.out.println(m.getId() + " - " + m.getNome());
            }
        } catch (Exception e) {
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void editarMarca() {
    readMarcas();
    System.out.print("ID da marca a editar: ");
    int id = Main.lerInt();
    System.out.print("Novo nome: ");
    String nome = Main.sc.nextLine().trim();
    if (nome.isBlank()) {
        System.out.println("O nome da marca não pode estar vazio!");
        return;
    }
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
        transaction = session.beginTransaction();
        Marca marca = session.get(Marca.class, id);
        if (marca == null) {
            System.out.println("Essa marca não existe.");
            transaction.rollback();
            return;
        }
        // Verificar se outra marca já tem este nome
        Long duplicados = session.createQuery(
                "SELECT COUNT(m) FROM Marca m " +
                "WHERE LOWER(m.nome) = :nome AND m.id <> :id",
                Long.class)
                .setParameter("nome", nome.toLowerCase(java.util.Locale.ROOT))
                .setParameter("id", id)
                .getSingleResult();
        if (duplicados > 0) {
            System.out.println("Já existe outra marca com esse nome!");
            transaction.rollback();
            return;
        }
        marca.setNome(nome);
        transaction.commit();
        System.out.println("Marca atualizada com sucesso!");
    } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
        System.out.println("Erro na base de dados: " + e.getMessage());
    }
}

    public static void eliminarMarca() {
        readMarcas();
        System.out.print("ID da marca a eliminar: ");
        int id = Main.lerInt();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Marca marca = session.get(Marca.class, id);
            if (marca == null) {
                System.out.println("Esse ID não existe.");
                transaction.rollback();
                return;
            }
            session.remove(marca);
            transaction.commit();
            System.out.println("Marca eliminada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
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
        System.out.print("Nome da categoria: ");
        String nome = Main.sc.nextLine();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(new Categoria(nome));
            transaction.commit();
            System.out.println("Categoria criada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void readCategorias() {
        System.out.println("--- Categorias ---");
        try (Session session = sessionFactory.openSession()) {
        	ArrayList<Categoria> categorias = new ArrayList<>(session
        	        .createQuery("from Categoria", Categoria.class)
        	        .getResultList());
            for (Categoria c : categorias) {
                System.out.println(c.getId() + " - " + c.getNome());
            }
        } catch (Exception e) {
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void editarCategoria() {
        readCategorias();
        System.out.print("ID da categoria a editar: ");
        int id = Main.lerInt();
        System.out.print("Novo nome: ");
        String nome = Main.sc.nextLine();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Categoria categoria = session.get(Categoria.class, id);
            if (categoria == null) {
                System.out.println("Esse ID não existe.");
                transaction.rollback();
                return;
            }
            categoria.setNome(nome);
            transaction.commit();
            System.out.println("Categoria atualizada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void eliminarCategoria() {
        readCategorias();
        System.out.print("ID da categoria a eliminar: ");
        int id = Main.lerInt();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Categoria categoria = session.get(Categoria.class, id);
            if (categoria == null) {
                System.out.println("Esse ID não existe.");
                transaction.rollback();
                return;
            }
            // Não deixa eliminar se ainda tiver subcategorias
            if (!categoria.getSubcategorias().isEmpty()) {
                System.out.println("Esta categoria tem subcategorias. Elimina-as primeiro.");
                transaction.rollback();
                return;
            }
            session.remove(categoria);
            transaction.commit();
            System.out.println("Categoria eliminada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    // ================= SUBCATEGORIAS =================
    public static void criarSubCategoria() {
        readCategorias();
        System.out.print("ID da categoria principal: ");
        int idCategoria = Main.lerInt();
        System.out.print("Nome da subcategoria: ");
        String nome = Main.sc.nextLine();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Categoria categoria = session.get(Categoria.class, idCategoria);
            if (categoria == null) {
                System.out.println("Essa categoria não existe.");
                transaction.rollback();
                return;
            }
            SubCategoria subcategoria = new SubCategoria(nome);
            categoria.addSubCategoria(subcategoria);
            session.persist(subcategoria);
            transaction.commit();
            System.out.println("Subcategoria criada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void readSubCategorias() {
        System.out.println("--- Subcategorias ---");
        try (Session session = sessionFactory.openSession()) {
        	ArrayList<SubCategoria> subcategorias = new ArrayList<>(session
        	        .createQuery("select s from SubCategoria s join s.categoria c "
        	                + "order by c.nome, s.nome", SubCategoria.class)
        	        .getResultList());
            for (SubCategoria s : subcategorias) {
                System.out.println(s.getId() + " - " + s.getCategoria().getNome()
                        + " > " + s.getNome());
            }
        } catch (Exception e) {
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void editarSubCategoria() {
        readSubCategorias();
        System.out.print("ID da subcategoria a editar: ");
        int id = Main.lerInt();
        System.out.print("Novo nome: ");
        String nome = Main.sc.nextLine();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            SubCategoria subcategoria = session.get(SubCategoria.class, id);
            if (subcategoria == null) {
                System.out.println("Esse ID não existe.");
                transaction.rollback();
                return;
            }
            subcategoria.setNome(nome);
            transaction.commit();
            System.out.println("Subcategoria atualizada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }

    public static void eliminarSubCategoria() {
        readSubCategorias();
        System.out.print("ID da subcategoria a eliminar: ");
        int id = Main.lerInt();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            SubCategoria subcategoria = session.get(SubCategoria.class, id);
            if (subcategoria == null) {
                System.out.println("Esse ID não existe.");
                transaction.rollback();
                return;
            }
            session.remove(subcategoria);
            transaction.commit();
            System.out.println("Subcategoria eliminada com sucesso!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Erro na base de dados: " + e.getMessage());
        }
    }
}