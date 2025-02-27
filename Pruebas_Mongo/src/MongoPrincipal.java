import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoPrincipal {

	static MongoCollection<Document> coleccion;
	
	public static void insertarCochesDePrueba() {
	    // Verificar si la colección ya tiene documentos
	    if (coleccion.countDocuments() > 0) {
	        System.out.println("No se insertaron coches de prueba porque la base de datos ya contiene datos.");
	        return;
	    }

	    Document coche1 = new Document("Id", 1)
	            .append("Marca", "Toyota")
	            .append("Modelo", "Corolla")
	            .append("Color", "Rojo")
	            .append("Año", 2020);

	    Document coche2 = new Document("Id", 2)
	            .append("Marca", "Honda")
	            .append("Modelo", "Civic")
	            .append("Color", "Azul")
	            .append("Año", 2019);

	    Document coche3 = new Document("Id", 3)
	            .append("Marca", "Ford")
	            .append("Modelo", "Mustang")
	            .append("Color", "Negro")
	            .append("Año", 2021);

	    Document coche4 = new Document("Id", 4)
	            .append("Marca", "Chevrolet")
	            .append("Modelo", "Camaro")
	            .append("Color", "Amarillo")
	            .append("Año", 2018);

	    Document coche5 = new Document("Id", 5)
	            .append("Marca", "BMW")
	            .append("Modelo", "Serie 3")
	            .append("Color", "Blanco")
	            .append("Año", 2022);

	    coleccion.insertMany(Arrays.asList(coche1, coche2, coche3, coche4, coche5));

	    System.out.println("Se han insertado 5 coches de prueba correctamente con ID autoincremental.");
	}



	public static void main(String[] args) throws InterruptedException {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);// e.g. or Log.Warning, etc...

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase dataBase = mongoClient.getDatabase("concesionario");
		coleccion = dataBase.getCollection("Coches");

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		String id;

		while (opcion != 6) {
			System.out.println("\n\n=================================================");
			System.out.println("                 MENÚ CONCESIONARIO");
			System.out.println("=================================================");
			System.out.println("1. Mostrar todos los coches del concesionario.");
			System.out.println("2. Mostrar información detallada de un coche.");
			System.out.println("3. Insertar un nuevo coche.");
			System.out.println("4. Actualizar coche.");
			System.out.println("5. Borrar coche.");
			System.out.println("6. Cerrar concesionario");
			System.out.println("7. Inserta coches para probar la db");
			System.out.print("\n >>> Elige una opcion: ");
			opcion = Integer.parseInt(scanner.nextLine());
			switch (opcion) {
			case 1: {
//					mostrarTodos();
				break;
			}
			case 2: {
//					System.out.println(">>> Indica el número del libro a mostrar: ");
//					id = scanner.nextLine();
//					mostrarLibro(id);
				break;
			}
			case 3: {
//					crear();
				break;
			}
			case 4: {
//					System.out.println(">>> Indica el número del libro a actualizar: ");
//					id = scanner.nextLine();
//					actualizarLibro(id);
				break;
			}
			case 5: {
//					System.out.println(">>> Indica el número del libro a borrar: ");
//					id = scanner.nextLine();
//					borrarLibro(id);
				break;
			}
			case 6: {
				System.out.println(">>> Saliendo...");
				scanner.close();
				mongoClient.close();
				break;
			}
			case 7: {
				insertarCochesDePrueba();
				break;
			}
			default:
				break;
			}
		}
	}
}
