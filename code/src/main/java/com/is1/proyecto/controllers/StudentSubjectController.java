package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.RegistrationSubject;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.RegistrationSubjectService;
import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.RegistrationSubjectCreateDTO;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

public class StudentSubjectController extends BaseController {

    private final RegistrationSubjectService registrationService;

    public StudentSubjectController(
            RegistrationSubjectService registrationService,
            MustacheTemplateEngine templateEngine) {

        super(templateEngine);
        this.registrationService = registrationService;
    }

    public String showAvailableSubjects(Request req, Response res) {

        Long userId = req.session().attribute("userId");

        Person person = Person.findById(userId);

        Map<String,Object> model = new HashMap<>();

        try {

            List<Subject> subjects =
                registrationService.getAvailableSubjectsForStudent(
                    person.getDni());

            List<Map<String,Object>> subjectMaps = new ArrayList<>();

            for (Subject s : subjects) {

                Map<String,Object> m = new HashMap<>();

                m.put("id", s.getLongId());
                m.put("code", s.getCode());
                m.put("name", s.getName());
                m.put("hours", s.getHours());

                subjectMaps.add(m);
            }

            model.put("subjects", subjectMaps);
            model.put("hasSubjects", !subjectMaps.isEmpty());

        } catch (Exception e) {

            model.put("hasSubjects", false);
            model.put("errorMessage", e.getMessage());
        }

        return templateEngine.render(
            new ModelAndView(model, "student_subjects.mustache"));
    }

    public String enroll(Request req, Response res) {

        Long userId = req.session().attribute("userId");

        Person person = Person.findById(userId);

        RegistrationSubjectCreateDTO dto =
            new RegistrationSubjectCreateDTO();

        dto.studentDni = person.getDni();

        try {

            dto.subjectId =
                Long.parseLong(req.queryParams("subject_id"));

            RegistrationSubject registration =
                registrationService.create(dto);

            res.redirect(
                "/student/subjects/confirmation/" +
                registration.getLongId());

        } catch (ServiceException e) {

            res.redirect(
                "/student/subjects?error=" +
                encode(e.getMessage()));

        } catch (Exception e) {

            res.redirect(
                "/student/subjects?error=" +
                encode("Error interno."));
        }

        return "";
    }

    public String showConfirmation(Request req, Response res) {

        Long registrationId =
            Long.parseLong(req.params(":id"));

        RegistrationSubject registration =
            RegistrationSubject.findById(registrationId);

        Subject subject =
            Subject.findById(registration.getSubjectId());

        Map<String,Object> model = new HashMap<>();

        model.put("subjectName", subject.getName());
        model.put("subjectCode", subject.getCode());
        model.put("date", registration.getDate());

        return templateEngine.render(
            new ModelAndView(
                model,
                "student_subject_confirmation.mustache"));
    }
}