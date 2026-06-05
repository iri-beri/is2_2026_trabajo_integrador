package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.CareerController;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.services.CareerService;

import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

public class CareerRoutes {

    public static void register() {

        CareerService careerService = new CareerService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        CareerController controller = new CareerController(careerService, templateEngine);

        // -----------------------------------------------------------
        // Filtro de autenticación y autorización — ADMIN only
        // Se aplica a todas las rutas /careers/* y /career/*
        // -----------------------------------------------------------
        Spark.before("/careers",                                (req, res) -> requireAdmin(req, res));
        Spark.before("/careers/*",                              (req, res) -> requireAdmin(req, res));
        Spark.before("/career/create",                          (req, res) -> requireAdmin(req, res));
        Spark.before("/career/new",                             (req, res) -> requireAdmin(req, res));

        // -----------------------------------------------------------
        // Rutas de carreras
        // -----------------------------------------------------------
        Spark.get("/careers",                                   controller::listCareers);
        Spark.get("/careers/:id/subjects",                      controller::showCareerSubjects);
        Spark.post("/careers/:id/subjects/add",                 controller::addSubjectToCareer);
        Spark.post("/careers/:id/subjects/:subjectId/remove",   controller::removeSubjectFromCareer);

        Spark.get("/career/create",                             controller::showCreateCareerForm);
        Spark.post("/career/new",                               controller::createCareer);
    }

    // -----------------------------------------------------------
    // Verifica que haya una sesión activa y que el usuario sea ADMIN.
    // Si no → redirige a /login o /dashboard según el caso.
    // -----------------------------------------------------------
    private static void requireAdmin(spark.Request req, spark.Response res) {

        Boolean loggedIn = req.session().attribute("loggedIn");

        // Sin sesión → login
        if (loggedIn == null || !loggedIn) {
            res.redirect("/login");
            Spark.halt(302);
        }

        // Con sesión pero sin rol ADMIN → dashboard con error
        Long userId = req.session().attribute("userId");
        Person person = Person.findById(userId);

        if (person == null || !person.isAdmin()) {
            res.redirect("/dashboard?error=No+tenés+permisos+para+acceder+a+esa+sección.");
            Spark.halt(302);
        }
    }
}
