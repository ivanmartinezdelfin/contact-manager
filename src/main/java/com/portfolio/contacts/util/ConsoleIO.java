package com.portfolio.contacts.util;

import java.util.Scanner;

public class ConsoleIO {
    private final Scanner scanner = new Scanner(System.in);

    public void println(String s) {
        System.out.println(s);
    }

    public void print(String s) {
        System.out.println(s); 
    }

    public String readLine(String prompt) {
        print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while(true) {
            String raw = readLine(prompt);
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                println("Entrada inválida. Ingresa un número.");
            }
        }
    }
    public void pause(String prompt) {
        readLine(prompt);
    }
}