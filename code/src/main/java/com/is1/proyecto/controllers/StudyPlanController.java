package com.is1.proyecto.controllers;

import com.is1.proyecto.models.StudyPlan;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.StudyPlanService;
import com.is1.proyecto.services.dto.StudyPlanCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class StudyPlanController extends BaseController {

    private final StudyPlanService studyPlanService;

    public StudyPlanController(StudyPlanService studyPlanService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.studyPlanService = studyPlanService;
    }

    // -----------------------------------------------------------
    // GET /studyplan/create
    // -----------------------------------------------------------
    public String showCreateStudyPlanForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "studyplan_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /studyplan/new
    // -----------------------------------------------------------
    public String createStudyPlan(Request req, Response res) {

        StudyPlanCreateDTO dto = buildStudyPlanDTO(req);

        try {

            StudyPlan studyPlan = studyPlanService.createStudyPlan(dto);
            redirectToSuccess(res, studyPlan.getName(), "Plan de estudio creado exitosamente.", false);

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/studyplan/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            res.status(500);
            res.redirect("/studyplan/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // BUILDER
    // -----------------------------------------------------------
    private StudyPlanCreateDTO buildStudyPlanDTO(Request req) {

        StudyPlanCreateDTO dto = new StudyPlanCreateDTO();

        dto.version = req.queryParams("version");
        dto.name    = req.queryParams("name");

        return dto;
    }
}