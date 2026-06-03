package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class DashboardController extends BaseController {

    public DashboardController(MustacheTemplateEngine templateEngine) {
        super(templateEngine);
    }

    public String show(Request req, Response res) {
        
        Long userId = req.session().attribute("userId");

        if (userId == null) {
            res.redirect("/login");
            return null;
        }

        Person person = Person.findById(userId);

        if (person == null) {
            throw new RuntimeException("No existe Person con id = " + userId);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("username", person.getUsername());
        model.put("isAdmin", person.isAdmin());
        model.put("isProfessor", person.isProfessor());
        model.put("isStudent", person.isStudent());

        // Mensaje opcional
        String successMessage = req.queryParams("message");

        if (successMessage != null) {
            model.put("successMessage", successMessage);
        }

        return templateEngine.render(new ModelAndView(model, "dashboard.mustache"));
    }
}