package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Grade;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.RegistrationSubject;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.GradeService;
import com.is1.proyecto.services.ServiceException;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeController extends BaseController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.gradeService = gradeService;
    }

    // -----------------------------------------------------------
    // GET /professor/subject/:subjectId/students
    // Lista alumnos inscriptos a la materia con sus notas.
    // -----------------------------------------------------------
    public String showStudents(Request req, Response res) {

        if (!isProfessor(req)) { res.redirect("/login"); return ""; }

        Long professorId = req.session().attribute("userId");
        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        try {
            Long subjectId = Long.parseLong(req.params(":subjectId"));

            Subject subject = Subject.findById(subjectId);
            if (subject == null) throw new ServiceException("Materia no encontrada.", 404);

            model.put("subjectId",   subjectId);
            model.put("subjectName", subject.getName());
            model.put("subjectCode", subject.getCode());

            List<RegistrationSubject> registrations = gradeService.getStudentsBySubject(subjectId);
            List<Map<String, Object>> rows = new ArrayList<>();

            for (int i = 0; i < registrations.size(); i++) {
                RegistrationSubject reg = registrations.get(i);
                Long studentId = reg.getStudentId();

                Student student = Student.findFirst("person_id = ?", studentId);
                Person person   = student != null ? Person.findById(student.getPersonId()) : null;

                // Buscar nota cargada por este profesor
                Grade existing = Grade.findFirst(
                    "student_id = ? AND subject_id = ? AND professor_id = ?",
                    studentId, subjectId, professorId
                );

                Map<String, Object> row = new HashMap<>();
                row.put("studentId",   studentId);
                row.put("subjectId",   subjectId);
                row.put("name",        person != null ? person.getString("name")    : "—");
                row.put("surname",     person != null ? person.getString("surname")  : "—");
                row.put("dni",         person != null ? person.getString("dni")      : "—");
                row.put("hasGrade",    existing != null);
                row.put("grade",       existing != null ? existing.getGrade()       : "");
                row.put("description", existing != null ? existing.getDescription() : "");
                row.put("gradeDate",   existing != null ? existing.getDate()        : "");
                row.put("enrollDate",  reg.getDate());
                row.put("first",       i == 0);
                row.put("last",        i == registrations.size() - 1);
                rows.add(row);
            }

            model.put("students", rows);

        } catch (ServiceException e) {
            model.put("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            model.put("errorMessage", "Error interno. Intente de nuevo.");
        }

        return templateEngine.render(new ModelAndView(model, "professor_subject_students.mustache"));
    }

    // -----------------------------------------------------------
    // POST /professor/subject/:subjectId/grade
    // Carga o actualiza la nota de un alumno.
    // -----------------------------------------------------------
    public String saveGrade(Request req, Response res) {

        if (!isProfessor(req)) { res.redirect("/login"); return ""; }

        Long professorId = req.session().attribute("userId");
        Long subjectId   = Long.parseLong(req.params(":subjectId"));
        String base      = "/professor/subject/" + subjectId + "/students";

        try {
            Long studentId   = Long.parseLong(req.queryParams("studentId"));
            Double gradeVal  = Double.parseDouble(req.queryParams("grade"));
            String desc      = req.queryParams("description");

            gradeService.saveGrade(professorId, studentId, subjectId, gradeVal, desc);
            res.redirect(base + "?message=" + encode("Nota guardada correctamente."));

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
    // GET /student/academic-history
    // Historial académico del estudiante logueado.
    // -----------------------------------------------------------
    public String showAcademicHistory(Request req, Response res) {

        Long userId      = req.session().attribute("userId");
        Boolean loggedIn = req.session().attribute("loggedIn");
        String role      = req.session().attribute("role");

        if (userId == null || loggedIn == null || !loggedIn || !"STUDENT".equals(role)) {
            res.redirect("/login?error=" + encode("Acceso no autorizado."));
            return "";
        }

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        String username = req.session().attribute("currentUserUsername");
        model.put("username", username);

        try {
            List<Grade> grades = gradeService.getAcademicHistory(userId);
            List<Map<String, Object>> rows = new ArrayList<>();

            for (int i = 0; i < grades.size(); i++) {
                Grade g = grades.get(i);

                Subject subject = Subject.findById(g.getSubjectId());
                Person professor = Person.findById(g.getProfessorId());

                Map<String, Object> row = new HashMap<>();
                row.put("subjectName",     subject   != null ? subject.getName()              : "—");
                row.put("subjectCode",     subject   != null ? subject.getCode()              : "—");
                row.put("professorName",   professor != null ? professor.getString("name")    : "—");
                row.put("professorSurname",professor != null ? professor.getString("surname")  : "—");
                row.put("grade",           g.getGrade());
                row.put("description",     g.getDescription() != null ? g.getDescription()   : "—");
                row.put("date",            g.getDate());
                row.put("approved",        g.getGrade() >= 6.0);
                row.put("first",           i == 0);
                row.put("last",            i == grades.size() - 1);
                rows.add(row);
            }

            model.put("grades", rows);

        } catch (Exception e) {
            e.printStackTrace();
            model.put("errorMessage", "Error al cargar el historial. Intente de nuevo.");
        }

        return templateEngine.render(new ModelAndView(model, "academic_history.mustache"));
    }

    // -----------------------------------------------------------
    // Helper
    // -----------------------------------------------------------
    private boolean isProfessor(Request req) {
        Boolean loggedIn = req.session().attribute("loggedIn");
        String role      = req.session().attribute("role");
        return loggedIn != null && loggedIn && "PROFESSOR".equals(role);
    }
}
