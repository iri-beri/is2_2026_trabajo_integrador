package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Student;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected final MustacheTemplateEngine templateEngine;

    public BaseController(MustacheTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    protected void addFlashMessages(Request req, Map<String, Object> model) {

        String success = req.queryParams("message");
        String error = req.queryParams("error");

        if (success != null && !success.isEmpty()) {
            model.put("successMessage", success);
        }

        if (error != null && !error.isEmpty()) {
            model.put("errorMessage", error);
        }
    }

    protected String encode(String value) {

        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    protected void loginUser(Request req, Person person) {

        req.session(true).attribute("currentUserUsername", person.getUsername());
        req.session().attribute("userId", person.getLongId());
        req.session().attribute("loggedIn", true);
        
        if (person.isAdmin()) {
        	req.session().attribute("role", "ADMIN");
    	} else if (person.isProfessor()) {
        	req.session().attribute("role", "PROFESSOR");
    	} else if (person.isStudent()) {
        	req.session().attribute("role", "STUDENT");
    	}
    }

    // ----------------------------------------------------------------
    // Vista de éxito reutilizable
    // ----------------------------------------------------------------
    public String showUserCreated(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        model.put("name", req.queryParams("name"));
        model.put("message", req.queryParams("message"));
        boolean loggedIn = "true".equals(req.queryParams("loggedIn"));
        model.put("loggedIn", loggedIn);

        return templateEngine.render(new ModelAndView(model, "user_created.mustache"));
    }

    // ----------------------------------------------------------------
    // Redirect reutilizable
    // ----------------------------------------------------------------
    protected void redirectToSuccess(Response res, String name, String message, boolean loggedIn) {

        res.redirect("/dashboard?message=" + encode(message));
    }

    protected void loadPersonData(Person person, Map<String, Object> model) {
        // Datos de Person
        model.put("name", person.getName());
        model.put("surname", person.getSurname());
        model.put("dni", person.getDni());
        model.put("username", person.getUsername());
        model.put("email", person.getEmail());
        model.put("cellphone", person.getCellphone());
        model.put("birthdate", person.getBirthdate());
        // Roles
        model.put("isAdmin", person.isAdmin());
        model.put("isProfessor", person.isProfessor());
        model.put("isStudent", person.isStudent());
        // Datos de Professor
        if (person.isProfessor()) {
            Professor professor = person.getProfessor();
            model.put("professor", true);
            model.put("degree", professor.getDegree());
            model.put("graduate_univ", professor.getGraduateUniv());
            model.put("position", professor.getPosition());
        }
        // Datos de Student
        if (person.isStudent()) {
            Student student = person.getStudent();
            model.put("student", true);
            model.put("birthplace", student.getBirthplace());
            model.put("town_of_residence", student.getTownOfResidence());
            model.put("contact_relative", student.getContactRelative());
            model.put("contact_cellphone", student.getContactCellphone());
        }
    }
}
