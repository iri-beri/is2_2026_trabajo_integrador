package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.StudyPlanController;
import com.is1.proyecto.services.StudyPlanService;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class StudyPlanRoutes {

    private final StudyPlanController controller;

    public StudyPlanRoutes() {
        StudyPlanService service = new StudyPlanService();
        MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();
        this.controller = new StudyPlanController(service, templateEngine);
    }

    // -----------------------------------------------------------
    // Registra todas las rutas relacionadas a StudyPlan en Spark
    // -----------------------------------------------------------
    public void register() {
        get("/studyplan/create", controller::showCreateStudyPlanForm);
        post("/studyplan/new",   controller::createStudyPlan);
    }
}
