package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Student;
import com.is1.proyecto.models.Role;

import com.is1.proyecto.services.StudentService;
import com.is1.proyecto.services.ServiceException;

import com.is1.proyecto.services.dto.StudentCreateDTO;

import com.is1.proyecto.validators.UserValidator;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class StudentController extends BaseController {

    private final StudentService studentService;

    public StudentController(StudentService studentService, MustacheTemplateEngine templateEngine) {

        super(templateEngine);
        this.studentService = studentService;
    }

    // -----------------------------------------------------------
    // GET /student/create
    // -----------------------------------------------------------
    public String showForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "student_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /student/new
    // -----------------------------------------------------------
    public String create(Request req, Response res) {

        StudentCreateDTO dto = buildDTO(req);

        try {

            UserValidator.validate(dto);
            Student student = studentService.createStudent(dto);
            redirectToSuccess(res, student.getPerson().getName(), "Estudiante creado exitosamente", false);

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/student/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            System.out.println("ERROR ESTUDIANTE:");
            e.printStackTrace();

            res.status(500);
            res.redirect("/student/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // DTO Builder
    // -----------------------------------------------------------
    private StudentCreateDTO buildDTO(Request req) {

        StudentCreateDTO dto = new StudentCreateDTO();

        dto.dni = req.queryParams("dni");
        dto.name = req.queryParams("name");
        dto.surname = req.queryParams("surname");
        dto.username = req.queryParams("username");
        dto.email = req.queryParams("email");
        dto.cellphone = req.queryParams("cellphone");
        dto.birthdate = req.queryParams("birthdate");
        dto.password = req.queryParams("password");
        dto.birthplace = req.queryParams("birthplace");
        dto.town_of_residence = req.queryParams("town_of_residence");
        dto.contact_relative = req.queryParams("contact_relative");
        dto.contact_cellphone = req.queryParams("contact_cellphone");
        dto.role = Role.STUDENT;
        
        return dto;
    }
}