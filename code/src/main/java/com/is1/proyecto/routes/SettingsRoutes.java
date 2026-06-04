package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.SettingsController;
import com.is1.proyecto.services.SettingsService;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class SettingsRoutes {

    private final SettingsController controller;

    public SettingsRoutes() {
        // Acá se ensamblan las dependencias: Service → Controller
        SettingsService service = new SettingsService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

        controller = new SettingsController(service, templateEngine);
    }

    public void register() {
        get("/settings", controller::show);
        post("/settings/update", controller::updateProfile);
        post("/settings/password", controller::updatePassword);
    }
}