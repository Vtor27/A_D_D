package es.florida;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class Principal {

	public static Session openConexion() {
		try {
//			// Desactiva todos los logs de Java Util Logging (JUL)
			LogManager.getLogManager().reset();
			java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
			// Carga la configuración y crea una session factory.
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
//			configuration.addResource("coche.hbm.xml");	Busca el archivo con el nombre indicado pero solo en la carpeta resouces
			configuration.addFile("./src/es/florida/Opinion.hbm.xml");
			configuration.addFile("./src/es/florida/Pelicula.hbm.xml");
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


	public static void mostrarFilmoteca() {
		Session session = openConexion();
		session.beginTransaction();
		
		List<Pelicula> peliculas = new ArrayList<>();
		peliculas = session.createQuery("FROM Pelicula", Pelicula.class).list();

		System.out.println("Mi Filmoteca: \n");
		for (Pelicula pelicula : peliculas) {
			System.out.println(pelicula.mostrarDatos());
		}
		System.out.println("   Opiniones: ");
		List<Opinion> opiniones = new ArrayList<>();
		opiniones = session.createQuery("FROM Opinion", Opinion.class).list();

		for (Opinion opinion : opiniones) {
			System.out.println(opinion.mostrarOpiniones());
		}
		
		
		closeConnection(session);
	}

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		String respuesta;

		do {
			// Menú de selección.
			System.out.println("\n\n\n-----------------------------------");
			System.out.println("------------Filmoteca-----------");
			System.out.println("-----------------------------------");
			System.out.println("Selecciona una opción: ");
			System.out.println("1- Mostrar peliculas y opiniones.");
			System.out.println("2- Mostrar detalles de un coche.");
			System.out.println("3- Añadir coche nuevo");
			System.out.println("4- Editar coche(Indica la id).");
			System.out.println("5- Borrar coche(Indica su id).");
			System.out.println("6- Salir. \n\n\n");

			respuesta = scanner.nextLine();

			switch (respuesta) {
			case "1":
				mostrarFilmoteca();
				break;

			case "2":
//				System.out.println("Indica el id del coche a buscar -> ");
//				int idCocheBuscar = scanner.nextInt();
//				buscaCochePorId(idCocheBuscar);
				break;

			case "3":
//				insertarNuevoCoche();
				break;

			case "4":
//				System.out.println("Indica el id del coche a editar -> ");
//				int idCocheEditar = scanner.nextInt();
//				editarCoche(idCocheEditar);
				break;

			case "5":
//				System.out.println("Indica el id del coche que quieres eliminar -> ");
//				int idCocheEliminado = scanner.nextInt();
//				eliminarCochePorId(idCocheEliminado);
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
