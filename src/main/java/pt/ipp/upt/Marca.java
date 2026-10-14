package pt.ipp.upt;

public class Marca {
	private int id;
    private String nome;
    
    public Marca(int id, String nome) { 
    	this.id = id; this.nome = nome; 
    	}
    
    public int getId() { 
    	return id; 
    	}
    
    public String getNome() { 
    	return nome; 
    	}
    @Override 
    public String toString() { 
    	return nome; 
    	}
}
