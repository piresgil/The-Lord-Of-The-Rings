package main.application.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.application.model.enums.Tipo;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Class Abstrata de personagem
 */
@Data
public abstract class Personagem {
    @NotNull
    private String id;
    @NotNull(message = "O tipo é obrigatório.")
    private Tipo tipo;
    @NotNull(message = "O nome é obrigatório.")
    private String nome;
    @NotNull(message = "A vida é obrigatório.")
    private Integer vida;
    @NotNull(message = "A armadura é obrigatório.")
    private Integer armadura;
    @NotNull(message = "O ataque é obrigatório.")
    private Integer ataque;

    @JsonCreator
    public Personagem() {

    }

    @JsonCreator
    public Personagem(@JsonProperty("id") String id,  // Pode permitir um id vindo do JSON
                      @JsonProperty("tipo") Tipo tipo,
                      @JsonProperty("nome") String nome,
                      @JsonProperty("vida") Integer vida,
                      @JsonProperty("armadura") Integer armadura,
                      @JsonProperty("ataque") Integer ataque) {
        this.id =  UUID.randomUUID().toString();

        this.tipo = tipo;
        this.nome = nome;
        this.vida = vida;
        this.armadura = armadura;
        this.ataque = ataque;
    }

    protected Integer getMaxRolagem() {
        Integer MAXIMO_DADOS_ROLAGEM = 0;

        if (this.tipo == Tipo.ELFO || this.tipo == Tipo.GUERREIRO || this.tipo == Tipo.MAGO || this.tipo == Tipo.HOBBIT) {
            MAXIMO_DADOS_ROLAGEM = 100;
        }
        if (this.tipo == Tipo.ORQUES || this.tipo == Tipo.TROLLS || this.tipo == Tipo.ARANHAS || this.tipo == Tipo.DRAGOES) {
            MAXIMO_DADOS_ROLAGEM = 90;
        }
        return MAXIMO_DADOS_ROLAGEM;
    }

    public Integer rolar() {
        Integer ataque = Dado.rolar(getMaxRolagem()); // Primeira rolagem

        // Se for um Herói, realiza uma segunda rolagem e escolhe o maior valor
        if (this.tipo == Tipo.ELFO || this.tipo == Tipo.GUERREIRO || this.tipo == Tipo.MAGO || this.tipo == Tipo.HOBBIT) {
            Integer segundaRolagem = Dado.rolar(getMaxRolagem());
            ataque = Math.max(ataque, segundaRolagem);
        }
        return ataque;
    }

    public Integer combater(Personagem defensor) {
        if (defensor.getVida() < 0) {
            return 0;
        }
        ataque = rolar();

        // System.out.println((String.format("%s, %s(Vida: %d, Def: %d) ataca %s, %s(Vida: %d, Def: %d)", getNome(), getTipo(), getVida(), getArmadura(),
        //         getNome(), defensor.getTipo(), defensor.getVida(), defensor.getArmadura())));

        // 2. Modificadores de ataque baseados no tipo
        if (this.getTipo() == Tipo.ELFO && defensor.getTipo() == Tipo.ORQUES) {
            ataque += 10;  // Elfos ganham +10 contra Orques
        }
        if (this.getTipo() == Tipo.HOBBIT && defensor.getTipo() == Tipo.TROLLS) {
            ataque -= 5;  // Hobbits perdem -5 contra Trolls
        }

        // 3. Calculando a armadura reduzida caso o atacante seja um ORQUE (aplicado apenas neste turno)
        Integer armaduraEfetiva = defensor.getArmadura();
        if (this.getTipo() == Tipo.ORQUES) {
            armaduraEfetiva -= (int) (armaduraEfetiva * 0.1); // Reduz 10%
        }

        // 4. Calculando dano real
        Integer dano = ataque > armaduraEfetiva ? (ataque - armaduraEfetiva) : 0; // Dano mínimo é 0

        // 5. Aplica o dano ao defensor
        defensor.setVida(defensor.getVida() - dano);
        // batalhas.add(String.format("        %s atacou com %d causando %d de dano a %s.",
        //         this.getNome(), ataque, this.dano, defensor.getNome()));
        System.out.println(String.format("        %s atacou com %d causando %d de dano a %s.",
                this.getNome(), ataque, dano, defensor.getNome()));
        return dano;
    }
}
