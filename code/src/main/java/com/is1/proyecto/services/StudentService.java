package com.is1.proyecto.services;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.services.dto.StudentCreateDTO;

public class StudentService {

    private static final String EMAIL_REGEX =
        "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*" +
        "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final AuthService authService;

    public StudentService() {
        this.authService = new AuthService();
    }

    // -----------------------------------------------------------
    // Método público principal: recibe un DTO, devuelve el Student creado.
    // El Controller solo llama a esto. No sabe nada de lo que pasa adentro.
    // -----------------------------------------------------------
    public Student createStudent(StudentCreateDTO dto) {

        // PASO 1: Validar campos obligatorios y formatos
        validateFields(dto);

        // PASO 2: AuthService crea: Person, PersonRole(STUDENT), Student
        Person person = authService.createPerson(dto);

        // PASO 3: Recuperamos el Student creado por AuthService
        Student.update("birthplace = ?, town_of_residence = ?, contact_relative = ?, contact_cellphone = ?",
                    "person_id = ?", dto.birthplace, dto.town_of_residence, dto.contact_relative,
                    dto.contact_cellphone, person.getLongId());

        return Student.findFirst("person_id = ?", person.getLongId());
    }

    // -----------------------------------------------------------
    // Validaciones de formato y presencia
    // Lanza ServiceException(400) si algo está mal.
    // -----------------------------------------------------------
    private void validateFields(StudentCreateDTO dto) {
        if (isBlank(dto.name) || isBlank(dto.surname) || isBlank(dto.email) || isBlank(dto.dni)
            || isBlank(dto.username) || isBlank(dto.password)) {
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