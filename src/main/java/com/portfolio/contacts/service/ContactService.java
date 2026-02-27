package com.portfolio.contacts.service;

import com.portfolio.contacts.domain.CompanyContact;
import com.portfolio.contacts.domain.Contact;
import com.portfolio.contacts.domain.PersonContact;
import com.portfolio.contacts.repo.ContactRepository;
import com.portfolio.contacts.util.Validator;

import java.util.*;

public  class ContactService {
    private final ContactRepository repo;

    public ContactService(ContactRepository repo) {
        this.repo = Objects.requireNonNull(repo, "repo");

    }

    public Contact createPerson(String name, String phone, String email, String notes ) {
        Validator.requireNonBlank(name, "Nombre");
        Validator.validatePhone(phone);
        Validator.validateEmail(email);

        String id = UUID.randomUUID().toString();
        Contact c = new PersonContact(id, name, phone, email, Validator.safe(notes));
        repo.save(c);
        return c;

    }

    public Contact createCompany(String name, String phone, String email, String company) {
        Validator.requireNonBlank(name, "Nombre");
        Validator.validatePhone(phone);
        Validator.validateEmail(email);

        String id = UUID.randomUUID().toString();
        Contact c = new CompanyContact(id, name, phone, email, company);
        repo.save(c);
        return c;
    }

    public List<Contact> listAll() {
        return repo.findAll().stream()
                .sorted(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public List<Contact> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        return repo.searchByName(query).stream()
                .sorted(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Optional<Contact> getById(String id) {
        return repo.findById(id);
    }

    public Contact updateBasic(String id, String newName, String newPhone, String newEmail, String newExtra) {
        Contact c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("No existe contacto con id:" + id));

        if (newName != null && !newName.trim().isEmpty()) c.setName(newName);
        if (newPhone != null && !newPhone.trim().isEmpty()) c.setPhone(newPhone);
        if (newEmail != null && !newEmail.trim().isEmpty()) c.setEmail(newEmail);
        if (newExtra != null) c.setExtraValue(newExtra);

        repo.save(c);
        return c;
    }

    public boolean delete(String id) {
        return repo.deleteById(id);
    }

}