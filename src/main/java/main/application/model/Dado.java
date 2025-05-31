package main.application.model;

import java.util.Random;

/**
 * Class Dado,
 * representa um dado que retorna um número, usado para lógicas de ataques e buffs
 */
public class Dado {
    public static Integer rolar(Integer max) {
        Random random = new Random();

        return random.nextInt(max + 1);
    }
}
