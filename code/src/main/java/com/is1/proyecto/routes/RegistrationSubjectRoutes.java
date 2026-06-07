package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.RegistrationSubjectController;
import com.is1.proyecto.services.RegistrationSubjectService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class RegistrationSubjectRoutes {

    private final RegistrationSubjectController controller;

    public RegistrationSubjectRoutes() {
        RegistrationSubjectService service = new RegistrationSubjectService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        this.controller = new RegistrationSubjectController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Rutas de inscripción de alumnos a materias
    // -----------------------------------------------------------
    public void register() {
        get("/registration",                     controller::showStudents); 
        get("/registration/create",              controller::showForm);
        post("/registration/new",                controller::create);
        get("/registration/confirmation/:id",    controller::showConfirmation);
    }
}