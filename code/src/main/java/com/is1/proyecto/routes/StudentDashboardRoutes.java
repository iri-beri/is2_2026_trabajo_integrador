package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.StudentDashboardController;

import static spark.Spark.get;

public class StudentDashboardRoutes {

    private final StudentDashboardController controller;

    public StudentDashboardRoutes(StudentDashboardController controller) {
        this.controller = controller;
    }

    public void register() {
        get("/dashboard/student", controller::showDashboard);
    }
}
