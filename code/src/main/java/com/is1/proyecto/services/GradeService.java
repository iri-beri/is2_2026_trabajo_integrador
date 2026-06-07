package com.is1.proyecto.services;

import com.is1.proyecto.models.Grade;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.RegistrationSubject;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.models.Subject;

import java.time.LocalDate;
import java.util.List;

public class GradeService {

    // -----------------------------------------------------------
    // Devuelve los estudiantes inscriptos a una materia,
    // con su nota si ya fue cargada por este profesor.
    // -----------------------------------------------------------
    public List<RegistrationSubject> getStudentsBySubject(Long subjectId) {

        Subject subject = Subject.findById(subjectId);
        if (subject == null) {
            throw new ServiceException("Materia no encontrada.", 404);
        }

        return RegistrationSubject.where("subject_id = ?", subjectId);
    }

    // -----------------------------------------------------------
    // Carga o actualiza una nota.
    // Solo el profesor asignado a esa materia puede hacerlo.
    // -----------------------------------------------------------
    public Grade saveGrade(Long professorId, Long studentId, Long subjectId,
                           Double gradeValue, String description) {

        validateGradeValue(gradeValue);
        validateStudentEnrolled(studentId, subjectId);
        validateProfessorAssigned(professorId, subjectId);

        // Si ya existe una nota del mismo profesor para ese alumno/materia, la actualiza
        Grade existing = Grade.findFirst(
            "student_id = ? AND subject_id = ? AND professor_id = ?",
            studentId, subjectId, professorId
        );

        if (existing != null) {
            existing.setGrade(gradeValue);
            existing.setDescription(description != null ? description : "");
            existing.setDate(LocalDate.now().toString());
            existing.saveIt();
            return existing;
        }

        Grade grade = new Grade();
        grade.setStudentId(studentId);
        grade.setSubjectId(subjectId);
        grade.setProfessorId(professorId);
        grade.setGrade(gradeValue);
        grade.setDescription(description != null ? description : "");
        grade.setDate(LocalDate.now().toString());
        grade.saveIt();

        return grade;
    }

    // -----------------------------------------------------------
    // Historial académico de un estudiante: todas sus notas.
    // -----------------------------------------------------------
    public List<Grade> getAcademicHistory(Long studentId) {

        return Grade.where("student_id = ?", studentId);
    }

    // -----------------------------------------------------------
    // Validaciones privadas
    // -----------------------------------------------------------
    private void validateGradeValue(Double grade) {
        if (grade == null || grade < 0 || grade > 10) {
            throw new ServiceException("La nota debe ser un valor entre 0 y 10.", 400);
        }
    }

    private void validateStudentEnrolled(Long studentId, Long subjectId) {
        boolean enrolled = RegistrationSubject.findFirst(
            "student_id = ? AND subject_id = ?", studentId, subjectId
        ) != null;

        if (!enrolled) {
            throw new ServiceException("El alumno no está inscripto en esa materia.", 400);
        }
    }

    private void validateProfessorAssigned(Long professorId, Long subjectId) {
        long count = com.is1.proyecto.models.Grade.count(
            "professor_id = ? OR professor_id IS NOT NULL",
            professorId
        );
        // Verificar que el profesor tiene esa materia asignada
        Object result = org.javalite.activejdbc.Base.firstCell(
            "SELECT COUNT(*) FROM professor_subjects WHERE professor_person_id = ? AND subject_id = ?",
            professorId, subjectId
        );
        long assigned = result != null ? ((Number) result).longValue() : 0;
        if (assigned == 0) {
            throw new ServiceException("No tenés esa materia asignada.", 403);
        }
    }
}
