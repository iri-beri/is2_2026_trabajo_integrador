package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.SubjectService;
import com.is1.proyecto.services.dto.SubjectCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class SubjectController extends BaseController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.subjectService = subjectService;
    }

    // -----------------------------------------------------------
    // GET /subject/create
    // -----------------------------------------------------------
    public String showCreateSubjectForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "subject_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /subject/new
    // -----------------------------------------------------------
    public String createSubject(Request req, Response res) {

        SubjectCreateDTO dto = buildSubjectDTO(req);

        try {

            Subject subject = subjectService.createSubject(dto);
            redirectToSuccess(res, subject.getName(), "Materia creada exitosamente.", false);

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/subject/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            res.status(500);
            res.redirect("/subject/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // BUILDER
    // -----------------------------------------------------------
    private SubjectCreateDTO buildSubjectDTO(Request req) {

        SubjectCreateDTO dto = new SubjectCreateDTO();

        String codeParam = req.queryParams("code");
        String hoursParam = req.queryParams("hours");

        dto.code          = codeParam != null ? Integer.parseInt(codeParam) : null;
        dto.name          = req.queryParams("name");
        dto.courseSyllabus = req.queryParams("course_syllabus");
        dto.hours         = hoursParam != null ? Integer.parseInt(hoursParam) : null;

        return dto;
    }
}