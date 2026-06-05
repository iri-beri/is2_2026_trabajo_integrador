package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Career;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.CareerService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.CareerCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CareerController extends BaseController {

    private final CareerService careerService;

    public CareerController(CareerService careerService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.careerService = careerService;
    }

    // -----------------------------------------------------------
    // GET /careers
    // Lista todas las carreras
    // -----------------------------------------------------------
    public String listCareers(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        List<Career> careers = careerService.getAllCareers();

        // Convertimos a lista de maps para Mustache
        List<Map<String, Object>> careerList = new ArrayList<>();
        for (Career c : careers) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id",   c.getLongId());
            entry.put("code", c.getCode());
            entry.put("name", c.getName());
            careerList.add(entry);
        }

        model.put("careers",      careerList);
        model.put("hasCareers",   !careerList.isEmpty());

        return templateEngine.render(new ModelAndView(model, "career_list.mustache"));
    }

    // -----------------------------------------------------------
    // GET /careers/:id/subjects
    // Detalle de materias de una carrera
    // -----------------------------------------------------------
    public String showCareerSubjects(Request req, Response res) {

        Integer careerId = Integer.parseInt(req.params(":id"));

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        try {
            Career career = careerService.findById(careerId);

            List<Subject> assigned    = careerService.getSubjectsForCareer(careerId);
            List<Subject> available   = careerService.getSubjectsNotInCareer(careerId);

            model.put("careerId",   career.getLongId());
            model.put("careerName", career.getName());
            model.put("careerCode", career.getCode());

            model.put("assignedSubjects",  buildSubjectMaps(assigned,  careerId, true));
            model.put("availableSubjects", buildSubjectMaps(available, careerId, false));

            model.put("hasAssigned",  !assigned.isEmpty());
            model.put("hasAvailable", !available.isEmpty());

        } catch (ServiceException e) {
            res.redirect("/careers?error=" + encode(e.getMessage()));
            return "";
        }

        return templateEngine.render(new ModelAndView(model, "career_subjects.mustache"));
    }

    // -----------------------------------------------------------
    // POST /careers/:id/subjects/add
    // Agrega una materia a la carrera
    // -----------------------------------------------------------
    public String addSubjectToCareer(Request req, Response res) {

        Integer careerId  = Integer.parseInt(req.params(":id"));
        Integer subjectId;

        try {
            subjectId = Integer.parseInt(req.queryParams("subject_id"));
        } catch (NumberFormatException e) {
            res.redirect("/careers/" + careerId + "/subjects?error=" + encode("Materia inválida."));
            return "";
        }

        try {
            careerService.addSubjectToCareer(careerId, subjectId);
            res.redirect("/careers/" + careerId + "/subjects?success=" + encode("Materia agregada correctamente."));
        } catch (ServiceException e) {
            res.status(e.getStatusCode());
            res.redirect("/careers/" + careerId + "/subjects?error=" + encode(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            res.redirect("/careers/" + careerId + "/subjects?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // POST /careers/:id/subjects/:subjectId/remove
    // Quita una materia de la carrera
    // -----------------------------------------------------------
    public String removeSubjectFromCareer(Request req, Response res) {

        Integer careerId  = Integer.parseInt(req.params(":id"));
        Integer subjectId = Integer.parseInt(req.params(":subjectId"));

        try {
            careerService.removeSubjectFromCareer(careerId, subjectId);
            res.redirect("/careers/" + careerId + "/subjects?success=" + encode("Materia removida correctamente."));
        } catch (ServiceException e) {
            res.status(e.getStatusCode());
            res.redirect("/careers/" + careerId + "/subjects?error=" + encode(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            res.redirect("/careers/" + careerId + "/subjects?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
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
    // HELPERS
    // -----------------------------------------------------------
    private List<Map<String, Object>> buildSubjectMaps(List<Subject> subjects,
                                                        Integer careerId,
                                                        boolean isAssigned) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Subject s : subjects) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id",       s.getLongId());
            entry.put("code",     s.getCode());
            entry.put("name",     s.getName());
            entry.put("hours",    s.getHours());
            entry.put("careerId", careerId);
            entry.put("assigned", isAssigned);
            list.add(entry);
        }
        return list;
    }

    private CareerCreateDTO buildCareerDTO(Request req) {

        CareerCreateDTO dto = new CareerCreateDTO();

        String codeParam = req.queryParams("code");

        dto.code = codeParam != null ? Integer.parseInt(codeParam) : null;
        dto.name = req.queryParams("name");

        return dto;
    }
}
