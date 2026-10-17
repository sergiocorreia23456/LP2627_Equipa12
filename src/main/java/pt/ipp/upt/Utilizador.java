package pt.ipp.upt;
import java.util.ArrayList;

public class Utilizador {
    private int id;
    private String nome;
    private String email;
    private String password;
    private String contacto;
    private TipoUtilizador tipo;
    private ArrayList<Objeto> objetos = new ArrayList<>();

    public Utilizador(int id, String nome, String email, String password,
                      String contacto, TipoUtilizador tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.contacto = contacto;
        this.tipo = tipo;
    }
    public String getNome() {
        return nome;
    }
    public TipoUtilizador getTipo() {
        return tipo;
    }
    
    public void editarPerfil() {
            System.out.print("Novo nome [" + nome + "]: ");
            String texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                nome = texto;
            }
            System.out.print("Novo contacto [" + contacto + "]: ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                contacto = texto;
            }
            System.out.print("Nova password (Enter para manter): ");
            texto = Main.sc.nextLine();
            if (!texto.equals("")) {
                password = texto;
            }
            System.out.println("Perfil atualizado!");
        }
    
    public void visualizarAnuncios() {
        System.out.println("--- Todos os anúncios publicados ---");
        boolean encontrou = false;
        for (Objeto o : Main.objetos) {
            if (o.isEstado()) {
                o.consultarDetalhes();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Não há anúncios publicados.");
        }
    }

    public void registarObjeto() {
        System.out.print("Tipo (1 = Encontrado, 2 = Perdido): ");
        int opcao = Integer.parseInt(Main.sc.nextLine());
        TipoObjeto tipoObjeto;
        if (opcao == 1) {
            tipoObjeto = TipoObjeto.ENCONTRADO;
        } else {
            tipoObjeto = TipoObjeto.PERDIDO;
        }
        System.out.print("Título: ");
        String titulo = Main.sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = Main.sc.nextLine();
        System.out.print("Marca: ");
        String nomeMarca = Main.sc.nextLine();
        System.out.print("Localização: ");
        String localizacao = Main.sc.nextLine();
        System.out.println("Categorias:");
        for (Categoria c : Main.categorias) {
            System.out.println(c.getId() + " - " + c.getNome());
        }
        System.out.print("ID da categoria: ");
        int idCategoria = Integer.parseInt(Main.sc.nextLine());
        Categoria categoria = Main.procurarCategoria(idCategoria);
        if (categoria == null) {
            System.out.println("Categoria não existe.");
            return;
        }
        Marca marca = new Marca(Main.proximoIdMarca, nomeMarca);
        Main.proximoIdMarca++;
        Objeto novo = new Objeto(Main.proximoIdObjeto, titulo, descricao, marca,localizacao, categoria, tipoObjeto);
        Main.proximoIdObjeto++;
        Main.objetos.add(novo);
        objetos.add(novo);
        System.out.println("Objeto registado com ID " + novo.getId());
    }

    public void editarObjeto() {
        Objeto o = escolherObjeto();
        if (o != null) {
            o.editar();
        }
    }
    
    public void pesquisarObjeto() {
            System.out.print("Palavra a pesquisar: ");
            String palavra = Main.sc.nextLine().toLowerCase();
            int contador = 0;
            for (Objeto o : Main.objetos) {
                if (o.isEstado() && o.getTextoParaPesquisa().toLowerCase().contains(palavra)) {
                    o.consultarDetalhes();
                    contador++;
                }
            }
            System.out.println(contador + " resultado(s).");
        }
    
    public void consultarObjetosEncontrados() {
        System.out.println("--- Objetos encontrados ---");
        boolean encontrou = false;
        for (Objeto o : Main.objetos) {
            if (o.getTipo() == TipoObjeto.ENCONTRADO && o.isEstado()) {
                o.consultarDetalhes();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Não há objetos encontrados.");
        }
    }
    // o utilizador elimina o objeto
    public void eliminarObjeto() {
        Objeto o = escolherObjeto();
        if (o != null) {
            Objeto.eliminar(o);
            objetos.remove(o);
            System.out.println("Objeto eliminado!");
        }
    }
    // o admin remove o objeto se tiver informação incorreta
    public void removerObjeto(Objeto o) {
        objetos.remove(o);
    }
    
 // Mostra os objetos do utilizador e pede o ID
    private Objeto escolherObjeto() {
        if (objetos.size() == 0) {
            System.out.println("Ainda não registaste nenhum objeto.");
            return null;
        }
        System.out.println("Os teus objetos:");
        for (Objeto o : objetos) {
            System.out.println(o.getId() + " - " + o.getTitulo());
        }
        System.out.print("ID do objeto: ");
        int id = Integer.parseInt(Main.sc.nextLine());
        for (Objeto o : objetos) {
            if (o.getId() == id) {
                return o;
            }
        }
        System.out.println("Esse objeto não foi criado por ti ou não existe.");
        return null;
    }
}