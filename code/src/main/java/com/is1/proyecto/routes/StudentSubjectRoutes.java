package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.StudentSubjectController;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.services.RegistrationSubjectService;

import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

public class StudentSubjectRoutes {

    public void register() {

        RegistrationSubjectService service =
            new RegistrationSubjectService();

        StudentSubjectController controller =
            new StudentSubjectController(
                service,
                new MustacheTemplateEngine()
            );

        Spark.before("/student/subjects*", (req,res) -> {

            Boolean loggedIn =
                req.session().attribute("loggedIn");

            if (loggedIn == null || !loggedIn) {

                res.redirect("/login");
                Spark.halt();
            }

            Long userId =
                req.session().attribute("userId");

            Person person =
                Person.findById(userId);

            if (person == null || !person.isStudent()) {

                res.redirect("/dashboard");
                Spark.halt();
            }
        });

        Spark.get(
            "/student/subjects",
            controller::showAvailableSubjects);

        Spark.post(
            "/student/subjects/enroll",
            controller::enroll);

        Spark.get(
            "/student/subjects/confirmation/:id",
            controller::showConfirmation);
    }
}
