package com.is1.proyecto;

import com.is1.proyecto.config.DBConfigSingleton;
import com.is1.proyecto.routes.*;
import com.is1.proyecto.controllers.AdminController;
import com.is1.proyecto.services.AuthService;

import spark.template.mustache.MustacheTemplateEngine;

import org.javalite.activejdbc.Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {

        // 1. Configuración del servidor
        port(8080);

        // 2. Instancia única de configuración de BD
        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();

        // 3. Inicializar esquema antes de recibir requests
        initializeDatabase(dbConfig);

        // 4. Filtros de conexión por request
        configureFilters(dbConfig);

        // 5. Registro de rutas

        // Dependencias compartidas
        AuthService authService = new AuthService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        AdminController adminController = new AdminController(authService, templateEngine);

        new AuthRoutes(adminController).register();
        new AdminRoutes(adminController).register();
        new ProfessorRoutes().register();
        new StudentRoutes().register();
        new SubjectRoutes().register();
    }

    // -----------------------------------------------------------
    // Abre y cierra la conexión en cada request.
    // Usa los métodos ya definidos en DBConfigSingleton.
    // -----------------------------------------------------------
    private static void configureFilters(DBConfigSingleton dbConfig) {

        before((req, res) -> {
            try {
                dbConfig.openConnection();
            } catch (Exception e) {
                System.err.println("Error al abrir conexión: " + e.getMessage());

                halt(500,
                    "{\"error\": \"Error interno: fallo al conectar a la base de datos.\"}");
            }
        });

        afterAfter((req, res) -> {
            try {

                if (Base.hasConnection()) {
                    dbConfig.closeConnection();
                }

            } catch (Exception e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        });
    }

    // -----------------------------------------------------------
    // Ejecuta el scheme.sql al arrancar.
    // Abre una conexión temporal solo para esto.
    // -----------------------------------------------------------
    private static void initializeDatabase(DBConfigSingleton dbConfig) {
        System.out.println("Iniciando esquema de base de datos...");

        try {
            dbConfig.openConnection();

            InputStream is = App.class.getClassLoader().getResourceAsStream("scheme.sql");

            if (is == null) {
                throw new IOException("scheme.sql no encontrado en el classpath.");
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }

            Base.exec(sb.toString());
            System.out.println("Esquema cargado exitosamente.");

        } catch (IOException e) {
            System.err.println("FATAL: No se pudo leer scheme.sql. " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("FATAL: Error al inicializar la base de datos. " + e.getMessage());
            System.exit(1);
        } finally {
            dbConfig.closeConnection();
        }
    }
}