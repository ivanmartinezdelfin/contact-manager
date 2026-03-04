package com.portfolio.contacts.cli;

import com.portfolio.contacts.domain.Contact;
import com.portfolio.contacts.service.ContactService;
import com.portfolio.contacts.util.ConsoleIO;

import java.util.List;
import java.util.NoSuchElementException;

public class Menu {
    private final ConsoleIO io;
    private final ContactService service;

    public Menu(ConsoleIO io, ContactService service) {
        this.io = io;
        this.service = service;
    }

    public void run() {
        while (true) {
            io.println("");
            io.println("=== Contact Manager ===");
            io.println("1) Agregar contacto (Persona)");
            io.println("2) Agregar contacto (Empresa)");
            io.println("3) Listar contactos");
            io.println("4) Buscar por nombre");
            io.println("5) Actualizar contacto por id");
            io.println("6) Eliminar contacto por id");
            io.println("7) Salir");

            int opt = io.readInt("Opcion: ");
            try {
                switch (opt) {
                    case 1 -> addPerson();
                    case 2 -> addCompany();
                    case 3 -> listAll();
                    case 4 -> search();
                    case 5 -> update();
                    case 6 -> delete();
                    case 0 -> {
                        io.println("Bye.");
                        return;
                    }
                    default -> io.println("Opción inválida.");

                }
            } catch (IllegalArgumentException e) {
                io.println("ERROR: " + e.getMessage());
                io.pause("Enter para continuar...");
            } catch (NoSuchElementException e) {
                io.println("ERROR: " + e.getMessage());
                io.pause("Enter para continuar...");
            } catch (Exception e) {
                io.println("ERROR: inesperado" + e.getClass().getSimpleName() + " - " + e.getMessage());
                io.pause("Enter para continuar...");
            }

        }
    }
    private void addPerson() {
        io.println("\n-- Alta Persona --");
        String name = io.readLine("Nombre: ");
        String phone = io.readLine("Teléfono: ");
        String email = io.readLine("Email: ");
        String notes = io.readLine("Notas (opcional): ");

        Contact c = service.createPerson(name, phone, email, notes);
        io.println("Creado: " + c);
        io.pause("Enter para continuar...");
    }

    private void addCompany() {
        io.println("\n -- Alta Empresa --");
        String name = io.readLine("Nombre de contacto: ");
        String phone = io.readLine("Teléfono: ");
        String email = io.readLine("Email: ");
        String company = io.readLine("Empresa: ");

        Contact c = service.createCompany(name, phone, email, company);
        io.println("Creado: " + c);
        io.pause("Enter para continuar...");
    }

    private void listAll() {
        io.println("\n-- Lista --");
        List<Contact> all = service.listAll();
        if (all.isEmpty()) {
            io.println("Sin contactos.");
        } else {
            for (Contact c : all) io.println(c.toString());
        }
        io.pause("Enter para continuar...");
    }
    private void search() {
        io.println("\n-- Buscar --");
        String q = io.readLine("Nombre contiene: ");
        List<Contact> found = service.searchByName(q);
        if (found.isEmpty()) {
            io.println("Sin resultados.");
        } else {
            for (Contact c : found) io.println(c.toString());
        }
        io.pause("Enter para continuar...");
    }

    private void update() {
        io.println("\n-- Actualizar --");
        String id = io.readLine("Id: ");

        Contact c = service.getById(id).orElseThrow(() -> new NoSuchElementException("No existe contacto con id: " + id));
        io.println("Actual: " + c);

        io.println("Deja vacío si no quieres cambiar ese campo");
        String newName = io.readLine("Nuevo nombre: ");
        String newPhone = io.readLine("Nuevo teléfono: ");
        String newEmail = io.readLine("Nuevo email ");
        String newExtra = io.readLine("Nuevo " + c.extraLabel() + ": ");

        Contact updated = service.updateBasic(id, newName, newPhone, newEmail, newExtra);
        io.println("Actualizado " + updated);
        io.pause("Enter para continuar...");
    }
    
    private void delete() {
        io.println("\n-- Eliminar --");
        String id = io.readLine("Id: ");
        boolean ok = service.delete(id);
        io.println(ok ? "Eliminado." : "No se encontró el id.");
        io.pause("Enter para continuar...");
    }
}