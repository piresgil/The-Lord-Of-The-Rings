package main.application.service;

import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.model.Personagem;
import main.application.repository.PersonagemJsonRepository;
import main.application.utils.JsonUtils;

import java.util.*;

/**
 * Class Manager
 * carregar, alterar, grava, eliminar, personagens e fazer a atualizações no repositório JSON
 */
public class PersonagemManager {
    private List<Heroi> listaHerois;
    private List<Besta> listaBestas;
    private final Map<String, Personagem> cache;

    public PersonagemManager() {
        // Copia os heróis carregados para um novo ArrayList mutável
        this.listaHerois = new ArrayList<>(PersonagemJsonRepository.carregarHerois());
        // Copia as bestas carregadas para um novo ArrayList mutável
        this.listaBestas = new ArrayList<>(PersonagemJsonRepository.carregarBestas());
        this.cache = new HashMap<>(); // Cache para todos os personagens? Ou caches separados?
        inicializarCache();
    }

    // cache para personagens para não haver inconsistência na persistência dos dados
    private void inicializarCache() {
        listaHerois.forEach(heroi -> cache.put(heroi.getId(), heroi));
        listaBestas.forEach(besta -> cache.put(besta.getId(), besta));
    }

    /**
     * Busca um personagem pelo ID no cache.
     *
     * @param id O ID do personagem.
     * @return O personagem encontrado ou Optional vazio se não encontrado.
     */
    public Optional<Personagem> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    // Busca todos os Herois
    public List<Personagem> findAllHerois() {
        return new ArrayList<>(listaHerois);
    }

    // Busca todos os Herois
    public List<Personagem> findAllBestas() {
        return new ArrayList<>(listaBestas);
    }

    // Salvar Heróis
    public void salvarHerois() {
        PersonagemJsonRepository.salvarHerois(this.listaHerois);
    }

    // Salvar Bestas
    public void salvarBestas() {
        PersonagemJsonRepository.salvarBestas(this.listaBestas);
    }

    // adicionar novo Herói
    public void adicionarHeroi(Heroi heroi) {
        listaHerois.add(heroi);
        cache.put(heroi.getId(), heroi);
        salvarHerois();
    }

    // adicionar nova Besta
    public void adicionarBesta(Besta besta) {
        listaBestas.add(besta);
        cache.put(besta.getId(), besta);
        salvarBestas();
    }

    // Editar Herói
    public void atualizarHeroi(Heroi heroiAtualizado) {
        for (int i = 0; i < listaHerois.size(); i++) {
            if (listaHerois.get(i).getId().equals(heroiAtualizado.getId())) {
                listaHerois.set(i, heroiAtualizado);
                return;
            }
        }
        System.out.println("Herói não encontrado para atualizar.");
    }

    // Editar Besta
    public void atualizarBesta(Besta bestaAtualizada) {
        for (int i = 0; i < listaBestas.size(); i++) {
            if (listaBestas.get(i).getId().equals(bestaAtualizada.getId())) {
                listaBestas.set(i, bestaAtualizada);
                return;
            }
        }
        System.out.println("Herói não encontrado para atualizar.");
    }

    // Remover Herói
    public void removerHeroi(String id) {
        boolean removido = listaHerois.removeIf(heroi -> heroi.getId().equals(id));
        if (removido) {
            cache.remove(id);
            salvarHerois();
        }
    }

    // Remover Besta
    public void removerBesta(String id) {
        boolean removido = listaBestas.removeIf(besta -> besta.getId().equals(id));
        if (removido) {
            cache.remove(id);
            salvarBestas();
        }
    }

    // salva todas as listas (Heróis e Bestas)
    public void salvarTodos() {
        PersonagemJsonRepository.salvarHerois(listaHerois);
        PersonagemJsonRepository.salvarBestas(listaBestas);
    }
}
