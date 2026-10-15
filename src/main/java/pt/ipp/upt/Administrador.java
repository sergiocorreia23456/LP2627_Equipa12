package pt.ipp.upt;

public class Administrador extends Utilizador {

    public Administrador(int id, String nome, String email, String password, String contacto) {
        super(id, nome, email, password, contacto, TipoUtilizador.ADMINISTRADOR);
    }

    public void visualizarObjeto() {
        System.out.println("--- Todos os objetos ---");
        for (Objeto o : Main.objetos) {
            o.consultarDetalhes();
        }
    }
}