package es.florida;

public class Pelicula {
	int id, anyo;
	String titulo, director;
	
	public Pelicula(int id, int anyo, String titulo, String director) {
		super();
		this.id = id;
		this.anyo = anyo;
		this.titulo = titulo;
		this.director = director;
	}
	
	public Pelicula(int anyo, String titulo, String director) {
		super();
		this.anyo = anyo;
		this.titulo = titulo;
		this.director = director;
	}
	
	public Pelicula() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAnyo() {
		return anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}
	
	public String mostrarDatos() {
		return (String.valueOf(id) + ". " + titulo + "(" + director + ", " + anyo);
	}
}
