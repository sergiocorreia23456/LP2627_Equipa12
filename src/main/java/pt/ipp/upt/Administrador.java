package pt.ipp.upt;

import jakarta.persistence.Entity;
import org.hibernate.Session;
import java.util.ArrayList;
import org.hibernate.Session;

@Entity
public class Administrador extends Utilizador {
	
	protected Administrador() {
	    super();
	}

    public Administrador(int id, String nome, String email, String password, String contacto) {
        super(id, nome, email, password, contacto, TipoUtilizador.ADMINISTRADOR);
    }

    public void visualizarObjeto() {
        System.out.println("--- Todos os objetos ---");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ArrayList<Objeto> objetos = new ArrayList<>(
                session.createQuery("FROM Objeto", Objeto.class)
                       .getResultList()
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

    public void eliminarAnuncio() {
        visualizarObjeto();
        System.out.print("ID do anúncio a eliminar: ");
        int id = Main.lerInt();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Objeto objeto = session.get(Objeto.class, id);
            if (objeto == null) {
                System.out.println("Esse anúncio não existe.");
                return;
            }
            Objeto.eliminar(objeto);
        } catch (Exception e) {
            System.out.println("Erro ao eliminar anúncio: " + e.getMessage());
        }
    }

    public void consultarEstatisticas() {  //utilizamos Long porque a função COUNT() do Hibernate devolve esse tipo de dados
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