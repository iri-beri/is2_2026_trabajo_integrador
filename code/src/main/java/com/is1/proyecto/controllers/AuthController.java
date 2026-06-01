package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Role;
import com.is1.proyecto.services.AuthService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.PersonLoginDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService, MustacheTemplateEngine templateEngine) {
        super(templateEngine);
        this.authService = authService;
    }

    // -----------------------------------------------------------
    // GET /login
    // -----------------------------------------------------------
    public String showLoginForm(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        addFlashMessages(req, model);

        return templateEngine.render(new ModelAndView(model, "login.mustache"));
    }

    // -----------------------------------------------------------
    // POST /login
    // Autentica la persona y redirige según cantidad de roles:
    // - 1 rol  → dashboard correspondiente directamente
    // - N roles → selector de rol
    // -----------------------------------------------------------
    public String login(Request req, Response res) {

        PersonLoginDTO dto = new PersonLoginDTO();
        dto.username = req.queryParams("username");
        dto.password = req.queryParams("password");

        try {

            Person person = authService.login(dto);
            List<Role> roles = authService.getRoles(person.getLongId());

            // Guardar persona en sesión sin rol todavía
            loginUser(req, person);

            if (roles.size() == 1) {
                // Un solo rol: guardar y redirigir directo
                Role role = roles.get(0);
                req.session().attribute("userRole", role.name());
                res.redirect(dashboardFor(role));
            } else {
                // Múltiples roles: ir al selector
                req.session().attribute("pendingRoles", roles);
                res.redirect("/login/role");
            }

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", e.getMessage());
            return templateEngine.render(new ModelAndView(model, "login.mustache"));

        } catch (Exception e) {

            res.status(500);
            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", "Error interno. Intente de nuevo.");
            return templateEngine.render(new ModelAndView(model, "login.mustache"));
        }

        return "";
    }

    // -----------------------------------------------------------
    // GET /login/role
    // Muestra el selector de rol (solo si hay roles pendientes).
    // -----------------------------------------------------------
    public String showRoleSelector(Request req, Response res) {

        List<Role> roles = req.session().attribute("pendingRoles");

        if (roles == null || roles.isEmpty()) {
            res.redirect("/login");
            return "";
        }

        Map<String, Object> model = new HashMap<>();
        model.put("roles", roles.stream()
                .map(r -> Map.of("name", r.name(), "label", labelFor(r)))
                .collect(java.util.stream.Collectors.toList()));

        return templateEngine.render(new ModelAndView(model, "role_selector.mustache"));
    }

    // -----------------------------------------------------------
    // POST /login/role
    // Recibe el rol elegido, lo guarda en sesión y redirige.
    // -----------------------------------------------------------
    public String selectRole(Request req, Response res) {

        List<Role> pendingRoles = req.session().attribute("pendingRoles");

        if (pendingRoles == null || pendingRoles.isEmpty()) {
            res.redirect("/login");
            return "";
        }

        String roleParam = req.queryParams("role");

        try {

            Role selected = Role.valueOf(roleParam);

            if (!pendingRoles.contains(selected)) {
                throw new ServiceException("Rol no válido para este usuario.", 403);
            }

            req.session().attribute("userRole", selected.name());
            req.session().removeAttribute("pendingRoles");
            res.redirect(dashboardFor(selected));

        } catch (IllegalArgumentException e) {

            res.redirect("/login/role?error=" + encode("Rol inválido."));

        } catch (ServiceException e) {

            res.status(e.getStatusCode());
            res.redirect("/login/role?error=" + encode(e.getMessage()));
        }

        return "";
    }

    // -----------------------------------------------------------
    // POST /logout
    // -----------------------------------------------------------
    public String logout(Request req, Response res) {

        req.session().invalidate();
        res.redirect("/login");

        return "";
    }

    // -----------------------------------------------------------
    // HELPERS
    // -----------------------------------------------------------
    private String dashboardFor(Role role) {
        return switch (role) {
            case ADMIN     -> "/dashboard/admin";
            case PROFESSOR -> "/dashboard/professor";
            case STUDENT   -> "/dashboard/student";
        };
    }

    private String labelFor(Role role) {
        return switch (role) {
            case ADMIN     -> "Administrador";
            case PROFESSOR -> "Profesor";
            case STUDENT   -> "Estudiante";
        };
    }
}