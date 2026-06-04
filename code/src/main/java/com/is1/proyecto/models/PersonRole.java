package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("person_roles")
public class PersonRole extends Model {

    public void setPersonId(Object personId) {
        set("person_id", personId);
    }

    public Object getPersonId() {
        return get("person_id");
    }

    public void setRole(Role role) {
        set("role", role.name());
    }

    public Role getRole() {
        return Role.valueOf(getString("role"));
    }
}