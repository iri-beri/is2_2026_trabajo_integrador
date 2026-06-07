package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.RegistrationSubject;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.RegistrationSubjectService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.RegistrationSubjectCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationSubjectController extends BaseController {

    private final RegistrationSubjectService registrationService;

    public RegistrationSubjectController(
            RegistrationSubjectService registrationService,
            MustacheTemplateEngine templateEngine) {

        super(templateEngine);
        this.registrationService = registrationService;
    }
    // GET /registration
    public String showStudents(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        try {
            List<Student> students = Student.findAll();
            List<Map<String, Object>> studentMaps = new ArrayList<>();

            for (Student s : students) {
                Person p = s.getPerson();
                Map<String, Object> m = new HashMap<>();
                m.put("dni",      p.getDni());
                m.put("name",     p.getName());
                m.put("surname",  p.getSurname());
                studentMaps.add(m);
            }

            model.put("students",    studentMaps);
            model.put("hasStudents", !studentMaps.isEmpty());

        } catch (Exception e) {
            model.put("hasStudents", false);
        }

        return templateEngine.render(new ModelAndView(model, "registration_students.mustache"));
    }
    // GET /registration/create
    public String showForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        String dni = req.queryParams("student_dni");

        if (dni == null || dni.isBlank()) {
            res.redirect("/students?error=" + encode("Ingresá el DNI del alumno."));
            return "";
        }

        try {
            List<Subject> available = registrationService.getAvailableSubjectsForStudent(dni);

            model.put("studentDni",         dni);
            model.put("availableSubjects",  buildSubjectMaps(available));
            model.put("hasAvailable",       !available.isEmpty());

        } catch (ServiceException e) {
            res.redirect("/students?error=" + encode(e.getMessage()));
            return "";
        }

        return templateEngine.render(new ModelAndView(model, "registration_create.mustache"));
    }

    // POST /registration/new
    public String create(Request req, Response res) {

        String dni = req.queryParams("student_dni");
        Long subjectId;

        try {
            subjectId = Long.parseLong(req.queryParams("subject_id"));
        } catch (NumberFormatException e) {
            res.redirect("/registration/create?student_dni=" + encode(dni) 
                + "&error=" + encode("Materia inválida."));
            return "";
        }

        RegistrationSubjectCreateDTO dto = new RegistrationSubjectCreateDTO();
        dto.studentDni = dni;
        dto.subjectId  = subjectId;

        try {
            RegistrationSubject saved = registrationService.create(dto);
            res.redirect("/registration/confirmation/" + saved.getLongId()); 
        } catch (ServiceException e) {
            res.status(e.getStatusCode());
            res.redirect("/registration/create?student_dni=" + encode(dni)
                + "&error=" + encode(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            res.redirect("/registration/create?student_dni=" + encode(dni)
                + "&error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // Helper
    private List<Map<String, Object>> buildSubjectMaps(List<Subject> subjects) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Subject s : subjects) {
            Map<String, Object> m = new HashMap<>();
            m.put("id",    s.getLongId());
            m.put("name",  s.getName());
            m.put("code",  s.getCode());
            m.put("hours", s.getHours());
            list.add(m);
        }
        return list;
    }
    // -----------------------------------------------------------
    // GET /registration/confirmation/:id
    //
    // parent() de ActiveJDBC solo funciona con eager loading (include()).
    // Con findById() el cache de padres está vacío y devuelve null.
    // Navegamos con búsquedas explícitas por FK para evitar el NPE.
    // -----------------------------------------------------------
    public String showConfirmation(Request req, Response res) {
        System.out.println("student_dni: " + req.queryParams("student_dni"));
        System.out.println("subject_id: "  + req.queryParams("subject_id"));
        try {

            Long registrationId = Long.parseLong(req.params(":id"));
            RegistrationSubject registration = RegistrationSubject.findById(registrationId);

            if (registration == null) {
                res.redirect("/registration/create?error=" + encode("Inscripción no encontrada."));
                return "";
            }

            // Navegación explícita por FK — no usamos parent()
            Student student = Student.findFirst("person_id = ?", registration.getStudentId());
            Subject subject = Subject.findById(registration.getSubjectId());
            Person  person  = Person.findById(student.getPersonId());

            if (student == null || subject == null || person == null) {
                res.redirect("/registration/create?error=" + encode("Datos de inscripción incompletos."));
                return "";
            }

            Map<String, Object> model = new HashMap<>();

            model.put("date",           registration.getDate());

            model.put("studentDni",     person.getDni());
            model.put("studentName",    person.getName());
            model.put("studentSurname", person.getSurname());
            model.put("studentEmail",   person.getEmail());

            model.put("subjectId",      subject.getLongId());
            model.put("subjectCode",    subject.getCode());
            model.put("subjectName",    subject.getName());
            model.put("subjectHours",   subject.getHours());

            return templateEngine.render(
                new ModelAndView(model, "registration_confirmation.mustache"));

        } catch (NumberFormatException e) {

            res.redirect("/registration/create?error=" + encode("ID de inscripción inválido."));
            return "";
        }
    }

    // -----------------------------------------------------------
    // DTO Builder
    // -----------------------------------------------------------
    private RegistrationSubjectCreateDTO buildDTO(Request req) {

        RegistrationSubjectCreateDTO dto = new RegistrationSubjectCreateDTO();

        String subjectParam = req.queryParams("subject_id");

        dto.studentDni = req.queryParams("student_dni");

        try {
            dto.subjectId = subjectParam != null ? Long.parseLong(subjectParam) : null;
        } catch (NumberFormatException e) {
            throw new ServiceException("El ID de la materia debe ser un número entero.", 400);
        }

        return dto;
    }
}