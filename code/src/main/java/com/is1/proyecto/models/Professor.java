package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;

@Table("professors")
@BelongsTo(parent = Person.class, foreignKeyName = "person_id")
public class Professor extends Model {

    // ------------------------------------------------------
    // Relationship
    // ------------------------------------------------------
    
    public Person getPerson() {
        return this.parent(Person.class);
    }

    public void setPersonId(Long personId) {
        set("person_id", personId); 
    }

    public Long getPersonId() {
        return getLong("person_id");
    }

    // ------------------------------------------------------
    // Academic Information
    // ------------------------------------------------------

    public String getDegree() {
        return getString("degree");
    }

    public void setDegree(String degree) {
        set("degree", degree);
    }

    public String getGraduateUniv() {
        return getString("graduate_univ");
    }

    public void setGraduateUniv(String graduateUniv) {
        set("graduate_univ", graduateUniv);
    }

    public String getPosition() {
        return getString("position");
    }

    public void setPosition(String position) {
        set("position", position);
    }
    

}