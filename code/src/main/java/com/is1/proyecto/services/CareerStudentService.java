package com.is1.proyecto.services;

import com.is1.proyecto.models.Career;
import com.is1.proyecto.models.CareerStudent;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.services.dto.CareerStudentCreateDTO;
public class CareerStudentService {

    public EnrollmentResult enroll(CareerStudentCreateDTO dto) {

        validate(dto);

        Student student = findStudentByDni(dto.studentDni);
        Career career = findCareerByCode(dto.careerCode);

        Long studentId = student.getPersonId();
        Long careerId = career.getLongId();

        ensureNotAlreadyEnrolled(studentId, careerId);

        CareerStudent.createIt(
            "career_id", careerId,
            "student_id", studentId
        );

        return new EnrollmentResult(student, career);
    }

    private void validate(CareerStudentCreateDTO dto) {

        if (dto == null) {
            throw new ServiceException(
                "Solicitud inválida.", 400);
        }

        if (isBlank(dto.studentDni)) {
            throw new ServiceException(
                "El DNI del alumno es requerido.", 400);
        }

        if (dto.careerCode == null) {
            throw new ServiceException(
                "El código de carrera es requerido.", 400);
        }
    }

    private Student findStudentByDni(String dni) {

        Person person = Person.findFirst("dni = ?", dni);

        if (person == null) {
            throw new ServiceException(
                "No existe ninguna persona con DNI " + dni + ".", 404);
        }

        Student student = person.getStudent();

        if (student == null) {
            throw new ServiceException(
                "La persona con DNI " + dni + " no está registrada como alumno.", 404);
        }

        return student;
    }

    private Career findCareerByCode(Integer code) {

        Career career = Career.findFirst("code = ?", code);

        if (career == null) {
            throw new ServiceException(
                "No existe ninguna carrera con código " + code + ".", 404);
        }

        return career;
    }

    private void ensureNotAlreadyEnrolled(Long studentId, Long careerId) {

        boolean alreadyEnrolled =
            CareerStudent.findFirst(
                "career_id = ? AND student_id = ?",
                careerId,
                studentId
            ) != null;

        if (alreadyEnrolled) {
            throw new ServiceException(
                "El alumno ya está inscripto en esta carrera.", 409);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static class EnrollmentResult {

        public final Student student;
        public final Career career;

        public EnrollmentResult(Student student, Career career) {
            this.student = student;
            this.career = career;
        }
    }
}