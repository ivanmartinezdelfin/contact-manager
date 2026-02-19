package com.portfolio.contacts.domain;

public class PersonContact extends Contact {
    private String notes;

    public PersonContact(String id, String name, String phone, String email, String notes) {
        super(id, name, phone, email);
        this.notes = notes == null ? "" : notes.trim();
    }

    @Override
    public String type() {
        return "PERSON";
    }

    @Override
    public String extraLabel() {
        return "Notas";
    }

    @Override
    public String extraValue() {
        return notes;
    }

    @Override
    public void setExtraValue(String value) {
        this.notes = value == null? "" : value.trim();
    }
}