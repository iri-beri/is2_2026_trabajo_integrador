package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.GradeController;
import com.is1.proyecto.services.GradeService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class GradeRoutes {

    private final GradeController controller;

    public GradeRoutes() {
        GradeService service = new GradeService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        controller = new GradeController(service, templateEngine);
    }

    public void register() {
        // Profesor: ver alumnos de una materia y cargar notas
        get ("/professor/subject/:subjectId/students", controller::showStudents);
        post("/professor/subject/:subjectId/grade",    controller::saveGrade);

        // Estudiante: historial académico
        get ("/student/academic-history",              controller::showAcademicHistory);
    }
}
