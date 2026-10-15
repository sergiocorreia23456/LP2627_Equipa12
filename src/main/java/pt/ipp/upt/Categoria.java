package pt.ipp.upt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Categoria {
    private int id;
    private String nome;

    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public static void criar() throws SQLException {
        System.out.print("Nome da categoria: ");
        String nome = Main.sc.nextLine();
        Connection con = Conexao.abrir();
        PreparedStatement ps = con.prepareStatement("INSERT INTO categoria (nome) VALUES (?)");
        ps.setString(1, nome);
        ps.executeUpdate();
        ps.close();
        con.close();
        System.out.println("Categoria criada com sucesso!");
    }

    public static void read() throws SQLException {
        System.out.println("--- Categorias ---");
        Connection con = Conexao.abrir();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, nome FROM categoria");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));
        }
        rs.close();
        st.close();
        con.close();
    }

    public static void editar() throws SQLException {
        read();
        System.out.print("ID da categoria a editar: ");
        int id = Main.lerInt();
        System.out.print("Novo nome: ");
        String nome = Main.sc.nextLine();
        Connection con = Conexao.abrir();
        PreparedStatement ps = con.prepareStatement("UPDATE categoria SET nome = ? WHERE id = ?");
        ps.setString(1, nome);
        ps.setInt(2, id);
        int linhas = ps.executeUpdate();
        ps.close();
        con.close();
        if (linhas == 0) {
            System.out.println("Esse ID não existe.");
        } else {
            System.out.println("Categoria atualizada com sucesso!");
        }
    }

    public static void eliminar() throws SQLException {
        read();
        System.out.print("ID da categoria a eliminar: ");
        int id = Main.lerInt();
        Connection con = Conexao.abrir();
        // Não deixa eliminar se ainda tiver subcategorias
        PreparedStatement verifica = con.prepareStatement("SELECT COUNT(*) FROM subcategoria WHERE categoria_id = ?");
        verifica.setInt(1, id);
        ResultSet rs = verifica.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close();
        verifica.close();
        if (total > 0) {
            System.out.println("Esta categoria tem subcategorias. Elimina-as primeiro.");
            con.close();
            return;
        }
        PreparedStatement ps = con.prepareStatement("DELETE FROM categoria WHERE id = ?");
        ps.setInt(1, id);
        int linhas = ps.executeUpdate();
        ps.close();
        con.close();
        if (linhas == 0) {
            System.out.println("Esse ID não existe.");
        } else {
            System.out.println("Categoria eliminada com sucesso!");
        }
    }
}