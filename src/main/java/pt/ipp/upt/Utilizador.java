package pt.ipp.upt;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;

	@Entity
	@Table(name = "Utilizador")

	public class Utilizador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, length = 100)
	private String nome;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 20)
	private String contacto;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoUtilizador tipo;
	
	@OneToMany(mappedBy = "utilizador")
	private List<Objeto> objetos = new ArrayList<>();
	
	public List<Objeto> getObjetos() {
	    return objetos;
	}
	
	protected Utilizador() {
	}

    public Utilizador(int id, String nome, String email, String password,
                      String contacto, TipoUtilizador tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.contacto = contacto;
        this.tipo = tipo;
    }
    
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public TipoUtilizador getTipo() {
        return tipo;
    }
    public String getPassword() {
        return password;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}