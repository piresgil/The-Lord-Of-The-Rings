package main.application.repository;

import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.utils.JsonUtils;

import java.util.List;

/**
 * JSON Repository
 * faz as persistências nos JSON
 */
public class PersonagemJsonRepository {

    private static JsonUtils jsonUtils;

    public PersonagemJsonRepository() {
        this.jsonUtils = new JsonUtils();
    }

    public static List<Heroi> carregarHerois() {
        return jsonUtils.carregarHerois();
    }

    public static List<Besta> carregarBestas() {
        return jsonUtils.carregarBestas();
    }

    public static void salvarHerois(List<Heroi> herois) {
        jsonUtils.updateHerois(herois);
    }

    public static void salvarBestas(List<Besta> bestas) {
        jsonUtils.updateBestas(bestas);
    }
}
