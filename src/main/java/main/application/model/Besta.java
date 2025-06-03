package main.application.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.application.model.enums.Tipo;

/**
 * Class Besta, que é uma personagem
 */
public class Besta extends Personagem {
    public Besta() {
        super();
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Besta(@JsonProperty("id") String id,  // Pode permitir um id vindo do JSON
                 @JsonProperty("tipo") Tipo tipo,
                 @JsonProperty("nome") String nome,
                 @JsonProperty("vida") Integer vida,
                 @JsonProperty("armadura") Integer armadura,
                 @JsonProperty("ataque") Integer ataque) {
        super(id, tipo, nome, vida, armadura, ataque);
    }
}
