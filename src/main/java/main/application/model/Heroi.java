package main.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.application.model.enums.Tipo;

/**
 * Class Herói, que é uma personagem
 */
public class Heroi extends Personagem {
    public Heroi() {
        super();
    }

    public Heroi(@JsonProperty("id") String id,  // Pode permitir um id vindo do JSON
                 @JsonProperty("tipo") Tipo tipo,
                 @JsonProperty("nome") String nome,
                 @JsonProperty("vida") Integer vida,
                 @JsonProperty("armadura") Integer armadura,
                 @JsonProperty("ataque") Integer ataque) {
        super(id, tipo, nome, vida, armadura, ataque);
    }
}
