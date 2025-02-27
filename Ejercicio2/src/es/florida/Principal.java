package es.florida;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class Principal {

	static MongoCollection<Document> coleccionCoches;
	static MongoCollection<Document> coleccionRebajas;
	static MongoCollection<Document> coleccionImagenes;
	static MongoDatabase dataBase;


	public static void mostrarTodosSeleccionado() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Elige el tipo de combustible --> ");
		String tipoCombustible = scanner.nextLine();
		if(tipoCombustible.equals("combustion") || tipoCombustible.equals("hibrido") || tipoCombustible.equals("electrico")) {
			System.out.println(tipoCombustible);
			
			System.out.println("Elige el rango de precios: --> ('<=' o '>=)");
			String rango = scanner.nextLine();
			
			System.out.println("Elige el precio: --> ");
			int precio = scanner.nextInt();
			
			if(rango.equals("<=")) {
				System.out.println("entra");
				Bson queryPrecioMayor = and(eq("tipo", tipoCombustible),
						lte("precio", precio));
				MongoCursor<Document> cursor = coleccionCoches.find(queryPrecioMayor).iterator();
				while (cursor.hasNext()) {
					JSONObject obj = new JSONObject(cursor.next().toJson());
					System.out.println("- " + obj.getString("marca") + obj.getString("modelo") + "(" + obj.getString("tipo") + ") > " + obj.getInt("precio") + " eur");
				}
				
			} else if (rango.equals(">=")) {
				Bson queryPrecioMayor = and(eq("tipo", tipoCombustible),
						gte("precio", precio));
				MongoCursor<Document> cursor = coleccionCoches.find(queryPrecioMayor).iterator();
				while (cursor.hasNext()) {
					JSONObject obj = new JSONObject(cursor.next().toJson());
					System.out.println("- " + obj.getString("marca") + " " + obj.getString("modelo") + " (" + obj.getString("tipo") + ") > " + obj.getInt("precio") + " eur");
				}
			}
		}
	}
	
	public static String codificarImagenBase64(String rutaImagen) {
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(rutaImagen));
			return Base64.encodeBase64String(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean decodificarBase64AImagen(String base64String, String rutaDestino) {
		try {
			byte[] decodedBytes = Base64.decodeBase64(base64String);
			Files.write(Paths.get(rutaDestino), decodedBytes);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String guardarImagenEnDB(String rutaImagen) {

		String imagenBase64 = codificarImagenBase64(rutaImagen);
		if (imagenBase64 == null) {
			System.err.println("Error al codificar la imagen.");
			return null;
		}

		Document imagenDoc = new Document("base64", imagenBase64);
		coleccionImagenes.insertOne(imagenDoc);

		return imagenDoc.getObjectId("_id").toString();
	}
	
	

	public static void main(String[] args) {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);// e.g. or Log.Warning, etc...

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase dataBase = mongoClient.getDatabase("concesionario");
		coleccionCoches = dataBase.getCollection("coches");
		coleccionRebajas = dataBase.getCollection("rebajas");
		coleccionImagenes = dataBase.getCollection("Imagenes");

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		String id;

		while (opcion != 6) {
			System.out.println("\n\n=================================================");
			System.out.println("                 MENÚ CONCESIONARIO");
			System.out.println("=================================================");
			System.out.println("1. Mostrar todos los coches del concesionario.");
			System.out.println("1. Modificar precio coche, por id.");

			opcion = Integer.parseInt(scanner.nextLine());
			switch (opcion) {
			case 1: {
					mostrarTodosSeleccionado();
				break;
			}
			case 2: {
				
			break;
		}
			case 6: {
				System.out.println(">>> Saliendo...");
				scanner.close();
				mongoClient.close();
				break;
			}
			case 7: {
				break;
			}
			default:
				break;
			}
		}
	}

}
