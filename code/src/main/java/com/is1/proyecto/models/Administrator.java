package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;

@Table("administrators")
@BelongsTo(parent = Person.class, foreignKeyName = "person_id")
public class Administrator extends Model {

    public Person getPerson() {
        return this.parent(Person.class);
    }

    public void setPersonId(Long personId) {
        set("person_id", personId);
    }
    
    public Long getPersonId() {
        return getLong("person_id");
    }
}