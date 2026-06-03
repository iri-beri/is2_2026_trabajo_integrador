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
    // El Controller solo llama a esto.
    //
    // Responsabilidades:
    //   AuthService     → Person + PersonRole(STUDENT)
    //   StudentService  → fila students con todos sus campos
    // -----------------------------------------------------------
    public Student createStudent(StudentCreateDTO dto) {

        // PASO 1: Validar campos obligatorios y formatos
        validateFields(dto);

        // PASO 2: AuthService crea Person + PersonRole.
        //         NO crea la fila en students (ver AuthService.createPerson).
        Person person = authService.createPerson(dto);

        // PASO 3: Creamos el Student completo en un solo saveIt().
        Student student = new Student();
        student.setPersonId(person.getLongId());
        student.setBirthplace(dto.birthplace);
        student.setTownOfResidence(dto.town_of_residence);
        student.setContactRelative(dto.contact_relative);
        student.setContactCellphone(dto.contact_cellphone);
        student.saveIt();

        return student;
    }

    // -----------------------------------------------------------
    // Validaciones de formato y presencia.
    // Lanza ServiceException(400) si algo está mal.
    // -----------------------------------------------------------
    private void validateFields(StudentCreateDTO dto) {

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