package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.AdminController;

import static spark.Spark.get;
import static spark.Spark.post;

public class AdminRoutes {

    private final AdminController controller;

    public AdminRoutes(AdminController controller) {
        this.controller = controller;
    }

    // -----------------------------------------------------------
    // Rutas del administrador
    // -----------------------------------------------------------
    public void register() {
        get("/dashboard/admin", controller::showDashboard);
        get("/admin/create",    controller::showCreateAdminForm);
        get("/user/created",    controller::showUserCreated);
        post("/admin/new",      controller::createAdmin);
    }
}