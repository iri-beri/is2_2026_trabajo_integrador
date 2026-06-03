package com.is1.proyecto.services.dto;

public class ProfileUpdateDTO {

    // ----------------------------
    // Identificación
    // ----------------------------
    public Long userId;

    // ----------------------------
    // Person
    // ----------------------------
    public String name;
    public String surname;
    public String email;
    public String cellphone;
    public String birthdate;

    // ----------------------------
    // Professor
    // ----------------------------
    public String degree;
    public String graduateUniv;
    public String position;

    // ----------------------------
    // Student
    // ----------------------------
    public String birthplace;
    public String townOfResidence;
    public String contactRelative;
    public String contactCellphone;
}