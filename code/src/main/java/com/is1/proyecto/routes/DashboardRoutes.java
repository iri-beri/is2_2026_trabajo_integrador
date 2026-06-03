package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.DashboardController;

import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.get;

public class DashboardRoutes {

    private final DashboardController controller;

    public DashboardRoutes() {

        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        
        controller = new DashboardController(templateEngine);
    }

    public void register() {
        get("/dashboard", controller::show);
    }
}
