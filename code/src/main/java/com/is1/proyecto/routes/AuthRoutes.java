package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.AuthController;
import com.is1.proyecto.services.AuthService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class AuthRoutes {

    private final AuthController controller;

    public AuthRoutes(AuthController controller) {
        this.controller = controller;
    }

    // -----------------------------------------------------------
    // Rutas de autenticación
    // -----------------------------------------------------------
    public void register() {
        get("/",            controller::showLoginForm);
        get("/login",       controller::showLoginForm);
        post("/login",      controller::login);
        get("/login/role",  controller::showRoleSelector);
        post("/login/role", controller::selectRole);
        post("/logout",     controller::logout);
    }
}