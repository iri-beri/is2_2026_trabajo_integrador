package com.is1.proyecto.services;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.services.dto.ProfessorCreateDTO;

public class ProfessorService {

    private static final String EMAIL_REGEX =
        "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*" +
        "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final AuthService authService;

    public ProfessorService() {
        this.authService = new AuthService();
    }

    // -----------------------------------------------------------
    // Método público principal: recibe un DTO, devuelve el Professor creado.
    // El Controller solo llama a esto.
    //
    // Responsabilidades:
    //   AuthService  → Person + PersonRole(PROFESSOR)
    //   ProfessorService → fila professors con todos sus campos
    // -----------------------------------------------------------
    public Professor createProfessor(ProfessorCreateDTO dto) {

        // PASO 1: Validar campos obligatorios y formatos
        validateFields(dto);

        // PASO 2: AuthService crea Person + PersonRole.
        //         NO crea la fila en professors (ver AuthService.createPerson).
        Person person = authService.createPerson(dto);

        // PASO 3: Creamos el Professor completo en un solo saveIt().
        //         Así evitamos el UPDATE posterior que era la fuente del error.
        Professor professor = new Professor();
        professor.setPersonId(person.getLongId());
        professor.setDegree(dto.degree);
        professor.setGraduateUniv(dto.university);
        professor.setPosition(dto.position);
        professor.saveIt();

        return professor;
    }

    // -----------------------------------------------------------
    // Validaciones de formato y presencia.
    // Lanza ServiceException(400) si algo está mal.
    // -----------------------------------------------------------
    private void validateFields(ProfessorCreateDTO dto) {

        if (isBlank(dto.name) || isBlank(dto.surname) || isBlank(dto.email)
                || isBlank(dto.dni) || isBlank(dto.username) || isBlank(dto.password)) {
            throw new ServiceException(
                "Todos los campos obligatorios son requeridos.", 400);
        }

        if (!dto.email.matches(EMAIL_REGEX)) {
            throw new ServiceException("El formato del email no es válido.", 400);
        }
    }

    // -----------------------------------------------------------
    // Helper: null o vacío
    // -----------------------------------------------------------
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}