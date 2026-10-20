package pt.ipp.upt;

import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GereObjetos {

    public static void registarObjeto(Utilizador utilizador) {
        System.out.print("Tipo (1 = Encontrado, 2 = Perdido): ");
        int opcao = Main.lerInt();
        if (opcao != 1 && opcao != 2) {
            System.out.println("Tipo inválido.");
            return;
        }
        TipoObjeto tipoObjeto;
        if (opcao == 1) {
            tipoObjeto = TipoObjeto.ENCONTRADO;
        } else {
            tipoObjeto = TipoObjeto.PERDIDO;
        }
        String titulo, descricao, localizacao;
        do {
            System.out.print("Título: ");
            titulo = Main.sc.nextLine().trim();
            if (titulo.isBlank()) {
                System.out.println("Título obrigatório!");
            }
        } while (titulo.isBlank());
        do {
            System.out.print("Descrição: ");
            descricao = Main.sc.nextLine().trim();
            if (descricao.isBlank()) {
                System.out.println("Descrição obrigatória!");
            }
        } while (descricao.isBlank());
        do {
            System.out.print("Localização: ");
            localizacao = Main.sc.nextLine().trim();
            if (localizacao.isBlank()) {
                System.out.println("Localização obrigatória!");
            }
        } while (localizacao.isBlank());
        String nomeMarca;
        do {
            System.out.print("Marca: ");
            nomeMarca = Main.sc.nextLine().trim();
            if (nomeMarca.isBlank()) {
                System.out.println("A marca não pode estar vazia!");
            }
        } while (nomeMarca.isBlank());
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {
            Marca marca = session.createQuery(
                    "FROM Marca WHERE LOWER(nome) = :nome",
                    Marca.class)
                    .setParameter("nome",
                            nomeMarca.toLowerCase(java.util.Locale.ROOT))
                    .setMaxResults(1)
                    .uniqueResult();
            boolean novaMarca = (marca == null);
            if (novaMarca) {
                marca = new Marca(0, nomeMarca);
            }
            System.out.println("Categorias:");
            for (Categoria c : session.createQuery(
                    "FROM Categoria", Categoria.class).getResultList()) {
                System.out.println(c.getId() + " - " + c.getNome());
            }
            System.out.print("ID da categoria: ");
            int idCategoria = Main.lerInt();
            Categoria categoria = session.get(
                    Categoria.class, idCategoria);
            if (categoria == null) {
                System.out.println("Categoria não existe.");
                return;
            }
            Objeto novo = new Objeto(0, titulo, descricao, marca,localizacao, categoria, tipoObjeto);
            novo.setUtilizador(utilizador);
            Transaction transaction = session.beginTransaction();
            try {
                if (novaMarca) {
                    session.persist(marca);
                }
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
            System.out.println(
                    "Erro ao aceder à base de dados: " + e.getMessage());
        }
    }

    public static void editarObjeto(Utilizador utilizador) {
        Objeto o = escolherObjeto(utilizador);
        if (o != null) {
            editar(o);
        }
    }
    // o utilizador elimina o objeto
    public static void eliminarObjeto(Utilizador utilizador) {
        Objeto o = escolherObjeto(utilizador);
        if (o != null) {
        	eliminar(o);
        }
    }

    public static void pesquisarObjeto() {
        System.out.print("Palavra a pesquisar: ");
        String palavra = Main.sc.nextLine().toLowerCase();
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {
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

    public static void consultarObjetosEncontrados() {
        System.out.println("--- Objetos encontrados ---");
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {
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

    public static void visualizarAnuncios() {
        System.out.println("--- Todos os anúncios publicados ---");
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto WHERE estado = true",
                    Objeto.class
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
            System.out.println(
                    "Erro ao consultar anúncios: " + e.getMessage());
        }
    }

    private static Objeto escolherObjeto(Utilizador utilizador) {
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto o WHERE o.utilizador = :utilizador",
                    Objeto.class
                )
                .setParameter("utilizador", utilizador)
                .getResultList()
            );
            if (objetos.isEmpty()) {
                System.out.println("Ainda não registaste nenhum objeto.");
                return null;
            }
            System.out.println("Os teus objetos:");
            for (Objeto o : objetos) {
                System.out.println(
                        o.getId() + " - " + o.getTitulo());
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

    public static void visualizarObjeto() {
        System.out.println("--- Todos os objetos ---");
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery(
                    "FROM Objeto", Objeto.class
                ).getResultList()
            );
            if (objetos.isEmpty()) {
                System.out.println("Não existem objetos registados.");
                return;
            }
            for (Objeto o : objetos) {
                o.consultarDetalhes();
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar objetos: " + e.getMessage());
        }
    }
    //o admin elimina o anuncio se tiver informação incorreta
    public static void eliminarAnuncio() {
        visualizarObjeto();
        System.out.print("ID do anúncio a eliminar: ");
        int id = Main.lerInt();
        try (Session session =HibernateUtil.getSessionFactory().openSession()) {
            Objeto objeto = session.get(Objeto.class, id);
            if (objeto == null) {
                System.out.println("Esse anúncio não existe.");
                return;
            }
            eliminar(objeto);
        } catch (Exception e) {
            System.out.println("Erro ao eliminar anúncio: " + e.getMessage());
        }
    }

    public static void editar(Objeto o) {
    System.out.print("Novo título [" + o.getTitulo() + "]: ");
    String novoTitulo = Main.sc.nextLine();
    System.out.print("Nova descrição (Enter para manter): ");
    String novaDescricao = Main.sc.nextLine();
    System.out.print("Nova localização (Enter para manter): ");
    String novaLocalizacao = Main.sc.nextLine();
    System.out.print("Já foi resolvido? (sim/não): ");
    String resposta = Main.sc.nextLine();
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();
        Objeto objetoBD = session.get(Objeto.class, o.getId());
        if (objetoBD == null) {
            System.out.println("Objeto não existe.");
            transaction.rollback();
            return;
        }
        if (!novoTitulo.isEmpty()) {
            objetoBD.setTitulo(novoTitulo);
        }
        if (!novaDescricao.isEmpty()) {
            objetoBD.setDescricao(novaDescricao);
        }
        if (!novaLocalizacao.isEmpty()) {
            objetoBD.setLocalizacao(novaLocalizacao);
        }
        if (resposta.equalsIgnoreCase("sim")) {
            objetoBD.setEstado(false);
        }
        transaction.commit();
        System.out.println("Objeto atualizado com sucesso!");
    } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
        System.out.println("Erro ao atualizar objeto: " + e.getMessage());
    }
}

    public static void eliminar(Objeto o) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();
        Objeto objetoBD = session.get(Objeto.class, o.getId());
        if (objetoBD == null) {
            System.out.println("Objeto não existe.");
            transaction.rollback();
            return;
        }
        session.remove(objetoBD);
        transaction.commit();
        System.out.println("Objeto eliminado da base de dados!");
    } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
        System.out.println("Erro ao eliminar objeto: " + e.getMessage());
    	}
    }

    public static void consultarEstatisticas(Utilizador utilizador) { //utilizamos Long porque a função COUNT() do Hibernate devolve esse tipo de dados
    	if (utilizador == null ||utilizador.getTipo() != TipoUtilizador.ADMINISTRADOR) {
    	    System.out.println("Acesso negado! Apenas administradores podem consultar estatísticas.");
    	    return;
    	    }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long totalUtilizadores = session.createQuery(
                "SELECT COUNT(u) FROM Utilizador u", Long.class
            ).getSingleResult();
            Long totalObjetos = session.createQuery(
                "SELECT COUNT(o) FROM Objeto o", Long.class
            ).getSingleResult();
            Long encontrados = session.createQuery(
                "SELECT COUNT(o) FROM Objeto o WHERE o.tipo = :tipo", Long.class
            )
            .setParameter("tipo", TipoObjeto.ENCONTRADO)
            .getSingleResult();
            Long perdidos = session.createQuery(
                "SELECT COUNT(o) FROM Objeto o WHERE o.tipo = :tipo", Long.class
            )
            .setParameter("tipo", TipoObjeto.PERDIDO)
            .getSingleResult();
            Long resolvidos = session.createQuery(
                "SELECT COUNT(o) FROM Objeto o WHERE o.estado = false", Long.class
            ).getSingleResult();
            System.out.println("--- Estatísticas do Sistema ---");
            System.out.println("Utilizadores: " + totalUtilizadores);
            System.out.println("Objetos: " + totalObjetos);
            System.out.println("Encontrados: " + encontrados);
            System.out.println("Perdidos: " + perdidos);
            System.out.println("Resolvidos: " + resolvidos);
        } catch (Exception e) {
            System.out.println("Erro ao consultar estatísticas: " + e.getMessage());
        }
    }
}