package es.florida;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Principal {

	public static Session openConexion() {
		try {
			// Desactiva todos los logs de Java Util Logging (JUL)
			LogManager.getLogManager().reset();
			java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
			// Carga la configuración y crea una session factory.
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
//			configuration.addResource("coche.hbm.xml");	Busca el archivo con el nombre indicado pero solo en la carpeta resouces
			configuration.addFile("./src/es/florida/coche.hbm.xml");
			ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
					.build();
			SessionFactory sessionFactory = configuration.buildSessionFactory(registry);

			Session session = sessionFactory.openSession();
			return session;

		} catch (HibernateException e) {
			System.err.println("Error al inicializar Hibernate: " + e.getMessage());
			throw e;
		}
	}

	public static void closeConnection(Session session) {

		session.getTransaction().commit();
		session.clear();
		session.close();
	}

	public static void mostrarCoches() {
		Session session = openConexion();
		session.beginTransaction();

		System.out.println("\n --> Toda la lista de coches: ");
		List<Coche> listaCoches = new ArrayList<>();
		listaCoches = session.createQuery("FROM Coche", Coche.class).list();

		for (Object coche : listaCoches) {
			System.out.println(coche);
		}
		closeConnection(session);
	}

	public static void buscaCochePorId(int id) {
		Session session = openConexion();
		session.beginTransaction();

		Coche libroBuscado = (Coche) session.get(Coche.class, id);

		System.out.println(libroBuscado.toString());

		closeConnection(session);
	}

	public static void insertarNuevoCoche() {
		Session session = openConexion();
		session.beginTransaction();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Indica los datos del nuevo coche: ");
		System.out.print("Marca: ");
		String marca = scanner.nextLine();

		System.out.print("Modelo: ");
		String modelo = scanner.nextLine();

		System.out.print("Año: ");
		int año = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Color: ");
		String color = scanner.nextLine();

		
		Coche nuevoCoche = new Coche(año, modelo, marca, color);

		session.save(nuevoCoche);

		System.out.println("¡Coche introducido correctamente!");

		closeConnection(session);
	}
	
	public static void editarCoche(int id) {
		Session session = openConexion();
		session.beginTransaction();
		Scanner scanner = new Scanner(System.in);
		
		Coche coche = (Coche) session.get(Coche.class, id);
		System.out.println("Libro que se va a editar -> " + coche.toString());
		
		System.out.println("Indica los datos del coche a editar: ");
		System.out.print("Marca: ");
		String nuevaMarca = scanner.nextLine();
		if(!nuevaMarca.isEmpty()) {
			coche.setMarca(nuevaMarca);
		}
		
		System.out.print("Modelo: ");
		String nuevoModelo = scanner.nextLine();
		if(!nuevoModelo.isEmpty()) {
			coche.setModelo(nuevoModelo);
		}
		
		System.out.print("Año de matriculción: ");
		String nuevoAño = scanner.nextLine();
		if(!nuevoAño.isEmpty()) {
			coche.setAño(Integer.parseInt(nuevoAño));
		}
		
		System.out.print("Color: ");
		String nuevoColor = scanner.nextLine();
		if(!nuevoColor.isEmpty()) {
			coche.setColor(nuevoColor);
		}
		
		
		System.out.println("¡Coche actualizado!");
		
		closeConnection(session);
	}
	
	public static void eliminarCochePorId(int id) {
		Session session = openConexion();
		session.beginTransaction();
		
		Coche cocheEliminado = new Coche();
		cocheEliminado.setId(id);
		session.delete(cocheEliminado);
		
		closeConnection(session);
		
		System.out.println("Coche eliminado.");
	}

	public static void main(String[] args) {

		// Si los métodos no fueran statics deberia crear una instancia del objeto
		// Principal.

		Scanner scanner = new Scanner(System.in);
		String respuesta;

		do {
			// Menú de selección.
			System.out.println("-----------------------------------");
			System.out.println("------------Concesionari-----------");
			System.out.println("-----------------------------------");
			System.out.println("Selecciona una opción: ");
			System.out.println("1- Mostrar todos los coches.");
			System.out.println("2- Mostrar detalles de un coche.");
			System.out.println("3- Añadir coche nuevo");
			System.out.println("4- Editar coche(Indica la id).");
			System.out.println("5- Borrar coche(Indica su id).");
			System.out.println("6- Salir.");

			respuesta = scanner.nextLine();

			switch (respuesta) {
			case "1":
				mostrarCoches();
				break;

			case "2":
				System.out.println("Indica el id del coche a buscar -> ");
				int idCocheBuscar = scanner.nextInt();
				buscaCochePorId(idCocheBuscar);
				break;

			case "3":
				insertarNuevoCoche();
				break;

			case "4":
				System.out.println("Indica el id del coche a editar -> ");
				int idCocheEditar = scanner.nextInt();
				editarCoche(idCocheEditar);
				break;

			case "5":
				System.out.println("Indica el id del coche que quieres eliminar -> ");
				int idCocheEliminado = scanner.nextInt();
				eliminarCochePorId(idCocheEliminado);
				break;

			case "6":
				System.out.println("Saliendo...");
				break;
			default:
				break;
			}
		} while (respuesta != "6");

		scanner.close();
	}
}
