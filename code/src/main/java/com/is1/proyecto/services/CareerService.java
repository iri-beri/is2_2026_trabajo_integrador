package com.is1.proyecto.services;

import com.is1.proyecto.models.Career;
import com.is1.proyecto.services.dto.CareerCreateDTO;

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

    // -----------------------------------------------------------
    // Verifica que el código de la carrera no esté en uso.
    // -----------------------------------------------------------
    private void checkCodeAvailable(Integer code) {

        if (Career.findFirst("code = ?", code) != null) {
            throw new ServiceException("El código de carrera ya está en uso.", 409);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}