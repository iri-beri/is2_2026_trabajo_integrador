package com.is1.proyecto.services;

import com.is1.proyecto.models.Administrator;
import com.is1.proyecto.models.Person;
import com.is1.proyecto.models.PersonRole;
import com.is1.proyecto.models.Professor;
import com.is1.proyecto.models.Role;
import com.is1.proyecto.models.Student;
import com.is1.proyecto.services.dto.PersonCreateDTO;
import com.is1.proyecto.services.dto.PersonLoginDTO;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.stream.Collectors;

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
    // Lanza ServiceException en cualquier caso de fallo.
    // -----------------------------------------------------------
    public Person login(PersonLoginDTO dto) {

        validateFields(dto.username, dto.password);

        Person person = Person.findFirst("username = ?", dto.username);

        if (person == null || !BCrypt.checkpw(dto.password, person.getPassword())) {
            throw new ServiceException("Usuario o contraseña incorrectos.", 401);
        }

        return person;
    }

    // -----------------------------------------------------------
    // GET ROLES
    // Devuelve la lista de roles registrados para una persona.
    // -----------------------------------------------------------
    public List<Role> getRoles(Long personId) {

        List<PersonRole> personRoles = PersonRole.where("person_id = ?", personId);

        if (personRoles == null || personRoles.isEmpty()) {
            throw new ServiceException("La persona no tiene roles asignados.", 403);
        }

        return personRoles.stream()
                .map(pr -> pr.getRole())
                .collect(Collectors.toList());
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