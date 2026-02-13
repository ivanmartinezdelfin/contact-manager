package com.portfolio.contacts.domain;

import com.portfolio.contacts.util.Validator;

import java.util.Objects;

public abstract class Contact {
    private final String id; // UUID como string
    private String name;
    private String phone;
    private String email;

    protected Contact(String id, String name, String phone, String email) {
        Validator.requireNonBlank(id,"Id");
        Validator.requireNonBlank(name, "Name");
        Validator.requireNonBlank(phone, "Phone");
        Validator.requireNonBlank(email, "Email");

        this.id = id.trim();
        this.name = name.trim();
        this.phone = phone.trim();
        this.email = email.trim();
    }

    public abstract String type(); //"PERSON"/ "COMPANY"
    public abstract String extraLabel(); //"NOTAS" / "Empresa"
    public abstract String extraValue();
    public abstract void setExtraValue(String value);

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    public void setName(String name) {
        Validator.requireNonBlank(name, "Nombre");
        this.name = name.trim();
    }

    public void SetPhone(String phone) {
        Validator.validatePhone(phone);
        this.phone = phone.trim();
    }

    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email.trim();
    }

    // Persistencia simple: type|id|name|phone|email|extra
    public String toRecord() {
        return String.join("|",
                escape(type()),
                escape(id),
                escape(name),
                escape(phone),
                escape(email),
                escape(extraValue())
        );
    }

    protected static String escape(String s) {
        // Evita romper el separador: reemplaza "|" por "\|"
        return (s == null ? "" : s).replace("\\", "\\\\").replace("|", "\\|");
        }
    
    public static String unescape(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (esc) {
                out.append(c);
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    @Override
    public String toString() {
        return "[" + type() + "] " + name + " | " + phone + " | " + email + " | " + extraLabel() + ": " + extraValue() + " |  id=" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contact other)) return false;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}