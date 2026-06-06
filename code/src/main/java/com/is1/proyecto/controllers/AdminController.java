package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Role;
import com.is1.proyecto.models.Student;

import com.is1.proyecto.services.AuthService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.PersonCreateDTO;
import com.is1.proyecto.validators.UserValidator;

import org.javalite.activejdbc.LazyList;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController extends BaseController {

    private final AuthService authService;

    public AdminController(AuthService authService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.authService = authService;
    }

    // -----------------------------------------------------------
    // Helper privado: valida que haya sesión activa y rol ADMIN.
    // Devuelve la Person si todo está bien, null si redirigió.
    // -----------------------------------------------------------
    private Person requireAdmin(Request req, Response res) {

        Long userId  = req.session().attribute("userId");
        Boolean loggedIn = req.session().attribute("loggedIn");

        if (userId == null || loggedIn == null || !loggedIn) {
            res.redirect("/login?error=" + encode("Acceso no autorizado."));
            return null;
        }

        Person person = Person.findById(userId);
        if (person == null || !person.isAdmin()) {
            res.redirect("/login?error=" + encode("Acceso no autorizado."));
            return null;
        }

        return person;
    }

    // -----------------------------------------------------------
    // GET /dashboard/admin
    // -----------------------------------------------------------
    public String showDashboard(Request req, Response res) {

        Person person = requireAdmin(req, res);
        if (person == null) return "";

        Map<String, Object> model = new HashMap<>();
        model.put("username", person.getUsername());
        model.put("isAdmin", true);

        return templateEngine.render(new ModelAndView(model, "dashboard.mustache"));
    }

    // -----------------------------------------------------------
    // GET /admin/students
    // -----------------------------------------------------------
    public String showStudentList(Request req, Response res) {

        Person admin = requireAdmin(req, res);
        if (admin == null) return "";

        Map<String, Object> model = new HashMap<>();

        try {
            LazyList<Student> rawStudents = Student.findAll().include(Person.class);

            List<Map<String, Object>> studentRows = new ArrayList<>();
            int total = rawStudents.size();

            for (int i = 0; i < total; i++) {
                Student s = rawStudents.get(i);
                Person p  = s.getPerson();

                Map<String, Object> row = new HashMap<>();
                row.put("name",             p != null ? p.getString("name")      : "");
                row.put("surname",          p != null ? p.getString("surname")   : "");
                row.put("dni",              p != null ? p.getString("dni")       : "");
                row.put("email",            p != null ? p.getString("email")     : "");
                row.put("cellphone",        p != null ? p.getString("cellphone") : "");
                row.put("contactRelative",  s.getContactRelative()  != null ? s.getContactRelative()  : "");
                row.put("contactCellphone", s.getContactCellphone() != null ? s.getContactCellphone() : "");

                row.put("first", i == 0);
                row.put("last",  i == total - 1);

                studentRows.add(row);
            }

            model.put("students", studentRows);

        } catch (Exception e) {
            model.put("errorMessage", "Error al cargar la lista de estudiantes.");
        }

        return templateEngine.render(new ModelAndView(model, "student_list.mustache"));
    }

    // -----------------------------------------------------------
    // GET /admin/professors
    // -----------------------------------------------------------
    public String showProfessorList(Request req, Response res) {

        Person admin = requireAdmin(req, res);
        if (admin == null) return "";

        Map<String, Object> model = new HashMap<>();

        try {
            LazyList<Professor> rawProfessors = Professor.findAll().include(Person.class);

            List<Map<String, Object>> professorRows = new ArrayList<>();
            int total = rawProfessors.size();

            for (int i = 0; i < total; i++) {
                Professor prof = rawProfessors.get(i);
                Person p       = prof.getPerson();

                Map<String, Object> row = new HashMap<>();
                row.put("name",      p != null ? p.getString("name")      : "");
                row.put("surname",   p != null ? p.getString("surname")   : "");
                row.put("dni",       p != null ? p.getString("dni")       : "");
                row.put("email",     p != null ? p.getString("email")     : "");
                row.put("cellphone", p != null ? p.getString("cellphone") : "");
                row.put("position",  prof.getPosition() != null ? prof.getPosition() : "");
                row.put("personId",  prof.getPersonId());

                row.put("first", i == 0);
                row.put("last",  i == total - 1);

                professorRows.add(row);
            }

            model.put("professors", professorRows);

        } catch (Exception e) {
            model.put("errorMessage", "Error al cargar la lista de profesores.");
        }

        return templateEngine.render(new ModelAndView(model, "professor_list.mustache"));
    }

    // -----------------------------------------------------------
    // GET /admin/create
    // -----------------------------------------------------------
    public String showCreateAdminForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "user_form.mustache"));
    }

    // -----------------------------------------------------------
    // POST /admin/new
    // -----------------------------------------------------------
    public String createAdmin(Request req, Response res) {

        PersonCreateDTO dto = buildAdminDTO(req);

        try {

            UserValidator.validate(dto);

            Person person = authService.createPerson(dto);
            loginUser(req, person);
            req.session().attribute("userRole", Role.ADMIN.name());
            redirectToSuccess(res, person.getName(), "Administrador creado exitosamente", true);

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/admin/create?error=" + encode(e.getMessage()));

        } catch (Exception e) {

            res.status(500);
            res.redirect("/admin/create?error=" + encode("Error interno. Intente de nuevo."));
        }

        return "";
    }

    // -----------------------------------------------------------
    // BUILDER
    // -----------------------------------------------------------
    private PersonCreateDTO buildAdminDTO(Request req) {

        PersonCreateDTO dto = new PersonCreateDTO();

        dto.dni       = req.queryParams("dni");
        dto.name      = req.queryParams("name");
        dto.surname   = req.queryParams("surname");
        dto.username  = req.queryParams("username");
        dto.email     = req.queryParams("email");
        dto.cellphone = req.queryParams("cellphone");
        dto.birthdate = req.queryParams("birthdate");
        dto.password  = req.queryParams("password");
        dto.role      = Role.ADMIN;

        return dto;
    }
}
