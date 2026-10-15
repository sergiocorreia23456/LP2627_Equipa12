package pt.ipp.upt;

public class Administrador extends Utilizador {
    public Administrador(int id, String nome, String email, String password, String contacto) {
        super(id, nome, email, password, contacto, TipoUtilizador.ADMINISTRADOR);
    }
    public void eliminarAnuncio() {
        visualizarObjeto();
        System.out.print("ID do anúncio a eliminar: ");
        int id = Main.lerInt();

        Objeto encontrado = null;
        for (Objeto o : Main.objetos) {
            if (o.getId() == id) {
                encontrado = o;
            }
        }
        if (encontrado == null) {
            System.out.println("Esse anúncio não existe.");
            return;
        }

        encontrado.eliminar();
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
        System.out.println("--- Estatísticas ---");
        System.out.println("Utilizadores: " + Main.utilizadores.size());
        System.out.println("Objetos: " + Main.objetos.size());
        System.out.println("Encontrados: " + encontrados);
        System.out.println("Perdidos: " + perdidos);
        System.out.println("Resolvidos: " + resolvidos);
    }
}
}