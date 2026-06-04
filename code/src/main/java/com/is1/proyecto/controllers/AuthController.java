package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.services.AuthService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.PersonLoginDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.authService = authService;
    }

    // -----------------------------------------------------------
    // GET /login
    // -----------------------------------------------------------
    public String showLoginForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "login.mustache"));
    }

    // -----------------------------------------------------------
    // POST /login
    // -----------------------------------------------------------
    public String login(Request req, Response res) {
        PersonLoginDTO dto = new PersonLoginDTO();

        dto.username = req.queryParams("username");
        dto.password = req.queryParams("password");

        try {

            Person person = authService.login(dto);
            loginUser(req, person);
            res.redirect("/dashboard");

        } catch (ServiceException e) {

            res.status(e.getStatusCode());

            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", e.getMessage());

            return templateEngine.render(new ModelAndView(model, "login.mustache"));

        } catch (Exception e) {

            res.status(500);

            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", "Error interno. Intente de nuevo.");

            return templateEngine.render(new ModelAndView(model, "login.mustache"));
        }

        return "";
    }

    // -----------------------------------------------------------
    // GET /logout
    // -----------------------------------------------------------
    public String logout(Request req, Response res) {

        req.session().invalidate();
        res.redirect("/login");

        return "";
    }
}