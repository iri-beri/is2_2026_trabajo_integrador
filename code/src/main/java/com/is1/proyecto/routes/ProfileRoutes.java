package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.ProfileController;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;

public class ProfileRoutes {

    private final ProfileController controller;

    public ProfileRoutes() {
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        controller = new ProfileController(templateEngine);
    }

    public void register() {
        get("/profile", controller::show);
    }
}