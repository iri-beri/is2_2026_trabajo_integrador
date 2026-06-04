package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.AuthController;
import com.is1.proyecto.services.AuthService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class AuthRoutes {

    private final AuthController controller;

    public AuthRoutes() {

        AuthService service = new AuthService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

        controller = new AuthController( service, templateEngine);
    }

    public void register() {

        get("/", controller::showLoginForm);

        get("/login", controller::showLoginForm);

        post("/login", controller::login);

        get("/logout", controller::logout);
    }
}