package com.is1.proyecto.services;

import com.is1.proyecto.models.Career;
import com.is1.proyecto.models.Subject;
import com.is1.proyecto.services.dto.CareerCreateDTO;

import java.util.List;

public class CareerService {

    // -----------------------------------------------------------
    // Crea una nueva carrera.
    // Devuelve el Career creado.
    // -----------------------------------------------------------
    public Career createCareer(CareerCreateDTO dto) {

        validateFields(dto);
        checkCodeAvailable(dto.code);

        Career career = new Career();

        career.setCode(dto.code);
        career.setName(dto.name);

        career.saveIt();

        return career;
    }

    // -----------------------------------------------------------
    // Lista todas las carreras ordenadas por nombre.
    // -----------------------------------------------------------
    public List<Career> getAllCareers() {
        return Career.findAll().orderBy("name ASC");
    }

    // -----------------------------------------------------------
    // Busca una carrera por su id.
    // -----------------------------------------------------------
    public Career findById(Integer id) {

        Career career = Career.findById(id);

        if (career == null) {
            throw new ServiceException("Carrera no encontrada.", 404);
        }

        return career;
    }

    // -----------------------------------------------------------
    // Devuelve todas las materias que pertenecen a la carrera.
    // -----------------------------------------------------------
    public List<Subject> getSubjectsForCareer(Integer careerId) {

        Career career = findById(careerId);
        return career.getAll(Subject.class).orderBy("name ASC");
    }

    // -----------------------------------------------------------
    // Devuelve todas las materias que NO pertenecen a la carrera.
    // -----------------------------------------------------------
    public List<Subject> getSubjectsNotInCareer(Integer careerId) {

        Career career = findById(careerId);

        List<Subject> allSubjects      = Subject.findAll().orderBy("name ASC");
        List<Subject> careerSubjects   = career.getAll(Subject.class);

        // IDs ya asignados
        List<Integer> assignedIds = careerSubjects.stream()
                .map(s -> s.getInteger("id"))
                .toList();

        return allSubjects.stream()
                .filter(s -> !assignedIds.contains(s.getInteger("id")))
                .toList();
    }

    // -----------------------------------------------------------
    // Agrega una materia a la carrera.
    // -----------------------------------------------------------
    public void addSubjectToCareer(Integer careerId, Integer subjectId) {

        Career career  = findById(careerId);
        Subject subject = Subject.findById(subjectId);

        if (subject == null) {
            throw new ServiceException("Materia no encontrada.", 404);
        }

        // Verificar que no esté ya asignada
        boolean alreadyAssigned = career.getAll(Subject.class)
                .stream()
                .anyMatch(s -> s.getInteger("id").equals(subjectId));

        if (alreadyAssigned) {
            throw new ServiceException("La materia ya está asignada a esta carrera.", 409);
        }

        career.add(subject);
    }

    // -----------------------------------------------------------
    // Quita una materia de la carrera.
    // -----------------------------------------------------------
    public void removeSubjectFromCareer(Integer careerId, Integer subjectId) {

        Career career  = findById(careerId);
        Subject subject = Subject.findById(subjectId);

        if (subject == null) {
            throw new ServiceException("Materia no encontrada.", 404);
        }

        career.remove(subject);
    }

    // -----------------------------------------------------------
    // VALIDACIONES
    // -----------------------------------------------------------
    private void validateFields(CareerCreateDTO dto) {

        if (dto.code == null) {
            throw new ServiceException("El código de la carrera es requerido.", 400);
        }

        if (isBlank(dto.name)) {
            throw new ServiceException("El nombre de la carrera es requerido.", 400);
        }
    }

    private void checkCodeAvailable(Integer code) {

        if (Career.findFirst("code = ?", code) != null) {
            throw new ServiceException("El código de carrera ya está en uso.", 409);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
