package main.application.model.enums;

/**
 * Enum do tipo de personagens
 */
public enum Tipo {
    // Tipos de Heróis
    ELFO("HEROI"),
    HOBBIT("HEROI"),
    GUERREIRO("HEROI"),
    MAGO("HEROI"),

    // Tipos de Bestas
    ORQUES("BESTA"),
    TROLLS("BESTA"),
    ARANHAS("BESTA"),
    DRAGOES("BESTA");

    private final String categoria; // O tipo de dado agora é String

    // Construtor para o enum
    Tipo(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Retorna a categoria à qual este tipo de personagem pertence (ex: "HEROI", "BESTA").
     * @return A String que representa a categoria.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Verifica se este tipo de personagem é um Herói.
     * @return true se a categoria for "HEROI", false caso contrário.
     */
    public boolean isHeroi() {
        return this.categoria.equals("HEROI");
    }

    /**
     * Verifica se este tipo de personagem é uma Besta.
     * @return true se a categoria for "BESTA", false caso contrário.
     */
    public boolean isBesta() {
        return this.categoria.equals("BESTA");
    }
}

