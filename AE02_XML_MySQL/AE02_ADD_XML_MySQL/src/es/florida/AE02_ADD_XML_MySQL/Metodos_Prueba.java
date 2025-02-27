package es.florida.AE02_ADD_XML_MySQL;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class Metodos_Prueba {

	public List<Usuario> leerCSV(String file) {
		List<Usuario> usuarios = new ArrayList<Usuario>();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String linea = br.readLine(); // Leer la primera línea (puede ser encabezado)
			if (linea == null) {
				JOptionPane.showMessageDialog(null, "El archivo CSV está vacío.", "ERROR", JOptionPane.ERROR_MESSAGE);
				return usuarios;
			}

			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length == 4) { // Asegurar que tiene todas las columnas esperadas
					usuarios.add(new Usuario(datos[0], datos[1], datos[2], datos[3]));
				}
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return usuarios;
	}

	class Usuario {
		private String id;
		private String login;
		private String password;
		private String type;

		public Usuario(String id, String login, String password, String type) {
			this.id = id;
			this.login = login;
			this.password = password;
			this.type = type;
		}

		public String getId() {
			return id;
		}

		public String getLogin() {
			return login;
		}

		public String getPassword() {
			return password;
		}

		public String getType() {
			return type;
		}

		@Override
		public String toString() {
			return "Usuario{id='" + id + "', login='" + login + "', password='" + password + "', type='" + type + "'}";
		}
	}
}
