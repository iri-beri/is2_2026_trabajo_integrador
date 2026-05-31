package com.is1.proyecto.services;

import com.is1.proyecto.models.Administrator;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.PersonRole;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.services.dto.PersonCreateDTO;
import com.is1.proyecto.services.dto.PersonLoginDTO;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    // -----------------------------------------------------------
    // Registra una nueva persona.
    // Devuelve la Person creada.
    // -----------------------------------------------------------
    public Person createPerson(PersonCreateDTO dto) {

        validateFields(dto.username, dto.password);
        checkUsernameAvailable(dto.username);

        // -------------------------------------------------------
        // PERSON
        // -------------------------------------------------------
        Person person = new Person();

        person.setDni(dto.dni);
        person.setName(dto.name);
        person.setSurname(dto.surname);
        person.setUsername(dto.username);
        person.setEmail(dto.email);
        person.setCellphone(dto.cellphone);
        person.setBirthdate(dto.birthdate);

        person.setPassword(
            BCrypt.hashpw(dto.password, BCrypt.gensalt())
        );
        person.saveIt();

        Person savedPerson = Person.findFirst("username = ?", dto.username);
        if (savedPerson == null) {
            throw new ServiceException("No se pudo recuperar la persona recién creada.", 500);
        }

        Long personId = savedPerson.getLongId();

        // -------------------------------------------------------
        // ROLE
        // -------------------------------------------------------
        PersonRole personRole = new PersonRole();

        personRole.setPersonId(personId);
        personRole.setRole(dto.role);
        personRole.saveIt();

        // -------------------------------------------------------
        // SUBCLASS TABLE
        // -------------------------------------------------------
        switch (dto.role) {

            case ADMIN -> {

                Administrator administrator = new Administrator();
                administrator.setPersonId(personId);
                administrator.saveIt();
            }

            case PROFESSOR -> {

                Professor professor = new Professor();
                professor.setPersonId(personId);
                professor.saveIt();
            }

            case STUDENT -> {
                Student student = new Student();
                student.setPersonId(personId);
                student.saveIt();
            }

            default -> {
                throw new ServiceException("Rol inválido", 400);
            }
        }

        return savedPerson;
    }


    // -----------------------------------------------------------
    // LOGIN
    // Autentica un usuario.
    // Devuelve la Person si las credenciales son correctas.
    // Lanza ServiceException en cualquier caso de fallo
    // (mensaje genérico para no dar pistas de seguridad).
    // -----------------------------------------------------------
    public Person login(PersonLoginDTO dto) {
        
        validateFields(dto.username, dto.password);

        Person person = Person.findFirst("username = ?", dto.username);

        if (person == null || !BCrypt.checkpw(dto.password, person.getPassword())) {
            // Mismo mensaje para usuario inexistente y contraseña incorrecta:
            // no revelar cuál de los dos falló.
            throw new ServiceException("Usuario o contraseña incorrectos.", 401);
        }

        return person;
    }

    // -----------------------------------------------------------
    // VALIDACIONES
    // -----------------------------------------------------------
    private void validateFields(String username, String password) {
        
        if (isBlank(username) || isBlank(password)) {
            throw new ServiceException(
                "Nombre y contraseña son requeridos.", 400);
        }
    }

    // -----------------------------------------------------------
    // Verifica que el username de la Persona no esté tomado.
    // -----------------------------------------------------------
    private void checkUsernameAvailable(String username) {
        
        if (Person.findFirst("username = ?", username) != null) {
            throw new ServiceException(
                "El nombre de usuario ya está en uso.", 409);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}