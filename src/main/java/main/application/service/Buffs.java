package main.application.service;

import main.application.model.Dado;
import main.application.model.Personagem;

import java.util.List;

/**
 * Class Buffs, Lógica de buffs a serem lançados a cada turno, pode haver ou não a haver
 */
public class Buffs {

    // Método que aplica os buffs a duas listas separadas de heróis e bestas
    public static String aplicarBuffs(List<Personagem> herois, List<Personagem> bestas, int turno) {
        String resultado = "";

        // Gera um número aleatório entre 1 e 4 para escolher os buff
        int buffEscolhido = Dado.rolar(4);
        int dano = 1; // nao permite dano zero
        dano += Dado.rolar(19);

        // Aplicar os buff a todos os heróis
        for (Personagem heroi : herois) {
            if (buffEscolhido == 1 && turno % 5 == 0) {  // A cada 5 turnos
                if (heroi.getVida() > 0) {
                    int novaVida = Math.max(0, heroi.getArmadura() - dano); // Evita armadura negativa
                    heroi.setVida(novaVida); // Ignorando armadura
                }
            }

            if (buffEscolhido == 2 && turno % 3 == 0) {  // A cada 3 turnos
                if (heroi.getVida() > 0) {
                    heroi.setVida(heroi.getVida() + dano);  // Recuperando vida
                }
            }

            if (buffEscolhido == 3 && turno % 2 == 0) {  // A cada 2 turnos
                if (heroi.getVida() > 0) {
                    heroi.setArmadura(heroi.getArmadura() + dano);  // Ignorando armadura
                }
            }

            if (buffEscolhido == 4) {  // todos turnos
                if (heroi.getVida() > 0) {
                    int novaArmadura = Math.max(0, heroi.getArmadura() - dano); // Evita armadura negativa
                    heroi.setArmadura(novaArmadura);
                }
            }
        }

        // Aplicar os buff a todas as bestas
        for (Personagem besta : bestas) {
            if (buffEscolhido == 1 && turno % 5 == 0) {  // A cada 5 turnos
                if (besta.getVida() > 0) {
                    int novaVida = Math.max(0, besta.getArmadura() - dano); // Evita armadura negativa
                    besta.setVida(novaVida); // Ignorando armadura
                }
                //System.out.println(besta.getNome() + "Buff, sofreu " + dano + " de dano (Terramoto). ");
            }

            if (buffEscolhido == 2 && turno % 3 == 0) {  // A cada 3 turnos
                if (besta.getVida() > 0) {
                    besta.setVida(besta.getVida() + dano);  // Recuperando vida
                }
            }

            if (buffEscolhido == 3 && turno % 2 == 0) {  // A cada 2 turnos
                if (besta.getVida() > 0) {
                    besta.setArmadura(besta.getArmadura() + dano);  // Ignorando armadura
                }
            }

            if (buffEscolhido == 4) {  // todos turnos
                if (besta.getVida() > 0) {
                    int novaArmadura = Math.max(0, besta.getArmadura() - dano); // Evita armadura negativa
                    besta.setArmadura(novaArmadura);
                }
            }
        }

        if (buffEscolhido == 1 && turno % 5 == 0) {
            resultado += "Buff, sofrem " + dano + " de Dano. (Terramoto)";
        }

        if (buffEscolhido == 2 && turno % 3 == 0) {
            resultado += "Buff, ganham " + dano + " de Vida. (Bênção)";
        }

        if (buffEscolhido == 3 && turno % 2 == 0) {
            resultado += "Buff, ganham " + dano + " de Defesa. (Proteção)";
        }

        if (buffEscolhido == 4) {
            resultado += "Buff, sofrem " + dano + " de Defesa. (Maldição)";
        }

        return resultado.isEmpty() ? "Buff, nada aplicado." : resultado;
    }
}
