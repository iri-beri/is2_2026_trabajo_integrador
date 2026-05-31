package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.SubjectController;
import com.is1.proyecto.services.SubjectService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class SubjectRoutes {

    private final SubjectController controller;

    public SubjectRoutes() {
        SubjectService service = new SubjectService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        this.controller = new SubjectController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Registra todas las rutas relacionadas a Subject en Spark
    // -----------------------------------------------------------
    public void register() {
        get("/subject/create", controller::showCreateSubjectForm);
        post("/subject/new",   controller::createSubject);
    }
}
