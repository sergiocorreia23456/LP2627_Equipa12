package pt.ipp.upt;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ValoresIniciais {

    public static void criarCategorias() {

        String[][] dados = {
            {"Eletrónica", "Telemóveis", "Computadores", "Tablets"},
            {"Documentos", "Cartão de Cidadão", "Cartões bancários"},
            {"Acessórios", "Carteiras", "Mochilas", "Malas"},
            {"Vestuário", "Casacos", "Camisolas", "Calçado"},
            {"Chaves", "Chaves de casa", "Chaves de carro"},
            {"Outros", "Outros objetos"}
        };

        Transaction transaction = null;

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            for (String[] grupo : dados) {

                String nomeCategoria = grupo[0];

                Categoria categoria = session.createQuery(
                    "FROM Categoria WHERE nome = :nome",
                    Categoria.class)
                    .setParameter("nome", nomeCategoria)
                    .setMaxResults(1)
                    .uniqueResult();

                if (categoria == null) {
                    categoria = new Categoria(nomeCategoria);
                    session.persist(categoria);
                }

                for (int i = 1; i < grupo.length; i++) {

                    String nomeSubcategoria = grupo[i];

                    Long quantidade = session.createQuery(
                        "SELECT COUNT(s) FROM SubCategoria s " +
                        "WHERE s.nome = :nome " +
                        "AND s.categoria.id = :id",
                        Long.class)
                        .setParameter("nome", nomeSubcategoria)
                        .setParameter("id", categoria.getId())
                        .uniqueResult();

                    if (quantidade == 0) {

                        SubCategoria subcategoria =
                            new SubCategoria(nomeSubcategoria);

                        subcategoria.setCategoria(categoria);
                        session.persist(subcategoria);
                    }
                }
            }

            transaction.commit();

            System.out.println(
                "Categorias e subcategorias verificadas com sucesso!");

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            System.out.println(
                "Erro ao criar categorias: " + e.getMessage());

            throw new IllegalStateException(
                "Erro na inicialização das categorias.", e);
        }
    }
}