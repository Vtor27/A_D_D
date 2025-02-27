package es.florida;

public class Coche {
	int id, año;
	String marca, modelo, color;

	public Coche(int id, int año, String marca, String modelo, String color) {
		super();
		this.id = id;	
		this.año = año;
		this.marca = marca;
		this.modelo = modelo;
		this.color = color;
	}
	
	public Coche(int año, String marca, String modelo, String color) {
		super();
		this.año = año;
		this.marca = marca;
		this.modelo = modelo;
		this.color = color;
	}

	public Coche() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAño() {
		return año;
	}

	public void setAño(int año) {
		this.año = año;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Coche [id=" + id + ", año=" + año + ", marca=" + marca + ", modelo=" + modelo + ", color=" + color
				+ "]";
	}
}
