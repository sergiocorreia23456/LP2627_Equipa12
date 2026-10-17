package pt.ipp.upt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Marca {
    private int id;
    private String nome;

    public Marca(int id, String nome) {
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
        System.out.print("Nome da marca: ");
        String nome = Main.sc.nextLine();
        Connection con = Conexao.abrir();
        PreparedStatement ps = con.prepareStatement("INSERT INTO marca (nome) VALUES (?)");
        ps.setString(1, nome);
        ps.executeUpdate();
        ps.close();
        con.close();
        System.out.println("Marca criada com sucesso!");
    }

    public static void read() throws SQLException {
        System.out.println("--- Marcas ---");
        Connection con = Conexao.abrir();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, nome FROM marca");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));
        }
        rs.close();
        st.close();
        con.close();
    }

    public static void editar() throws SQLException {
        read();
        System.out.print("ID da marca a editar: ");
        int id = Main.lerInt();
        System.out.print("Novo nome: ");
        String nome = Main.sc.nextLine();
        Connection con = Conexao.abrir();
        PreparedStatement ps = con.prepareStatement("UPDATE marca SET nome = ? WHERE id = ?");
        ps.setString(1, nome);
        ps.setInt(2, id);
        int linhas = ps.executeUpdate();
        ps.close();
        con.close();
        if (linhas == 0) {
            System.out.println("Esse ID não existe.");
        } else {
            System.out.println("Marca atualizada com sucesso!");
        }
    }

    public static void eliminar() throws SQLException {
        read();
        System.out.print("ID da marca a eliminar: ");
        int id = Main.lerInt();
        Connection con = Conexao.abrir();
        PreparedStatement ps = con.prepareStatement("DELETE FROM marca WHERE id = ?");
        ps.setInt(1, id);
        int linhas = ps.executeUpdate();
        ps.close();
        con.close();
        if (linhas == 0) {
            System.out.println("Esse ID não existe.");
        } else {
            System.out.println("Marca eliminada com sucesso!");
        }
    }
}