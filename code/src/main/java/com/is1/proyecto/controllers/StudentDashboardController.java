package com.is1.proyecto.controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class StudentDashboardController extends BaseController {

    public StudentDashboardController(MustacheTemplateEngine templateEngine) {
        super(templateEngine);
    }

    // -----------------------------------------------------------
    // GET /dashboard/student
    // -----------------------------------------------------------
    public String showDashboard(Request req, Response res) {

        String username  = req.session().attribute("currentUserUsername");
        Boolean loggedIn = req.session().attribute("loggedIn");
        String role      = req.session().attribute("role"); // ← "role", no "userRole"

        if (username == null || loggedIn == null || !loggedIn || !"STUDENT".equals(role)) {
            res.redirect("/login?error=" + encode("Acceso no autorizado."));
            return "";
        }

        Map<String, Object> model = new HashMap<>();
        model.put("username", username);

        return templateEngine.render(new ModelAndView(model, "dashboard_student.mustache"));
    }
}
