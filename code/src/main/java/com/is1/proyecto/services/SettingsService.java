package com.is1.proyecto.services;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Person;
import com.is1.proyecto.services.dto.ProfileUpdateDTO;

public class SettingsService {

    public void updateProfile(ProfileUpdateDTO dto) {

        Person person = Person.findById(dto.userId);

        if (person == null) {
            throw new ServiceException("Usuario inexistente", 404);
        }

        Person existing = Person.findFirst("email = ? AND id <> ?", dto.email, dto.userId);
        if (existing != null) {
            throw new ServiceException("El email ya está siendo utilizado por otro usuario", 409);
        }

        // -------------------------------------
        // Actualizar Person
        // -------------------------------------
        person.setName(dto.name);
        person.setSurname(dto.surname);
        person.setEmail(dto.email);
        person.setCellphone(dto.cellphone);
        person.setBirthdate(dto.birthdate);
        person.saveIt();

        // -------------------------------------
        // Actualizar Professor (SQL directo para evitar problema de PK)
        // -------------------------------------
        if (person.isProfessor()) {
            Base.exec(
                "UPDATE professors SET degree = ?, graduate_univ = ?, position = ? WHERE person_id = ?",
                dto.degree      != null ? dto.degree      : "",
                dto.graduateUniv != null ? dto.graduateUniv : "",
                dto.position    != null ? dto.position    : "",
                dto.userId
            );
        }

        // -------------------------------------
        // Actualizar Student (SQL directo para evitar problema de PK)
        // -------------------------------------
        if (person.isStudent()) {
            Base.exec(
                "UPDATE students SET birthplace = ?, town_of_residence = ?, contact_relative = ?, contact_cellphone = ? WHERE person_id = ?",
                dto.birthplace       != null ? dto.birthplace       : "",
                dto.townOfResidence  != null ? dto.townOfResidence  : "",
                dto.contactRelative  != null ? dto.contactRelative  : "",
                dto.contactCellphone != null ? dto.contactCellphone : "",
                dto.userId
            );
        }
    }

    // -------------------------------------
    // Cambiar password
    // -------------------------------------
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
