package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.CareerController;
import com.is1.proyecto.services.CareerService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class CareerRoutes {

    private final CareerController controller;

    public CareerRoutes() {
        CareerService service = new CareerService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        this.controller = new CareerController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Registra todas las rutas relacionadas a Career en Spark
    // -----------------------------------------------------------
    public void register() {
        get("/career/create", controller::showCreateCareerForm);
        post("/career/new",   controller::createCareer);
    }
}