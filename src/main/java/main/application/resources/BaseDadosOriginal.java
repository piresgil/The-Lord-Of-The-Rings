package main.application.resources;

import javafx.collections.FXCollections;
import lombok.Data;
import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.model.enums.Tipo;

import java.util.List;

/**
 * Base de dados original, para Heróis e Bestas
 */
@Data
public class BaseDadosOriginal {

    public static final List<Heroi> HEROIS_ORIGEM = List.of(
            new Heroi(null, Tipo.ELFO, "Legolas", 150, 30, 10),
            new Heroi(null, Tipo.ELFO, "Glorfindel", 160, 55, 10),
            new Heroi(null, Tipo.ELFO, "Haldir", 155, 53, 10),
            new Heroi(null, Tipo.GUERREIRO, "Aragorn", 150, 50, 20),
            new Heroi(null, Tipo.GUERREIRO, "Boromir", 100, 60, 20),
            new Heroi(null, Tipo.GUERREIRO, "Aragorn", 150, 50, 20),
            new Heroi(null, Tipo.MAGO, "Gandalf", 200, 30, 30),
            new Heroi(null, Tipo.MAGO, "Radagast", 170, 58, 30),
            new Heroi(null, Tipo.MAGO, "Pallando", 160, 60, 30),
            new Heroi(null, Tipo.HOBBIT, "Frodo", 100, 30, 40),
            new Heroi(null, Tipo.HOBBIT, "Sam", 120, 40, 40),
            new Heroi(null, Tipo.HOBBIT, "Merry", 110, 35, 40)
    );

    public static final List<Besta> BESTAS_ORIGEM = List.of(
            new Besta(null, Tipo.ORQUES, "Lurtz", 200, 60, 10),
            new Besta(null, Tipo.ORQUES, "Shagrat", 220, 50, 10),
            new Besta(null, Tipo.ORQUES, "Gothmog", 120, 40, 10),
            new Besta(null, Tipo.TROLLS, "Uglúk", 120, 30, 20),
            new Besta(null, Tipo.TROLLS, "Mauhúr", 100, 30, 20),
            new Besta(null, Tipo.TROLLS, "Bolg", 110, 35, 20),
            new Besta(null, Tipo.ARANHAS, "Shelob", 150, 50, 30),
            new Besta(null, Tipo.ARANHAS, "Ungoliant", 160, 55, 30),
            new Besta(null, Tipo.ARANHAS, "Glaurung", 165, 58, 30),
            new Besta(null, Tipo.DRAGOES, "Drogoth", 130, 45, 40),
            new Besta(null, Tipo.DRAGOES, "Smaug", 170, 60, 40),
            new Besta(null, Tipo.DRAGOES, "Scatha", 155, 55, 40)
    );

    // Retorna cópias modificáveis
    public static List<Heroi> getListaHeroisOriginais() {
        return FXCollections.observableArrayList(HEROIS_ORIGEM);
    }

    public static List<Besta> getListaBestasOriginais() {
        return FXCollections.observableArrayList(BESTAS_ORIGEM);
    }
}
