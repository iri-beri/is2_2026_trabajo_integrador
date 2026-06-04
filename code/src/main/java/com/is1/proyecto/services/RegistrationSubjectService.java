package com.is1.proyecto.services;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.RegistrationSubject;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.dto.RegistrationSubjectCreateDTO;

import java.time.LocalDate;

public class RegistrationSubjectService {

    // -----------------------------------------------------------
    // Inscribe un Student a un Subject.
    // Devuelve el RegistrationSubject creado.
    // Lanza ServiceException ante cualquier fallo de negocio.
    // -----------------------------------------------------------
    public RegistrationSubject create(RegistrationSubjectCreateDTO dto) {

        validateIds(dto);

        Student student = findStudent(dto.dni);
        Subject subject = findSubject(dto.subjectId);

        checkNotAlreadyRegistered(student.getPersonId(),subject.getLongId());

        RegistrationSubject registration = new RegistrationSubject();

        registration.setStudentId(student.getPersonId());
        registration.setSubjectId(subject.getLongId());
        registration.setDate(LocalDate.now().toString());

        registration.saveIt();

        return registration;
    }

    // -----------------------------------------------------------
    // Validaciones
    // -----------------------------------------------------------
    private void validateIds(RegistrationSubjectCreateDTO dto) {

        if (dto.dni == null || dto.subjectId == null) {
            throw new ServiceException(
                "El ID del alumno y el ID de la materia son requeridos.", 400);
        }
    }

   private Student findStudent(String dni) {

        Person person = Person.findFirst("dni = ?", dni);

        if (person == null) {
            throw new ServiceException(
                "No existe una persona con DNI " + dni + ".", 404);
        }

        Student student = Student.findFirst(
            "person_id = ?",
            person.getLongId()
        );

        if (student == null) {
            throw new ServiceException(
                "La persona con DNI " + dni + " no está registrada como alumno.", 404);
        }

        return student;
    }
    
    private Subject findSubject(Long subjectId) {

        Subject subject = Subject.findById(subjectId);

        if (subject == null) {
            throw new ServiceException(
                "No existe una materia con ID " + subjectId + ".", 404);
        }

        return subject;
    }

    private void checkNotAlreadyRegistered(Long dni, Long subjectId) {

        boolean exists = RegistrationSubject.findFirst(
            "student_id = ? AND subject_id = ?", dni, subjectId
        ) != null;

        if (exists) {
            throw new ServiceException(
                "El alumno ya está inscripto en esa materia.", 409);
        }
    }
}
