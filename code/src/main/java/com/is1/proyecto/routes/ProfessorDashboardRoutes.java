package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.ProfessorDashboardController;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.get;

public class ProfessorDashboardRoutes {

    private final ProfessorDashboardController controller;

    public ProfessorDashboardRoutes(ProfessorDashboardController controller) {
        this.controller = controller;
    }

    public void register() {
        get("/dashboard/professor", controller::showDashboard);
    }
}
