package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Subject;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorDashboardController extends BaseController {

    public ProfessorDashboardController(MustacheTemplateEngine templateEngine) {
        super(templateEngine);
    }

    // -----------------------------------------------------------
    // GET /dashboard/professor
    // -----------------------------------------------------------
    public String showDashboard(Request req, Response res) {

        String username  = req.session().attribute("currentUserUsername");
        Boolean loggedIn = req.session().attribute("loggedIn");
        String role      = req.session().attribute("role");  // ← "role", no "userRole"

        if (username == null || loggedIn == null || !loggedIn || !"PROFESSOR".equals(role)) {
            res.redirect("/login?error=" + encode("Acceso no autorizado."));
            return "";
        }

        Map<String, Object> model = new HashMap<>();
        model.put("username", username);

        return templateEngine.render(new ModelAndView(model, "dashboard_professor.mustache"));
    }

    // -----------------------------------------------------------
    // GET /professor/my-subjects
    // -----------------------------------------------------------
    public String showMySubjects(Request req, Response res) {

        String username  = req.session().attribute("currentUserUsername");
        Boolean loggedIn = req.session().attribute("loggedIn");
        String role      = req.session().attribute("role");  // ← "role", no "userRole"

        if (username == null || loggedIn == null || !loggedIn || !"PROFESSOR".equals(role)) {
            res.redirect("/login?error=" + encode("Acceso no autorizado."));
            return "";
        }

        Map<String, Object> model = new HashMap<>();
        model.put("username", username);

        try {

            Long userId = req.session().attribute("userId");

            List<Subject> subjects = Subject.find(
                "id IN (SELECT subject_id FROM professor_subjects WHERE professor_person_id = ?)",
                userId
            );

            List<Map<String, Object>> rows = new ArrayList<>();
            for (int i = 0; i < subjects.size(); i++) {
                Subject s = subjects.get(i);
                Map<String, Object> row = new HashMap<>();
                row.put("subjectId",      s.getLongId());
                row.put("code",           s.getCode());
                row.put("name",           s.getName());
                row.put("hours",          s.getHours());
                row.put("courseSyllabus", s.getCourseSyllabus() != null ? s.getCourseSyllabus() : "—");
                row.put("first",          i == 0);
                row.put("last",           i == subjects.size() - 1);
                rows.add(row);
            }

            model.put("subjects", rows);

        } catch (Exception e) {
            e.printStackTrace();
            model.put("errorMessage", "Error al cargar las materias. Intente de nuevo.");
        }

        return templateEngine.render(new ModelAndView(model, "my_subjects_professor.mustache"));
    }
}
