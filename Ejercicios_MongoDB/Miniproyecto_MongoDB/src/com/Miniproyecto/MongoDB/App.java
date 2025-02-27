package com.Miniproyecto.MongoDB;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class App {

	static MongoCollection<Document> coleccion;

	public static void mostrarTodos() {
		MongoCursor<Document> cursor = coleccion.find().iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			System.out.println("ID: " + obj.getString("Id") + " - TITULO: " + obj.getString("Titulo"));
		}
	}

	public static void mostrarLibro(String id) {
		Bson query = eq("Id", id);
		MongoCursor<Document> cursor = coleccion.find(query).iterator();
		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());
			System.out.println("ID: " + obj.getString("Id"));
			System.out.println("TITULO: " + obj.getString("Titulo"));
			System.out.println("AUTOR: " + obj.getString("Autor"));
			System.out.println("AÑO NACIMIENTO: " + obj.getString("Año_nacimiento"));
			System.out.println("AÑO PUBLICACION: " + obj.getString("Año_publicacion"));
			System.out.println("EDITORIAL: " + obj.getString("Editorial"));
			System.out.println("Nº PAGINAS: " + obj.getString("Numero_paginas"));
		}
	}

	public static void crear() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("ID: ");
		String id = scanner.nextLine();
		System.out.println("TITULO: ");
		String titulo = scanner.nextLine();
		System.out.println("AUTOR: ");
		String autor = scanner.nextLine();
		System.out.println("AÑO NACIMIENTO: ");
		String año_nac = scanner.nextLine();
		System.out.println("AÑO PUBLICACION: ");
		String año_pub = scanner.nextLine();
		System.out.println("EDITORIAL: ");
		String editorial = scanner.nextLine();
		System.out.println("Nº PAGINAS ");
		String n_pag = scanner.nextLine();
		Document doc = new Document();
		doc.append("Id", id);
		doc.append("Titulo", titulo);
		doc.append("Autor", autor);
		doc.append("Año_nacimiento", año_nac);
		doc.append("Año_publicacion", año_pub);
		doc.append("Editorial", editorial);
		doc.append("Numero_paginas", n_pag);
		coleccion.insertOne(doc);
	}

	public static void actualizarLibro(String id) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Introduce datos nuevos(caracter '=' para dejarlo igual)");
		Bson query = eq("Id", id);
		System.out.println("TITULO: ");
		String nuevoCampo = scanner.nextLine();
		if (!nuevoCampo.equals("=")) {
			coleccion.updateOne(query, new Document("$set", new Document("Titulo", nuevoCampo)));
		}
		System.out.println("AUTOR: ");
		nuevoCampo = scanner.nextLine();
		if (!nuevoCampo.equals("=")) {
			coleccion.updateOne(query, new Document("$set", new Document("Autor", nuevoCampo)));
		}
		System.out.println("AÑO NACIMIENTO: ");
		nuevoCampo = scanner.nextLine();
		if (!nuevoCampo.equals("=")) {
			coleccion.updateOne(query, new Document("$set", new Document("Año_nacimiento", nuevoCampo)));
		}
		System.out.println("AÑO PUBLICACION: ");
		nuevoCampo = scanner.nextLine();
		if (!nuevoCampo.equals("=")) {
			coleccion.updateOne(query, new Document("$set", new Document("Año_publicacion", nuevoCampo)));
		}
		System.out.println("EDITORIAL: ");
		nuevoCampo = scanner.nextLine();
		if (!nuevoCampo.equals("=")) {
			coleccion.updateOne(query, new Document("$set", new Document("Editorial", nuevoCampo)));
		}
		System.out.println("Nº PAGINAS: ");
		nuevoCampo = scanner.nextLine();
		if (!nuevoCampo.equals("=")) {
			coleccion.updateOne(query, new Document("$set", new Document("Numero_paginas", nuevoCampo)));
		}
	}

	public static void borrarLibro(String id) {
		coleccion.deleteOne(eq("Id", id));
	}

	public static void insertarLibrosDePrueba() {
		Document libro1 = new Document("Id", "1").append("Titulo", "Cien años de soledad")
				.append("Autor", "Gabriel García Márquez").append("Año_nacimiento", "1927")
				.append("Año_publicacion", "1967").append("Editorial", "Sudamericana").append("Numero_paginas", "471");

		Document libro2 = new Document("Id", "2").append("Titulo", "1984").append("Autor", "George Orwell")
				.append("Año_nacimiento", "1903").append("Año_publicacion", "1949")
				.append("Editorial", "Secker & Warburg").append("Numero_paginas", "328");

		Document libro3 = new Document("Id", "3").append("Titulo", "El señor de los anillos")
				.append("Autor", "J.R.R. Tolkien").append("Año_nacimiento", "1892").append("Año_publicacion", "1954")
				.append("Editorial", "Allen & Unwin").append("Numero_paginas", "1178");

		Document libro4 = new Document("Id", "4").append("Titulo", "Don Quijote de la Mancha")
				.append("Autor", "Miguel de Cervantes").append("Año_nacimiento", "1547")
				.append("Año_publicacion", "1605").append("Editorial", "Francisco de Robles")
				.append("Numero_paginas", "863");

		Document libro5 = new Document("Id", "5").append("Titulo", "Orgullo y prejuicio").append("Autor", "Jane Austen")
				.append("Año_nacimiento", "1775").append("Año_publicacion", "1813")
				.append("Editorial", "T. Egerton, Whitehall").append("Numero_paginas", "432");

		coleccion.insertMany(Arrays.asList(libro1, libro2, libro3, libro4, libro5)); // Utiliza este array para meter
																						// todos los libros a la vez.
		System.out.println("Se han insertado 5 libros de prueba correctamente.");
	}

	public static void main(String[] args) throws InterruptedException {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);// e.g. or Log.Warning, etc...

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase dataBase = mongoClient.getDatabase("Biblioteca");
		coleccion = dataBase.getCollection("Libros");

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		String id;

		while (opcion != 6) {
			System.out.println("\n\n=================================================");
			System.out.println("                 MENÚ BIBLIOTECA");
			System.out.println("=================================================");
			System.out.println("1. Mostrar todos los títulos de la biblioteca.");
			System.out.println("2. Mostrar información detallada de un libro.");
			System.out.println("3. Crear un nuevo libro.");
			System.out.println("4. Actualizar libro.");
			System.out.println("5. Borrar libro.");
			System.out.println("6. Cerrar biblioteca");
			System.out.println("7. Inserta libros para probar la db");
			System.out.print("\n >>> Elige una opcion: ");
			opcion = Integer.parseInt(scanner.nextLine());
			switch (opcion) {
			case 1: {
				mostrarTodos();
				break;
			}
			case 2: {
				System.out.println(">>> Indica el número del libro a mostrar: ");
				id = scanner.nextLine();
				mostrarLibro(id);
				break;
			}
			case 3: {
				crear();
				break;
			}
			case 4: {
				System.out.println(">>> Indica el número del libro a actualizar: ");
				id = scanner.nextLine();
				actualizarLibro(id);
				break;
			}
			case 5: {
				System.out.println(">>> Indica el número del libro a borrar: ");
				id = scanner.nextLine();
				borrarLibro(id);
				break;
			}
			case 6: {
				System.out.println(">>> Saliendo...");
				scanner.close();
				mongoClient.close();
				break;
			}
			case 7: {
				insertarLibrosDePrueba();
				break;
			}
			default:
				break;
			}
		}
	}
}

