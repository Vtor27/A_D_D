package es.florida;

public class Opinion {
	int id;
	String titulo, usuario, opinion;
	
	public Opinion(int id, String titulo, String usuario, String opinion) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.usuario = usuario;
		this.opinion = opinion;
	}
	
	public Opinion(String titulo, String usuario, String opinion) {
		super();
		this.titulo = titulo;
		this.usuario = usuario;
		this.opinion = opinion;
	}
	
	public Opinion() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	public String mostrarOpiniones() {
		return ("  - " + usuario + ": " + opinion);
	}
}
