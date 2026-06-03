package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.AdminController;
import com.is1.proyecto.services.AuthService;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class AdminRoutes {

    private final AdminController controller;

    public AdminRoutes() {

        AuthService service = new AuthService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

        controller = new AdminController(service, templateEngine);
    }

    public void register() {

        get("/admin/create", controller::showCreateAdminForm);

        post("/admin/new", controller::createAdmin);
    }
}