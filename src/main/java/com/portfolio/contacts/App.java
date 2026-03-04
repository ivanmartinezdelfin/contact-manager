package com.portfolio.contacts;

import com.portfolio.contacts.cli.Menu;
import com.portfolio.contacts.repo.ContactRepository;
import com.portfolio.contacts.repo.FileContactRepository;
import com.portfolio.contacts.service.ContactService;
import com.portfolio.contacts.util.ConsoleIO;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        ConsoleIO io = new ConsoleIO();

        // Persistencia simple en /data/contacts.db (se crea si no existe)
        Path dbPath = Paths.get("data", "contacts.db");
        ContactRepository repo = new FileContactRepository(dbPath);

        ContactService service = new ContactService(repo);
        Menu menu = new Menu(io, service);

        menu.run();
    }
}