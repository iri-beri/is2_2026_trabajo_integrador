package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.AdminController;
import com.is1.proyecto.services.AuthService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class AdminRoutes {

    private final AdminController controller;

    public AdminRoutes(AdminController controller) {
        this.controller = controller;
    }

    // -----------------------------------------------------------
    // Rutas de administración
    // -----------------------------------------------------------
    public void register() {
        get("/admin/create",  controller::showCreateAdminForm);
        get("/user/created",  controller::showUserCreated);
        get("/dashboard",     controller::showDashboard);
        post("/admin/new",    controller::createAdmin);
    }
}
