package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.ProfessorSubjectController;
import com.is1.proyecto.services.ProfessorSubjectService;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

public class ProfessorSubjectRoutes {

    private final ProfessorSubjectController controller;

    public ProfessorSubjectRoutes() {
        ProfessorSubjectService service = new ProfessorSubjectService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

        controller = new ProfessorSubjectController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Registra las rutas de gestión de materias por profesor.
    // SOLO accesible por ADMIN (guard con before filter).
    // -----------------------------------------------------------
    public void register() {

        // Guard: solo administradores
        before("/admin/professor/:professorId/subjects",        (req, res) -> requireAdmin(req, res));
        before("/admin/professor/:professorId/subjects/add",    (req, res) -> requireAdmin(req, res));
        before("/admin/professor/:professorId/subjects/remove", (req, res) -> requireAdmin(req, res));

        get ("/admin/professor/:professorId/subjects",        controller::showSubjects);
        post("/admin/professor/:professorId/subjects/add",    controller::addSubject);
        post("/admin/professor/:professorId/subjects/remove", controller::removeSubject);
    }

    // -----------------------------------------------------------
    // Verifica que la sesión tenga rol ADMIN.
    // Si no, redirige a /login.
    // Ajusta el atributo de sesión al nombre que use tu proyecto
    // (ej: "role", "userRole", "loggedRole").
    // -----------------------------------------------------------
    private void requireAdmin(spark.Request req, spark.Response res) {

        String role = req.session().attribute("role"); // ← ajustar al nombre real del atributo

        if (role == null || !role.equals("ADMIN")) {
            res.redirect("/login");
            spark.Spark.halt(302);
        }
    }
}
