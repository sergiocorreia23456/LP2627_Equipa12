package pt.ipp.upt;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Utilizador> utilizadores = new ArrayList<>();
    static ArrayList<Objeto> objetos = new ArrayList<>();
    static ArrayList<Categoria> categorias = new ArrayList<>();
    static Utilizador sessaoAtual = null; // quem tem a sessão iniciada
    static int proximoIdUtilizador = 2;
    static int proximoIdObjeto = 1;
    static int proximoIdCategoria = 5;
    static int proximoIdMarca = 1;    
}