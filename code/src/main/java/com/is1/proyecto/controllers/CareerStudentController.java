package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Career;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.services.CareerStudentService;
import com.is1.proyecto.services.CareerStudentService.EnrollmentResult;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.CareerStudentCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class CareerStudentController extends BaseController {

    private final CareerStudentService careerStudentService;

    public CareerStudentController(
            CareerStudentService careerStudentService,
            MustacheTemplateEngine templateEngine) {

        super(templateEngine);
        this.careerStudentService = careerStudentService;
    }

    // -----------------------------------------------------------
    // GET /career-student/enroll
    // -----------------------------------------------------------
    public String showForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "career_student_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /career-student/enroll
    // -----------------------------------------------------------
    public String enroll(Request req, Response res) {

        CareerStudentCreateDTO dto = buildDTO(req);

        try {
            EnrollmentResult result = careerStudentService.enroll(dto);
            System.out.println("POST result");

            Long personId = result.student.getPersonId();

            req.session().attribute(
                "enrollmentPersonId",
                personId
            );

            req.session().attribute(
                "enrollmentCareerId",
                result.career.getLongId()
            );
            res.redirect("/career-student/confirmation");

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            e.printStackTrace();
            res.redirect("/career-student/enroll?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            res.status(500);
            e.printStackTrace();
            res.redirect("/career-student/enroll?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // GET /career-student/confirmation
    // Lee los IDs de sesión y muestra los datos completos.
    // Usa búsquedas explícitas por FK — no usa parent().
    // -----------------------------------------------------------
    public String showConfirmation(Request req, Response res) {

        Long personId =
            req.session().attribute("enrollmentPersonId");

        Long careerId =
            req.session().attribute("enrollmentCareerId");

                if (personId == null || careerId == null) {

            res.redirect(
                "/career-student/enroll?error=" +
                encode("No hay inscripción reciente.")
            );

            return "";
        }
        //Limpiar la sesion
       req.session().removeAttribute("enrollmentPersonId");
req.session().removeAttribute("enrollmentCareerId");
                Person person =
            Person.findById(personId);

        Student student =
            Student.findFirst("person_id = ?", personId);

        Career career =
            Career.findById(careerId);

        if (student == null || career == null || person == null) {
            res.redirect("/career-student/enroll?error=" + encode("Datos de inscripción no encontrados."));
            return "";
        }

        Map<String, Object> model = new HashMap<>();

        model.put("studentDni",     person.getDni());
        model.put("studentName",    person.getName());
        model.put("studentSurname", person.getSurname());
        model.put("studentEmail",   person.getEmail());

        model.put("careerCode",     career.getCode());
        model.put("careerName",     career.getName());

        return templateEngine.render(
            new ModelAndView(model, "career_student_confirmation.mustache"));
    }

    // -----------------------------------------------------------
    // DTO Builder
    // -----------------------------------------------------------
    private CareerStudentCreateDTO buildDTO(Request req) {

        CareerStudentCreateDTO dto = new CareerStudentCreateDTO();

        String codeParam = req.queryParams("career_code");

        dto.studentDni = req.queryParams("student_dni");

        try {
            dto.careerCode = codeParam != null ? Integer.parseInt(codeParam) : null;
        } catch (NumberFormatException e) {
            throw new ServiceException("El código de carrera debe ser un número entero.", 400);
        }

        return dto;
    }
}