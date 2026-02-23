package com.portfolio.contacts.repo;

import com.portfolio.contacts.domain.CompanyContact;
import com.portfolio.contacts.domain.Contact;
import com.portfolio.contacts.domain.PersonContact;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileContactRepository implements ContactRepository {
    private final Path dbPath;
    private final InMemoryContactRepository mem = new InMemoryContactRepository();

    public FileContactRepository(Path dbPath) {
        this.dbPath = Objects.requireNonNull(dbPath, "dbPath");
        loadFromDisk();
    }

    @Override
    public List<Contact> findAll() {
        return mem.findAll();
    }

    @Override
    public Optional<Contact> findById(String id) {
        return mem.findById(id);
    }

    @Override
    public List<Contact> searchByName(String query) {
        return mem.searchByName(query);
    }

    @Override
    public void save(Contact contact) {
        mem.save(contact);
        persist();
    }

    @Override
    public boolean deleteById(String id) {
        boolean ok = mem.deleteById(id);
        if (ok) persist();
        return ok;
    }

    private void loadFromDisk() {
        try {
            Files.createDirectories(dbPath.getParent());
            if (!Files.exists(dbPath)) {
                Files.writeString(dbPath, "", StandardCharsets.UTF_8, StandardOpenOption.CREATE);
                return;
            }
            List<String> lines = Files.readAllLines(dbPath, StandardCharsets.UTF_8);
            List<Contact> loaded = new ArrayList<>();

            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                Contact c = parseRecord(trimmed);
                loaded.add(c);
            }

            mem.putAll(loaded);
        } catch (IOException e) {
            // Si falla el disco, el programa aún puede correr en memoria
            System.err.println("WARN: No se pudo cargar la base de contactos: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("WARN: Archivo corrupto o inválido: " + e.getMessage());
        }
    }

    private void persist() {
        // Escritura segura: temporal + move
        Path tmp = dbPath.resolveSibling(dbPath.getFileName().toString() + ".tmp");
        List<String> lines = mem.findAll().stream()
                .sorted(Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER))
                .map(Contact::toRecord)
                .toList();

        try {
            Files.createDirectories(dbPath.getParent());
        Files.write(tmp, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.move(tmp, dbPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("WARN: No se pudo persistir el archivo: " + e.getMessage());
        }
    }
    
    private static Contact parseRecord(String record) {
        // type|id|name|phone|emai|extra
        List<String> parts = splitEscaped(record, '|');
        if (parts.size() < 6) {
            throw new IllegalArgumentException("Registro incompleto: " + record);
        
        }

        String type = Contact.unescape(parts.get(0));
        String id = Contact.unescape(parts.get(1));
        String name = Contact.unescape(parts.get(2));
        String phone = Contact.unescape(parts.get(3));
        String email = Contact.unescape(parts.get(4));
        String extra = Contact.unescape(parts.get(5));

        return switch (type) {
            case "PERSON" -> new PersonContact(id, name, phone, email, extra);
            case "COMPANY" -> new CompanyContact(id, name, phone, email, extra);
            default -> throw new IllegalArgumentException("Tipo desconocido: " + type);  
        };
    }

    private static List<String> splitEscaped(String s, char delim) {
        List<String> out = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        boolean esc = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (esc) {
                token.append(c);
                esc = false;
            } else if (c == '\\') {
                esc = true;
            } else if (c == delim) {
                out.add(token.toString());
                token.setLength(0);
            } else {
                token.append(c);
            }
        }
        out.add(token.toString());
        return out;
    }

}
