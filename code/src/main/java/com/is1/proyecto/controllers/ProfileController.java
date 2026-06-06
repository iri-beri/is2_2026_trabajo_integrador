package com.is1.proyecto.controllers;

import java.util.Map;
import java.util.HashMap;

import com.is1.proyecto.models.Person;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

public class ProfileController extends BaseController {

    public ProfileController(MustacheTemplateEngine templateEngine) {
        super(templateEngine);
    }

    // -----------------------------------------------------------
    // GET /profile
    // -----------------------------------------------------------
    public String show(Request req, Response res) {

        Long userId = req.session().attribute("userId");

        if (userId == null) {
            res.redirect("/login");
            return null;
        }

        Person person = Person.findById(userId);

        if (person == null) {
            res.redirect("/login");
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);
        loadPersonData(person, model);

        return templateEngine.render(new ModelAndView(model, "profile.mustache"));
    }
}
