package com.is1.proyecto.validators;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import com.is1.proyecto.services.ServiceException;
import com.is1.proyecto.services.dto.PersonCreateDTO;

public class UserValidator {

    public static void validate(PersonCreateDTO dto) {

        // ---------------- DNI ----------------
        if (dto.dni == null || !dto.dni.matches("\\d{7,8}")) {
            throw new ServiceException("DNI inválido", 400);
        }

        long dniNumber = Long.parseLong(dto.dni);

        if (dniNumber < 1_000_000 || dniNumber > 100_000_000) {
            throw new ServiceException("DNI fuera de rango", 400);
        }

        // ---------------- EMAIL ----------------
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (dto.email == null || !Pattern.matches(emailRegex, dto.email)) {
            throw new ServiceException("Email inválido", 400);
        }

        // ---------------- CELULAR ----------------
        if (dto.cellphone != null && !dto.cellphone.isEmpty()) {

            if (!dto.cellphone.matches("^\\d{8,16}$")) {
                throw new ServiceException("Celular inválido", 400);
            }
        }

        // ---------------- FECHA ----------------
        try {
            LocalDate birthdate = LocalDate.parse(dto.birthdate);

            if (birthdate.getYear() < 1900) {
                throw new ServiceException("Fecha inválida (menor a 1900)", 400);
            }

            if (birthdate.isAfter(LocalDate.now())) {
                throw new ServiceException("Fecha inválida (futura)", 400);
            }

        } catch (DateTimeParseException e) {
            throw new ServiceException("Formato de fecha inválido", 400);
        }

        // ---------------- PASSWORD ----------------
        if (dto.password == null || dto.password.length() < 6) {
            throw new ServiceException("La contraseña debe tener más de 5 caracteres", 400);
        }
    }
}
