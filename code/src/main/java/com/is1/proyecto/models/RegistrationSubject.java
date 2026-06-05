package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("registration_subjects")
public class RegistrationSubject extends Model {

    public Long getStudentId() {
        return getLong("student_id");
    }

    public void setStudentId(Long studentId) {
        set("student_id", studentId);
    }

    public Long getSubjectId() {
        return getLong("subject_id");
    }

    public void setSubjectId(Long subjectId) {
        set("subject_id", subjectId);
    }

    public String getDate() {
        return getString("date");
    }

    public void setDate(String date) {
        set("date", date);
    }
}