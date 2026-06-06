package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.SettingsService;
import com.is1.proyecto.services.dto.ProfileUpdateDTO;

import java.util.Map;
import java.util.HashMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

public class SettingsController extends BaseController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.settingsService = settingsService;
    }

    public String show(Request req, Response res) {

        Long userId = req.session().attribute("userId");
        
        if (userId == null) {
            res.redirect("/login");
            return null;
        }

        Person person = Person.findById(userId);
        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);
        loadPersonData(person, model);
        
        return templateEngine.render(new ModelAndView(model, "settings.mustache"));
    }

    public Object updateProfile(Request req, Response res) {
        try {
            ProfileUpdateDTO dto = buildDTO(req);
            settingsService.updateProfile(dto);
            res.redirect("/profile?message=" + encode("Perfil actualizado correctamente."));
        } catch (ServiceException e) {
            res.redirect("/profile?error=" + encode(e.getMessage()));
        }

        return null;
    }

    public Object updatePassword(Request req, Response res) {
        Long userId = req.session().attribute("userId");

        try {
            settingsService.changePassword(userId, 
                                    req.queryParams("currentPassword"),
                                    req.queryParams("newPassword"),
                                    req.queryParams("confirmPassword"));

            res.redirect("/profile?message=" + encode("Contraseña actualizada correctamente."));
        } catch (ServiceException e) {
            res.redirect("/profile?error=" + encode(e.getMessage()));
        }

        return null;
    }

    private ProfileUpdateDTO buildDTO(Request req) {

        ProfileUpdateDTO dto = new ProfileUpdateDTO();

        dto.userId = req.session().attribute("userId");

        dto.name = req.queryParams("name");
        dto.surname = req.queryParams("surname");
        dto.email = req.queryParams("email");
        dto.cellphone = req.queryParams("cellphone");
        dto.birthdate = req.queryParams("birthdate");

        dto.degree = req.queryParams("degree");
        dto.graduateUniv = req.queryParams("graduate_univ");
        dto.position = req.queryParams("position");

        dto.birthplace = req.queryParams("birthplace");
        dto.townOfResidence = req.queryParams("town_of_residence");
        dto.contactRelative = req.queryParams("contact_relative");
        dto.contactCellphone = req.queryParams("contact_cellphone");

        return dto;
    }
}