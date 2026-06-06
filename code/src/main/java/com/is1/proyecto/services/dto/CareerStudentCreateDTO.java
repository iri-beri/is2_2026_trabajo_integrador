package com.is1.proyecto.services.dto;

public class CareerStudentCreateDTO {
    public String studentDni; 
    public Integer careerCode;
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(studentDni+ careerCode);
      
        return sb.toString();
    }
}
