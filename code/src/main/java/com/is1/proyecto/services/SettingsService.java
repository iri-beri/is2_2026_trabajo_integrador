package com.is1.proyecto.services;

import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.services.dto.ProfileUpdateDTO;

public class SettingsService {

    public void updateProfile(ProfileUpdateDTO dto) {

        Person person = Person.findById(dto.userId);

        if (person == null) {
            throw new ServiceException("Usuario inexistente", 404);
        }

        // -------------------------------------
        // Actualizar Person
        // -------------------------------------
        person.setName(dto.name);
        person.setSurname(dto.surname);
        person.setEmail(dto.email);
        person.setCellphone(dto.cellphone);
        person.setBirthdate(dto.birthdate);

        Person existing = Person.findFirst("email = ? AND id <> ?", dto.email, dto.userId);

        if (existing != null) {
            throw new ServiceException("El email ya está siendo utilizado por otro usuario", 409);
        }

        person.saveIt();
        // -------------------------------------
        // Actualizar Person
        // -------------------------------------
        if (person.isProfessor()) {
            Professor professor = person.getProfessor();
            if (professor != null) {
                professor.setDegree(dto.degree);
                professor.setGraduateUniv(dto.graduateUniv);
                professor.setPosition(dto.position);

                professor.saveIt();
            }
        }
        // -------------------------------------
        // Actualizar Student
        // -------------------------------------
        if (person.isStudent()) {
            Student student = person.getStudent();
            if (student != null) {
                student.setBirthplace(dto.birthplace);
                student.setTownOfResidence(dto.townOfResidence);
                student.setContactRelative(dto.contactRelative);
                student.setContactCellphone(dto.contactCellphone);

                student.saveIt();
            }
        }
    }

    // Cambiar de password
    public void changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword) {
        Person person = Person.findById(userId);

        if (person == null) {
            throw new ServiceException("Usuario inexistente", 404);
        }

        if (!BCrypt.checkpw(currentPassword, person.getPassword())) {
            throw new ServiceException("La contraseña actual es incorrecta", 400);
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new ServiceException("Las contraseñas no coinciden", 400);
        }

        if (newPassword.length() < 6) {

            throw new ServiceException("La contraseña debe tener al menos 6 caracteres", 400);
        }

        person.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        person.saveIt();
    }
}