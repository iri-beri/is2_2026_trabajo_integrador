package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@Table("registration_subjects")
@BelongsToParents({
    @BelongsTo(parent = Student.class, foreignKeyName = "student_id"),
    @BelongsTo(parent = Subject.class, foreignKeyName = "subject_id")
})
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

    // -----------------------------------------------------------
    // Navegación a los padres
    // -----------------------------------------------------------
    public Student getStudent() {
        return parent(Student.class);
    }

    public Subject getSubject() {
        return parent(Subject.class);
    }
}