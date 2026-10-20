package pt.ipp.upt;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GereUtilizadores {
	
	public static void criarConta() {
	    String nome;
	    do {
	        System.out.print("Nome: ");
	        nome = Main.sc.nextLine().trim();
	        if (nome.isEmpty()) {
	            System.out.println("Erro: O nome não pode estar vazio!");
	        }
	    } while (nome.isEmpty());
	    String email;
	    do {
	        System.out.print("Email: ");
	        email = Main.sc.nextLine().trim();
	        if (!email.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
	            System.out.println("Erro: Introduza um email válido!");
	        }
	    } while (!email.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"));
	    String password;
	    do {
	        System.out.print("Password: ");
	        password = Main.sc.nextLine();
	        if (password.isBlank()) {
	            System.out.println("Erro: A password não pode estar vazia!");
	        }
	    } while (password.isBlank());
	    String contacto;
	    do {
	        System.out.print("Contacto (9 dígitos): ");
	        contacto = Main.sc.nextLine().trim();
	        if (!contacto.matches("\\d{9}")) {
	            System.out.println("Erro: O contacto deve ter 9 dígitos!");
	        }
	    } while (!contacto.matches("\\d{9}"));
	    // Guardar utilizador no MySQL
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Utilizador existente = session.createQuery(
	                "FROM Utilizador WHERE email = :email",
	                Utilizador.class)
	                .setParameter("email", email)
	                .uniqueResult();
	        if (existente != null) {
	            System.out.println("Esse email já existe!");
	            return;
	        }
	        Utilizador novo = new Utilizador(0, nome, email, password, contacto,TipoUtilizador.UTILIZADOR);
	        Transaction transaction = session.beginTransaction();
	        try {
	            session.persist(novo);
	            transaction.commit();
	            System.out.println("Conta criada com sucesso!");
	        } catch (Exception e) {
	            if (transaction.isActive()) {
	                transaction.rollback();
	            }
	            System.out.println("Erro ao criar conta: " + e.getMessage());
	        }
	    } catch (Exception e) {
	        System.out.println("Erro ao aceder à base de dados: " + e.getMessage());
	    }
	}

	    public static void iniciarSessao() {
	        System.out.print("Email: ");
	        String email = Main.sc.nextLine();
	        System.out.print("Password: ");
	        String password = Main.sc.nextLine();
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            Utilizador u = session.createQuery(
	                "FROM Utilizador WHERE email = :email", Utilizador.class)
	                .setParameter("email", email)
	                .uniqueResult();
	            if (u != null && u.getPassword().equals(password)) {
	                Main.sessaoAtual = u;
	                System.out.println("Bem-vindo, " + u.getNome() + "!");
	            } else {
	                System.out.println("Email ou password errados!");
	            }
	        } catch (Exception e) {
	            System.out.println("Erro ao iniciar sessão: " + e.getMessage());
	        }
	    }

	    public static void terminarSessao() {
	        Main.sessaoAtual = null;
	        System.out.println("Sessão terminada.");
	    }
	    
	    public static void editarPerfil(Utilizador utilizador) {
	    	System.out.print("Novo nome [" + utilizador.getNome() + "]: ");
	    	String texto = Main.sc.nextLine();
	    	if (!texto.isEmpty()) {
	    		utilizador.setNome(texto);
	    	}
	    	System.out.print("Novo contacto (Enter para manter): ");
	    	texto = Main.sc.nextLine();
	    	if (!texto.isEmpty()) {
	    		utilizador.setContacto(texto);
	    	}
	    	System.out.print("Nova password (Enter para manter): ");
	    	texto = Main.sc.nextLine();
	    	if (!texto.isEmpty()) {
	    		utilizador.setPassword(texto);
	    	}
	    	Transaction transaction = null;
	    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    		transaction = session.beginTransaction();
	    		session.merge(utilizador);
	    		transaction.commit();
	    		System.out.println("Perfil atualizado com sucesso!");
	    	} catch (Exception e) {
	    		if (transaction != null && transaction.isActive()) {
	    			transaction.rollback();
	    		}
	    		System.out.println("Erro ao atualizar perfil: " + e.getMessage());
	    	}
	    }
}