////📌 Comparaciones básicas
//Bson igual = eq("Id", "1"); // ID igual a "1"
//Bson distinto = ne("Id", "1"); // ID distinto de "1"
//Bson mayorQue = gt("Numero_paginas", 300); // Más de 300 páginas
//Bson mayorOIgual = gte("Año_publicacion", 2000); // Publicado desde 2000
//Bson menorQue = lt("Año_nacimiento", 1900); // Autores nacidos antes de 1900
//Bson menorOIgual = lte("Numero_paginas", 500); // Hasta 500 páginas
//
//// 📌 Operadores lógicos
//Bson andQuery = and(eq("Autor", "George Orwell"), gte("Año_publicacion", 1940)); // Autor "Orwell" y desde 1940
//Bson orQuery = or(eq("Titulo", "1984"), eq("Titulo", "Cien años de soledad")); // "1984" o "Cien años de soledad"
//Bson notQuery = not(eq("Editorial", "Sudamericana")); // Editorial diferente de "Sudamericana"
//
//// 📌 Consultas con listas (Arrays)
//Bson inQuery = in("Id", Arrays.asList("1", "2", "3")); // ID dentro de [1,2,3]
//Bson ninQuery = nin("Titulo", Arrays.asList("1984", "Don Quijote de la Mancha")); // Títulos que NO sean esos
//
//// 📌 Búsqueda por texto
//Bson regexQuery = regex("Titulo", "señor"); // Títulos que contengan "señor"
//Bson regexCaseInsensitive = regex("Autor", "jane", "i"); // Ignora mayúsculas/minúsculas
//
//// 📌 Combinación de filtros
//Bson filtroComplejo = and(
//    gte("Año_publicacion", 1950), 
//    gte("Numero_paginas", 300), 
//    or(eq("Autor", "George Orwell"), eq("Autor", "J.R.R. Tolkien"))
//); // Libros desde 1950, +300 páginas y autores "Orwell" o "Tolkien"
//}
