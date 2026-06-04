package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;

@Table("students")
@BelongsTo(parent = Person.class, foreignKeyName = "person_id")
public class Student extends Model {

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
    
    // -------------------------
    // Student Information
    // -------------------------

    public String getBirthplace() {
        return getString("birthplace");
    }

    public void setBirthplace(String birthplace) {
        set("birthplace", birthplace);
    }

    public String getTownOfResidence() {
        return getString("town_of_residence");
    }

    public void setTownOfResidence(String town_of_residence) {
        set("town_of_residence", town_of_residence);
    }
    
    public String getContactRelative() {
        return getString("contact_relative");
    }

    public void setContactRelative(String contactRelative) {
        set("contact_relative", contactRelative);
    }

    public String getContactCellphone() {
        return getString("contact_cellphone");
    }

    public void setContactCellphone(String contactCellphone) {
        set("contact_cellphone", contactCellphone);
    }
}