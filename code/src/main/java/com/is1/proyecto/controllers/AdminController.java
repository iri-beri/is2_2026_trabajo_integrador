package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Role;

import com.is1.proyecto.services.AuthService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.PersonCreateDTO;
import com.is1.proyecto.validators.UserValidator;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class AdminController extends BaseController {

    private final AuthService authService;

    public AdminController(AuthService authService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.authService = authService;
    }

    // -----------------------------------------------------------
    // GET /admin/create
    // -----------------------------------------------------------
    public String showCreateAdminForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model,"user_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /admin/new
    // -----------------------------------------------------------
    public String createAdmin(Request req, Response res) {

        PersonCreateDTO dto = buildAdminDTO(req);

        try {

            UserValidator.validate(dto);

            Person person = authService.createPerson(dto);

            loginUser(req, person);

            redirectToSuccess(res, person.getName(), "Administrador creado exitosamente", true);

        } catch (ServiceException e) {

            res.status(e.getStatusCode());

            res.redirect("/admin/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            res.status(500);

            res.redirect("/admin/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    private PersonCreateDTO buildAdminDTO(Request req) {

        PersonCreateDTO dto = new PersonCreateDTO();

        dto.dni = req.queryParams("dni");
        dto.name = req.queryParams("name");
        dto.surname = req.queryParams("surname");
        dto.username = req.queryParams("username");
        dto.email = req.queryParams("email");
        dto.cellphone = req.queryParams("cellphone");
        dto.birthdate = req.queryParams("birthdate");
        dto.password = req.queryParams("password");
        dto.role = Role.ADMIN;

        return dto;
    }
}