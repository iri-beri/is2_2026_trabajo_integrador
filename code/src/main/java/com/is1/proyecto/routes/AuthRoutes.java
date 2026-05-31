package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.AdminController;

import static spark.Spark.get;
import static spark.Spark.post;

public class AuthRoutes {

    private final AdminController controller;

    public AuthRoutes(AdminController controller) {
        this.controller = controller;
    }

    // -----------------------------------------------------------
    // Rutas de autenticación
    // -----------------------------------------------------------
    public void register() {
        get("/",       controller::showLoginForm);
        get("/login",  controller::showLoginForm);
        post("/login", controller::login);
        post("/logout", controller::logout);
    }
}