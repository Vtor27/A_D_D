import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.ByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class Principal {
	
	static MongoCollection<Document> coleccion;
	static MongoDatabase dataBase;

	/**
	 * Establece la conexión con MongoDB
	 */
	public static void conectarConDataBase() {
		if (dataBase == null) {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			dataBase = mongoClient.getDatabase("concesionario");
			coleccion = dataBase.getCollection("Coches");
			System.out.println("Conexión establecida con MongoDB.");
		}
	}

	/**
	 * Codifica una imagen en Base64
	 * @param rutaImagen Ruta de la imagen a codificar.
	 * @return Cadena Base64 de la imagen o null en caso de error.
	 */
	public static String codificarImagenBase64(String rutaImagen) {
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(rutaImagen));
			return Base64.encodeBase64String(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Decodifica una cadena Base64 y la guarda como un archivo de imagen.
	 * @param base64String Cadena en Base64 de la imagen.
	 * @param rutaDestino Ruta donde se guardará la imagen decodificada.
	 * @return true si se guarda correctamente, false si hay error.
	 */
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

	/**
	 * Guarda una imagen en MongoDB en formato Base64.
	 * @param rutaImagen Ruta de la imagen que se va a almacenar.
	 * @return ID del documento insertado en MongoDB o null en caso de error.
	 */
	public static String guardarImagenEnDB(String rutaImagen) {
		conectarConDataBase();

		String imagenBase64 = codificarImagenBase64(rutaImagen);
		if (imagenBase64 == null) {
			System.err.println("Error al codificar la imagen.");
			return null;
		}

		Document imagenDoc = new Document("base64", imagenBase64);
		coleccion.insertOne(imagenDoc);
		System.out.println("Imagen guardada en MongoDB con ID: " + imagenDoc.getObjectId("_id").toString());

		return imagenDoc.getObjectId("_id").toString();
	}

	/**
	 * Recupera una imagen desde MongoDB en formato Base64 utilizando su ID.
	 * @param idImagen ID de la imagen en MongoDB.
	 * @return Cadena Base64 de la imagen o null si no se encuentra.
	 */
	public static String obtenerImagenBase64DesdeDB(String idImagen) {
		conectarConDataBase();

		Document documento = coleccion.find(eq("_id", new ObjectId(idImagen))).first();
		if (documento != null) {
			return documento.getString("base64");
		} else {
			System.err.println("No se encontró la imagen con ID: " + idImagen);
			return null;
		}
	}

	/**
	 * Recupera una imagen desde MongoDB y la devuelve como un ImageIcon.
	 * @param idImagen ID de la imagen en MongoDB.
	 * @return ImageIcon de la imagen recuperada o null en caso de error.
	 */
	public static ImageIcon obtenerImagenDesdeDB(String idImagen) {
		String base64 = obtenerImagenBase64DesdeDB(idImagen);
		if (base64 == null) {
			return null;
		}

		byte[] btDataFile = Base64.decodeBase64(base64);

		try {
			BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(btDataFile));
			if (imagen != null) {
				Image scaledImagen = imagen.getScaledInstance(290, 437, Image.SCALE_SMOOTH);
				return new ImageIcon(scaledImagen);
			} else {
				System.err.println("La imagen no se pudo decodificar.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE); // Suprime mensajes innecesarios de MongoDB

		conectarConDataBase(); // Establece la conexión

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		String id;

		while (opcion != 4) {
			System.out.println("\n\n=================================================");
			System.out.println("                 MENÚ IMÁGENES");
			System.out.println("=================================================");
			System.out.println("1. Subir imagen a MongoDB.");
			System.out.println("2. Recuperar imagen desde MongoDB.");
			System.out.println("3. Decodificar imagen y guardarla en archivo.");
			System.out.println("4. Salir.");
			System.out.print("\n >>> Elige una opción: ");
			opcion = Integer.parseInt(scanner.nextLine());

			switch (opcion) {
				case 1: {
					System.out.print(">>> Ingresa la ruta de la imagen a subir: ");
					String ruta = scanner.nextLine();
					String idImagen = guardarImagenEnDB(ruta);
					if (idImagen != null) {
						System.out.println("Imagen almacenada con ID: " + idImagen);
					}
					break;
				}
				case 2: {
					System.out.print(">>> Ingresa el ID de la imagen a recuperar: ");
					id = scanner.nextLine();
					ImageIcon imagenRecuperada = obtenerImagenDesdeDB(id);
					if (imagenRecuperada != null) {
						System.out.println("Imagen recuperada correctamente.");
						 //Si quieres mostrar la imagen en un JLabel
						 JLabel label = new JLabel(imagenRecuperada);
						 JOptionPane.showMessageDialog(null, label, "Imagen Recuperada", JOptionPane.PLAIN_MESSAGE);
						 System.out.println("Cerrar imagen antes de continuar...");
					}
					break;
				}
				case 3: {
					System.out.print(">>> Ingresa el ID de la imagen a decodificar y guardar en archivo: ");
					id = scanner.nextLine();
					String base64 = obtenerImagenBase64DesdeDB(id);
					if (base64 != null) {
						System.out.print(">>> Ingresa la ruta destino para guardar la imagen: ");
						String rutaDestino = scanner.nextLine();
						boolean guardado = decodificarBase64AImagen(base64, rutaDestino);
						if (guardado) {
							System.out.println("Imagen guardada correctamente en: " + rutaDestino);
						}
					}
					break;
				}
				case 4: {
					System.out.println(">>> Saliendo...");
					scanner.close();
					break;
				}
				default:
					System.out.println("Opción inválida, intenta nuevamente.");
					break;
			}
		}
	}
}

//Leer carpeta
//File carpeta = new File("ruta/de/la/carpeta"); // Reemplaza con la ruta real
//if (carpeta.exists() && carpeta.isDirectory()) {
//    File[] archivos = carpeta.listFiles();
//    if (archivos != null) {
//        for (File archivo : archivos) {
//            System.out.println(archivo.getName());
//        }
//    }
//} else {
//    System.out.println("La carpeta no existe o no es un directorio.");
//}
//}

//Leer carpeta
//File carpeta = new File("ruta/de/la/carpeta");
//if (carpeta.exists() && carpeta.isDirectory()) {
//    String[] nombres = carpeta.list();
//    if (nombres != null) {
//        for (String nombre : nombres) {
//            System.out.println(nombre);
//        }
//    }
//} else {
//    System.out.println("La carpeta no existe o no es un directorio.");
//}
//}
