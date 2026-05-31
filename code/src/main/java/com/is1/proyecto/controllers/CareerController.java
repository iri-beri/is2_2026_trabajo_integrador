package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Career;
import com.is1.proyecto.services.CareerService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.CareerCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class CareerController extends BaseController {

    private final CareerService careerService;

    public CareerController(CareerService careerService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.careerService = careerService;
    }

    // -----------------------------------------------------------
    // GET /career/create
    // -----------------------------------------------------------
    public String showCreateCareerForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "career_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /career/new
    // -----------------------------------------------------------
    public String createCareer(Request req, Response res) {

        CareerCreateDTO dto = buildCareerDTO(req);

        try {

            Career career = careerService.createCareer(dto);
            redirectToSuccess(res, career.getName(), "Carrera creada exitosamente.", false);

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/career/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            res.status(500);
            res.redirect("/career/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // BUILDER
    // -----------------------------------------------------------
    private CareerCreateDTO buildCareerDTO(Request req) {

        CareerCreateDTO dto = new CareerCreateDTO();

        String codeParam = req.queryParams("code");

        dto.code = codeParam != null ? Integer.parseInt(codeParam) : null;
        dto.name = req.queryParams("name");

        return dto;
    }
}