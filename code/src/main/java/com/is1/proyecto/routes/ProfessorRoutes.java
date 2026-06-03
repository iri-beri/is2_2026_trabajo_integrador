package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.ProfessorController;
import com.is1.proyecto.services.ProfessorService;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class ProfessorRoutes {

    private final ProfessorController controller;

    public ProfessorRoutes() {
        // Acá se ensamblan las dependencias: Service → Controller
        ProfessorService service = new ProfessorService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

        controller = new ProfessorController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Registra todas las rutas relacionadas a Professor en Spark
    // -----------------------------------------------------------
    public void register() {
        get("/professor/create", controller::showForm);
        post("/professor/new",   controller::create);
    }
}