package main.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.application.listener.AtualizarJsonListener;
import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.resources.BaseDadosOriginal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cass Util para manipular os JSON
 */
public class JsonUtils {
    // Object Mapper - facilita a conversão entre objetos de programação (como objetos Java) e outros formatos de dados, como JSON ou XML
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // Caminhos para os arquivos JSON
    private static final String JSON_DIR = "src/main/resources/json";  // Diretório onde os arquivos JSON serão salvos
    private static final String HEROIS_FILE = "herois.json";
    private static final Path HEROIS_PATH = Paths.get(JSON_DIR, HEROIS_FILE);
    private static final String BESTAS_FILE = "bestas.json";
    private static final Path BESTAS_PATH = Paths.get(JSON_DIR, BESTAS_FILE);


    private static AtualizarJsonListener listener;

    // Regista o listener
    public static void setListener(AtualizarJsonListener listener) {
        JsonUtils.listener = listener;
    }

    // Criar a pasta se não existir
    private static void criarDiretorio() {
        File diretorio = new File(JSON_DIR);
        if (!diretorio.exists()) {
            boolean sucesso = diretorio.mkdirs();  // Cria os diretórios
            if (sucesso) {
                System.out.println("Diretório criado com sucesso: " + JSON_DIR);
            } else {
                System.out.println("Falha ao criar o diretório.");
            }
        }
    }

    // Criar Json de Herois
    public static void criarJsonHerois() {
        criarDiretorio();  // Garante que o diretório existe

        if (!HEROIS_PATH.toFile().exists()) {
            List<Heroi> herois = BaseDadosOriginal.getListaHeroisOriginais();

            if (herois.isEmpty()) {
                System.out.println("Não há heróis para salvar. Lista está vazia.");
            } else {
                System.out.println("Salvando " + herois.size() + " heróis.");
                salvarHerois(herois, HEROIS_PATH);
                System.out.println("Json " + HEROIS_FILE + " criado com sucesso.");
                // Chama o listener para atualizar as listas
                if (listener != null) {
                    listener.atualizarJsonListener();  // Atualiza as listas na interface
                }
            }
        } else {
            System.out.println("O arquivo JSON de heróis já existe.");
        }
    }

    // Criar Json de Bestas
    public static void criarJsonBestas() {
        criarDiretorio();  // Garante que o diretório existe

        if (!BESTAS_PATH.toFile().exists()) {
            List<Besta> bestas = BaseDadosOriginal.getListaBestasOriginais();

            if (bestas.isEmpty()) {
                System.out.println("Não há bestas para salvar. Lista está vazia.");
            } else {
                System.out.println("Salvando " + bestas.size() + " bestas.");
                salvarBestas(bestas, BESTAS_PATH);
                System.out.println("Json " + BESTAS_FILE + " criado com sucesso.");
                // Chama o listener para atualizar as listas
                if (listener != null) {
                    listener.atualizarJsonListener();  // Atualiza as listas na interface
                }
            }
        } else {
            System.out.println("O arquivo JSON de bestas já existe.");
        }
    }

    /**
     * Metodo genérico para salvar uma lista de personagens em um arquivo JSON.
     * @param listaPersonagens Lista de personagens (pode ser de qualquer tipo).
     * @param filePath Caminho do arquivo onde será salvo o JSON.
     */
    private static <T> void salvarPersonagens(List<T> listaPersonagens, Path filePath) {
        try {
            File diretorioPai = filePath.toFile().getParentFile();
            if (diretorioPai != null) {
                diretorioPai.mkdirs();
            }

            mapper.registerModule(new JavaTimeModule());
            mapper.writeValue(filePath.toFile(), listaPersonagens);

            System.out.printf("Personagens salvos com sucesso em: %s%n", filePath.toAbsolutePath().normalize());

        } catch (IOException e) {
            System.err.printf("ERRO ao salvar personagens em %s: %s%n", filePath.toAbsolutePath(), e.getMessage());
            throw new RuntimeException("Falha ao persistir personagens", e);
        }
    }

    /**
     * Metodo específico para salvar a lista de heróis em arquivo JSON.
     * Chama o metodo genérico salvarPersonagens.
     * @param herois Lista de objetos Heroi.
     * @param filePath Caminho do arquivo JSON para salvar os heróis.
     */
    private static void salvarHerois(List<Heroi> herois, Path filePath) {
        salvarPersonagens(herois, filePath);
    }

    /**
     * Metodo específico para salvar a lista de bestas em arquivo JSON.
     * Chama o metodo genérico salvarPersonagens.
     * @param bestas Lista de objetos Besta.
     * @param filePath Caminho do arquivo JSON para salvar as bestas.
     */
    private static void salvarBestas(List<Besta> bestas, Path filePath) {
        salvarPersonagens(bestas, filePath);
    }

    // Carregar a lista de Heróis a partir do JSON
    public static List<Heroi> carregarHerois() {
        return carregarJson(HEROIS_PATH, Heroi[].class);
    }

    // Carregar a lista de Bestas a partir do JSON
    public static List<Besta> carregarBestas() {
        return carregarJson(BESTAS_PATH, Besta[].class);
    }

    // Método genérico para carregar o JSON de qualquer lista de personagens
    private static <T> List<T> carregarJson(Path filePath, Class<T[]> type) {
        try {
            // Verificar se o arquivo realmente existe
            if (!Files.exists(filePath)) {
                System.out.println("Arquivo JSON não encontrado: " + filePath);
                return new ArrayList<>();
            }

            String json = new String(Files.readAllBytes(filePath));  // Lê o conteúdo como string

            if (json.trim().isEmpty()) {
                System.out.println("O arquivo JSON está vazio.");
                return new ArrayList<>();
            }

            // Tenta ler o arquivo JSON como uma lista de personagens
            return Arrays.asList(mapper.readValue(filePath.toFile(), type));  // Lê conforme o tipo fornecido
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo JSON: " + e.getMessage());
            return new ArrayList<>();  // Retorna uma lista vazia em caso de erro
        }
    }

    private static <T> void updatePersonagens(List<T> personagens, Path filePath, String tipo) {
        if (personagens.isEmpty()) {
            System.out.println("Não há " + tipo + " para salvar. Lista está vazia.");
        } else {
            System.out.println("Salvando " + personagens.size() + " " + tipo + ".");
            salvarPersonagens(personagens, filePath);
            System.out.println("Json salvo com sucesso.");
        }
    }
    // Salvar as alterações feitas na lista de heróis no arquivo JSON
    public static void updateHerois(List<Heroi> heroisAlterados) {
        updatePersonagens(heroisAlterados, HEROIS_PATH, "heróis");
    }

    // Salvar as alterações feitas na lista de bestas no arquivo JSON
    public static void updateBestas(List<Besta> bestasAlteradas) {
        updatePersonagens(bestasAlteradas, BESTAS_PATH, "bestas");
    }

}
