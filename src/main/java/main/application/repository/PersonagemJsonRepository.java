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

    // Instância única da classe JsonUtils para realizar operações de carregamento e salvamento de JSON.
    private static final JsonUtils jsonUtils = new JsonUtils();

    /**
     * Metodo público para carregar a lista de heróis do JSON.
     *
     * @return Lista de objetos Heroi carregados do arquivo.
     */
    public static List<Heroi> carregarHerois() {
        return jsonUtils.carregarHerois();
    }

    /**
     * Metodo público para carregar a lista de bestas do JSON.
     *
     * @return Lista de objetos Besta carregados do arquivo.
     */
    public static List<Besta> carregarBestas() {
        return jsonUtils.carregarBestas();
    }

    /**
     * Metodo público para salvar a lista de heróis no JSON.
     * Passa a lista para o metodo responsável pela atualização dos dados no arquivo.
     *
     * @param herois Lista de objetos Heroi a ser salva.
     */
    public static void salvarHerois(List<Heroi> herois) {
        jsonUtils.updateHerois(herois);
    }

    /**
     * Metodo público para salvar a lista de bestas no JSON.
     * Passa a lista para o metodo responsável pela atualização dos dados no arquivo.
     *
     * @param bestas Lista de objetos Besta a ser salva.
     */
    public static void salvarBestas(List<Besta> bestas) {
        jsonUtils.updateBestas(bestas);
    }
}


