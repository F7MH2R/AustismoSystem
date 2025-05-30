package com.autismo.neuroprevia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootApplication
public class NeuropreviaApplication {

	public static void main(String[] args) {
		crearBaseDatosSiNoExiste();
		SpringApplication.run(NeuropreviaApplication.class, args);
	}


	private static void crearBaseDatosSiNoExiste() {
		String url = "jdbc:postgresql://localhost:5432/postgres";
		String usuario = "postgres";
		String contraseña = "hola12345";

		try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
			ResultSet rs = conn.createStatement().executeQuery("SELECT 1 FROM pg_database WHERE datname='neuroprevia_db'");
			if (!rs.next()) {
				conn.createStatement().executeUpdate("CREATE DATABASE neuroprevia_db");
				System.out.println("✔ Base de datos 'neuroprevia_db' creada exitosamente.");
			} else {
				System.out.println("ℹ Base de datos 'neuroprevia_db' ya existe.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
