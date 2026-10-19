
package pt.ipp.upt;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrador extends Utilizador {

    protected Administrador() {
        super();
    }

    public Administrador(int id, String nome, String email,
                         String password, String contacto) {
        super(id, nome, email, password, contacto,
              TipoUtilizador.ADMINISTRADOR);
    }

    public void visualizarObjeto() {
        System.out.println("--- Todos os objetos ---");

        for (Objeto o : Main.objetos) {
            o.consultarDetalhes();
        }
    }

    public void eliminarAnuncio() {
        visualizarObjeto();

        System.out.print("ID do anúncio a eliminar: ");
        int id = Main.lerInt();

        Objeto encontrado = null;

        for (Objeto o : Main.objetos) {
            if (o.getId() == id) {
                encontrado = o;
                break;
            }
        }

        if (encontrado == null) {
            System.out.println("Esse anúncio não existe.");
            return;
        }

        Objeto.eliminar(encontrado);

        for (Utilizador u : Main.utilizadores) {
            u.removerObjeto(encontrado);
        }

        System.out.println("Anúncio eliminado!");
    }

    public void consultarEstatisticas() {
        int encontrados = 0;
        int perdidos = 0;
        int resolvidos = 0;

        for (Objeto o : Main.objetos) {
            if (o.getTipo() == TipoObjeto.ENCONTRADO) {
                encontrados++;
            } else {
                perdidos++;
            }

            if (!o.isEstado()) {
                resolvidos++;
            }
        }

        System.out.println("--- Estatísticas do Sistema ---");
        System.out.println(
            "Utilizadores: " + Tabelas.listarUtilizadores().size()
        );
        System.out.println("Objetos: " + Main.objetos.size());
        System.out.println("Encontrados: " + encontrados);
        System.out.println("Perdidos: " + perdidos);
        System.out.println("Resolvidos: " + resolvidos);
    }
}
