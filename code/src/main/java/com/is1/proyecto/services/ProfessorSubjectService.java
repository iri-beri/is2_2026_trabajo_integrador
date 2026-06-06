package com.is1.proyecto.services;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Subject;
import org.javalite.activejdbc.Base;

import java.util.List;

public class ProfessorSubjectService {

    // -----------------------------------------------------------
    // Devuelve el Professor por su person_id.
    // Lanza ServiceException(404) si no existe.
    // -----------------------------------------------------------
    public Professor findProfessorByPersonId(Long personId) {

        Professor professor = Professor.findFirst("person_id = ?", personId);

        if (professor == null) {
            throw new ServiceException("Profesor no encontrado.", 404);
        }

        return professor;
    }

    // -----------------------------------------------------------
    // Devuelve la Person asociada al professor.
    // -----------------------------------------------------------
    public Person findPersonByProfessor(Professor professor) {

        Person person = Person.findById(professor.getPersonId());

        if (person == null) {
            throw new ServiceException("Persona asociada al profesor no encontrada.", 404);
        }

        return person;
    }

    // -----------------------------------------------------------
    // Lista de materias ya asignadas al profesor.
    // -----------------------------------------------------------
    public List<Subject> getAssignedSubjects(Long professorPersonId) {

        return Subject.find(
            "id IN (SELECT subject_id FROM professor_subjects WHERE professor_person_id = ?)",
            professorPersonId
        );
    }

    // -----------------------------------------------------------
    // Lista de materias NO asignadas al profesor (disponibles).
    // -----------------------------------------------------------
    public List<Subject> getAvailableSubjects(Long professorPersonId) {

        return Subject.find(
            "id NOT IN (SELECT subject_id FROM professor_subjects WHERE professor_person_id = ?)",
            professorPersonId
        );
    }

    // -----------------------------------------------------------
    // Agrega una materia al profesor.
    // Lanza ServiceException si ya está asignada.
    // -----------------------------------------------------------
    public void addSubject(Long professorPersonId, Long subjectId) {

        validateSubjectExists(subjectId);

        Long count = Base.count(
            "professor_subjects",
            "professor_person_id = ? AND subject_id = ?",
            professorPersonId.intValue(),
            subjectId.intValue()
        );

        if (count > 0) {
            throw new ServiceException("La materia ya está asignada a este profesor.", 409);
        }

        Base.exec(
            "INSERT INTO professor_subjects (professor_person_id, subject_id) VALUES (?, ?)",
            professorPersonId,
            subjectId
        );
    }

    // -----------------------------------------------------------
    // Quita una materia del profesor.
    // -----------------------------------------------------------
    public void removeSubject(Long professorPersonId, Long subjectId) {

        validateSubjectExists(subjectId);

        Base.exec(
            "DELETE FROM professor_subjects WHERE professor_person_id = ? AND subject_id = ?",
            professorPersonId,
            subjectId
        );
    }

    // -----------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------
    private void validateSubjectExists(Long subjectId) {

        if (Subject.findById(subjectId) == null) {
            throw new ServiceException("La materia especificada no existe.", 404);
        }
    }
}
