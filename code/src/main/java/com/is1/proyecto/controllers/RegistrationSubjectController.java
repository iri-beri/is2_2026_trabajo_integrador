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

import java.util.HashMap;
import java.util.Map;

public class RegistrationSubjectController extends BaseController {

    private final RegistrationSubjectService registrationService;

    public RegistrationSubjectController(
            RegistrationSubjectService registrationService,
            MustacheTemplateEngine templateEngine) {

        super(templateEngine);
        this.registrationService = registrationService;
    }

    // -----------------------------------------------------------
    // GET /registration/create
    // Muestra el formulario: dos campos un text para el dni y uno numerico para subject_id.
    // -----------------------------------------------------------
    public String showForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "registration_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /registration/new
    // Procesa la inscripción y redirige a la vista de confirmación.
    // -----------------------------------------------------------
    public String create(Request req, Response res) {

        RegistrationSubjectCreateDTO dto = buildDTO(req);

        try {

            RegistrationSubject registration = registrationService.create(dto);
            res.redirect("/registration/confirmation/" + registration.getLongId());

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/registration/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            res.redirect("/registration/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // GET /registration/confirmation/:id
    // Muestra los datos completos del Student, Subject y la fecha.
    // -----------------------------------------------------------
    public String showConfirmation(Request req, Response res) {
        System.out.println(
    "Buscando inscripción con ID: "
    + req.params(":id")
);
        String idParam = req.params(":id");

        try {

            Long registrationId = Long.parseLong(idParam);
            System.out.println("ID recibido: " + registrationId);

RegistrationSubject registration =
    RegistrationSubject.findById(registrationId);

System.out.println("Registration encontrada: " + registration);
            if (registration == null) {
                res.redirect("/registration/create?error=" + encode("Inscripción no encontrada."));
                return "";
            }

            Student student = registration.getStudent();
            Subject subject = registration.getSubject();
            Person  person  = student.getPerson();

            Map<String, Object> model = new HashMap<>();

            // Datos de la inscripción
            model.put("date",           registration.getDate());

            // Datos del alumno
            model.put("studentId",      student.getPersonId());
            model.put("studentName",    person.getName());
            model.put("studentSurname", person.getSurname());
            model.put("studentDni",     person.getDni());
            model.put("studentEmail",   person.getEmail());

            // Datos de la materia
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

        dto.dni = req.queryParams("dni");

        String subjectParam = req.queryParams("subject_id");

        try {
            dto.subjectId = subjectParam != null? Long.parseLong(subjectParam): null;
        } catch (NumberFormatException e) {
            throw new ServiceException(
                "El ID de la materia debe ser un número entero.", 400);
        }

        return dto;
    }
}