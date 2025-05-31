package main.application.service;

import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.model.Personagem;
import main.application.repository.PersonagemJsonRepository;

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
    public void salvarHerois(List<Heroi> listaPersonagens) {
        this.listaHerois = listaPersonagens;
        PersonagemJsonRepository.salvarHerois(listaPersonagens);  // Salva em herois.json
    }

    // Salvar Bestas
    public void salvarBestas(List<Besta> listaPersonagens) {
        this.listaBestas = listaPersonagens;
        PersonagemJsonRepository.salvarBestas(listaPersonagens); // Salva em bestas.json
    }

    // adicionar novo Herói
    public void adicionarHeroi(Heroi heroi) {
        listaHerois.add(heroi);
        cache.put(heroi.getId(), heroi);
        salvarHerois(listaHerois);
    }

    // adicionar nova Besta
    public void adicionarBesta(Besta besta) {
        listaBestas.add(besta);
        cache.put(besta.getId(), besta);
        salvarBestas(listaBestas);
    }

    // Editar Herói
    public void atualizarHeroi(Heroi heroi) {
        for (int i = 0; i < listaHerois.size(); i++) {
            if (listaHerois.get(i).getId().equals(heroi.getId())) {
                listaHerois.set(i, heroi);
                cache.put(heroi.getId(), heroi);
                return;
            }
        }
    }

    // Editar Besta
    public void atualizarBesta(Besta besta) {
        for (int i = 0; i < listaBestas.size(); i++) {
            if (listaBestas.get(i).getId().equals(besta.getId())) {
                listaBestas.set(i, besta);
                cache.put(besta.getId(), besta);
                return;
            }
        }
    }

    // Remover Herói
    public void removerHeroi(String id) {
        listaHerois.removeIf(heroi -> heroi.getId().equals(id));
        cache.remove(id);
    }

    // Remover Besta
    public void removerBesta(String id) {
        listaBestas.removeIf(besta -> besta.getId().equals(id));
        cache.remove(id);
    }

    // salva todas as listas (Heróis e Bestas)
    public void salvarTodos() {
        PersonagemJsonRepository.salvarHerois(listaHerois);
        PersonagemJsonRepository.salvarBestas(listaBestas);
    }
}
