package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.ProfessorSubjectService;
import com.is1.proyecto.services.ServiceException;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorSubjectController extends BaseController {

    private final ProfessorSubjectService professorSubjectService;

    public ProfessorSubjectController(ProfessorSubjectService professorSubjectService,
                                      MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.professorSubjectService = professorSubjectService;
    }

    // -----------------------------------------------------------
    // GET /admin/professor/:professorId/subjects
    // Muestra las materias asignadas y las disponibles para agregar.
    // -----------------------------------------------------------
    public String showSubjects(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        try {

            Long personId = Long.parseLong(req.params(":professorId"));
            Professor professor = professorSubjectService.findProfessorByPersonId(personId);
            Person person = professorSubjectService.findPersonByProfessor(professor);

            model.put("professorId",      personId);
            model.put("professorName",    person.getString("name"));
            model.put("professorSurname", person.getString("surname"));
            model.put("professorDni",     person.getString("dni"));

            List<Subject> assigned  = professorSubjectService.getAssignedSubjects(personId);
            List<Subject> available = professorSubjectService.getAvailableSubjects(personId);

            model.put("assignedSubjects",  buildSubjectRows(assigned,  personId));
            model.put("availableSubjects", buildSubjectRows(available, personId));

        } catch (ServiceException e) {

            model.put("errorMessage", e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
            model.put("errorMessage", "Error interno. Intente de nuevo.");
        }

        return templateEngine.render(new ModelAndView(model, "professor_subjects.mustache"));
    }

    // -----------------------------------------------------------
    // POST /admin/professor/:professorId/subjects/add
    // -----------------------------------------------------------
    public String addSubject(Request req, Response res) {

        Long personId = Long.parseLong(req.params(":professorId"));
        String base   = "/admin/professor/" + personId + "/subjects";

        try {

            Long subjectId = Long.parseLong(req.queryParams("subjectId"));
            professorSubjectService.addSubject(personId, subjectId);
            res.redirect(base + "?success=" + encode("Materia agregada correctamente."));

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect(base + "?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            e.printStackTrace();
            res.status(500);
            res.redirect(base + "?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // POST /admin/professor/:professorId/subjects/remove
    // -----------------------------------------------------------
    public String removeSubject(Request req, Response res) {

        Long personId = Long.parseLong(req.params(":professorId"));
        String base   = "/admin/professor/" + personId + "/subjects";

        try {

            Long subjectId = Long.parseLong(req.queryParams("subjectId"));
            professorSubjectService.removeSubject(personId, subjectId);
            res.redirect(base + "?success=" + encode("Materia quitada correctamente."));

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect(base + "?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            e.printStackTrace();
            res.status(500);
            res.redirect(base + "?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // Construye la lista de mapas que Mustache puede iterar,
    // con la lógica first/last que usa el proyecto para las tablas.
    // -----------------------------------------------------------
    private List<Map<String, Object>> buildSubjectRows(List<Subject> subjects, Long professorId) {

        List<Map<String, Object>> rows = new ArrayList<>();

        for (int i = 0; i < subjects.size(); i++) {

            Subject s = subjects.get(i);
            Map<String, Object> row = new HashMap<>();

            row.put("subjectId",      s.getLongId());
            row.put("professorId",    professorId);
            row.put("code",           s.getCode());
            row.put("name",           s.getName());
            row.put("hours",          s.getHours());
            row.put("courseSyllabus", s.getCourseSyllabus() != null ? s.getCourseSyllabus() : "—");
            row.put("first",          i == 0);
            row.put("last",           i == subjects.size() - 1);

            rows.add(row);
        }

        return rows;
    }
}
