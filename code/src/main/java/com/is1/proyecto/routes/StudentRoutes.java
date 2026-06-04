package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.StudentController;
import com.is1.proyecto.services.StudentService;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class StudentRoutes {

    private final StudentController controller;

    public StudentRoutes() {
        // Acá se ensamblan las dependencias: Service → Controller
        StudentService service = new StudentService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

        controller = new StudentController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Registra todas las rutas relacionadas a Student en Spark
    // -----------------------------------------------------------
    public void register() {
        get("/student/create", controller::showForm);
        post("/student/new",   controller::create);
    }
}