package com.is1.proyecto.services;

import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.dto.SubjectCreateDTO;

public class SubjectService {

    // -----------------------------------------------------------
    // Crea una nueva materia.
    // Devuelve el Subject creado.
    // -----------------------------------------------------------
    public Subject createSubject(SubjectCreateDTO dto) {

        validateFields(dto);
        checkCodeAvailable(dto.code);

        Subject subject = new Subject();

        subject.setCode(dto.code);
        subject.setName(dto.name);
        subject.setCourseSyllabus(dto.courseSyllabus);
        subject.setHours(dto.hours);

        subject.saveIt();

        return subject;
    }

    // -----------------------------------------------------------
    // VALIDACIONES
    // -----------------------------------------------------------
    private void validateFields(SubjectCreateDTO dto) {

        if (dto.code == null) {
            throw new ServiceException("El código de la materia es requerido.", 400);
        }

        if (isBlank(dto.name)) {
            throw new ServiceException("El nombre de la materia es requerido.", 400);
        }

        if (dto.hours == null || dto.hours <= 0) {
            throw new ServiceException("La cantidad de horas debe ser mayor a cero.", 400);
        }
    }

    // -----------------------------------------------------------
    // Verifica que el código de la materia no esté en uso.
    // -----------------------------------------------------------
    private void checkCodeAvailable(Integer code) {

        if (Subject.findFirst("code = ?", code) != null) {
            throw new ServiceException("El código de materia ya está en uso.", 409);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
