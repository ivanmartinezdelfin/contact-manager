package com.portfolio.contacts.repo;

import com.portfolio.contacts.domain.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    List<Contact> findAll();
    Optional<Contact> findById(String id);
    List<Contact> searchByName(String query);

    void save(Contact contact); // Upsert
    boolean deleteById(String id);
}