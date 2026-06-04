package com.is1.proyecto.services;

import com.is1.proyecto.models.StudyPlan;
import com.is1.proyecto.services.dto.StudyPlanCreateDTO;

public class StudyPlanService {

    // -----------------------------------------------------------
    // Crea un nuevo plan de estudio.
    // Devuelve el StudyPlan creado.
    // -----------------------------------------------------------
    public StudyPlan createStudyPlan(StudyPlanCreateDTO dto) {

        validateFields(dto);

        StudyPlan studyPlan = new StudyPlan();

        studyPlan.setVersion(dto.version);
        studyPlan.setName(dto.name);

        studyPlan.saveIt();

        return studyPlan;
    }

    // -----------------------------------------------------------
    // VALIDACIONES
    // -----------------------------------------------------------
    private void validateFields(StudyPlanCreateDTO dto) {

        if (isBlank(dto.name)) {
            throw new ServiceException("El nombre del plan de estudio es requerido.", 400);
        }

        if (isBlank(dto.version)) {
            throw new ServiceException("La versión del plan de estudio es requerida.", 400);
        }

        if (!dto.version.matches("^\\d{4}$")) {
            throw new ServiceException("La versión debe ser un año de 4 dígitos (ej: 2024).", 400);
        }

        int year = Integer.parseInt(dto.version);
        if (year < 1900 || year > 2100) {
            throw new ServiceException("La versión debe ser un año entre 1900 y 2100.", 400);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}