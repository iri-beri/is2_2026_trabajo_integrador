package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.CareerStudentController;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.services.CareerStudentService;

import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

public class CareerStudentRoutes {

    public static void register() {

        CareerStudentService service = new CareerStudentService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        CareerStudentController controller =
            new CareerStudentController(service, templateEngine);

        // Filtro ADMIN
        Spark.before("/career-student/*", (req, res) -> requireAdmin(req, res));

        // Rutas
        Spark.get("/career-student/enroll",        controller::showForm);
        Spark.post("/career-student/enroll",        controller::enroll);
        Spark.get("/career-student/confirmation",   controller::showConfirmation);
    }

    private static void requireAdmin(spark.Request req, spark.Response res) {

        Boolean loggedIn = req.session().attribute("loggedIn");

        if (loggedIn == null || !loggedIn) {
            res.redirect("/login");
            Spark.halt(302);
        }

        Long userId = req.session().attribute("userId");
        Person person = Person.findById(userId);

        if (person == null || !person.isAdmin()) {
            res.redirect("/dashboard?error=No+tenés+permisos+para+acceder+a+esa+sección.");
            Spark.halt(302);
        }
    }
}