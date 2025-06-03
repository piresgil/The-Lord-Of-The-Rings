package main.application.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import main.application.model.Personagem;
import main.application.service.Buffs;
import main.application.utils.PopupUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Class Controladora da batalha
 */
public class BatalhaController extends StackPane {

    @FXML
    private ListView<TextFlow> logView;  // Declarando a ListView no controlador

    @Setter
    private ObservableList<TextFlow> logBatalha = FXCollections.observableArrayList();

    @Getter
    @Setter
    private ObservableList<Personagem> herois;

    @Getter
    @Setter
    private ObservableList<Personagem> bestas;

    @FXML
    @Getter
    public StackPane root;

    private final Runnable onFinish;


    public BatalhaController(Runnable onFinish, List<Personagem> herois, List<Personagem> bestas) {
        // Armazena o parâmetro onFinish
        this.onFinish = onFinish;

        // Configuração das listas no construtor
        this.herois = FXCollections.observableArrayList(herois != null ? herois : new ArrayList<>());
        this.bestas = FXCollections.observableArrayList(bestas != null ? bestas : new ArrayList<>());


        this.root = new StackPane();  // A StackPane que vai ocupar a tela inteira
        configureBattleLayout();


        try {
            FXMLLoader loader = new FXMLLoader();
            // Caminho relativo à localização da classe Batalha
            URL fxmlUrl = getClass().getResource("/main/application/batalha.fxml");

            if (fxmlUrl == null) {
                throw new IllegalStateException("Arquivo FXML não encontrado! Verifique o caminho: " +
                        new File(".").getAbsolutePath());
            }

            loader.setLocation(fxmlUrl);
            loader.setController(this);
            this.root = loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o FXML da batalha.", e);
        }
    }

    // Configuração do layout de batalha estendendo o log de batalha em relação ao menu
    private void configureBattleLayout() {
        // O conteúdo da batalha (exemplo: uma imagem ou outros componentes)
        Label battleLabel = new Label("A batalha está em andamento...");

        // Aqui podemos adicionar um listener de redimensionamento ou definir propriedades para garantir que o battleRoot ocupe toda a tela
        root.setPrefWidth(Double.MAX_VALUE);  // Garante que ocupe a largura máxima
        root.setPrefHeight(Double.MAX_VALUE); // Garante que ocupe a altura máxima

        // Adicionando o conteúdo da batalha no StackPane
        root.getChildren().add(battleLabel); // Este é só um exemplo de conteúdo

        // Se quiser adicionar outros elementos, como animações, logs ou o estado da batalha, pode fazer isso aqui.
    }

    // Method Initialize, inicia a batalha
    @FXML
    public void initialize() {
        logView.refresh();

        // Verifica se as componentes FXML foram injetadas
        if (logView == null) {
            throw new IllegalStateException("Componentes FXML não injetados corretamente!");
        }

        // Configura a ListView
        logView.prefWidthProperty().bind(root.widthProperty());
        logView.prefHeightProperty().bind(root.heightProperty());

        // Inicializa a lista de logs
        logBatalha = FXCollections.observableArrayList();
        logView.setItems(logBatalha);

        if (herois == null) {
            herois = FXCollections.observableArrayList(); // Inicializa com uma lista vazia
        }

        if (bestas == null) {
            bestas = FXCollections.observableArrayList(); // Inicializa com uma lista vazia
        }

        // Configura o CellFactory para como os TextFlows serão exibidos.
        logView.setCellFactory(listView -> new ListCell<TextFlow>() {
            @Override
            protected void updateItem(TextFlow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });

        // Carrega o CSS para estilizar
        root.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
    }

    // Inicia a batalha
    public void iniciarBatalha() {
        // Verifica se as listas estão vazias ou nulas
        if (herois == null || herois.isEmpty() || bestas == null || bestas.isEmpty()) {
            System.out.println("Erro: As listas de heróis ou bestas estão vazias.");
            return; // Não inicia a batalha se as listas estiverem vazias
        }
        new Thread(() -> executarBatalha(herois, bestas)).start();
    }

    // Lógica de execuçao de batalha
    private void executarBatalha(List<Personagem> herois, List<Personagem> bestas) {

        if (herois == null || bestas == null) {
            // Adiciona um log de erro ou lança uma exceção personalizada
            System.out.println("Erro: listas de heróis ou bestas não inicializadas.");
            return;
        }
        int turno = 1;

        // Enquanto nenhuma das listas tiver Personagens continua a executar a batalha
        while (!herois.isEmpty() && !bestas.isEmpty()) {
            int finalTurno = turno;
            CountDownLatch latch = new CountDownLatch(1); // sincronia entre threads

            // Processa turno
            Platform.runLater(() -> {
                processarTurno(finalTurno);
                latch.countDown(); // sinaliza que terminou
            });

            try {
                latch.await(); // espera o Platform.runLater acabar
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // Verifica se ainda existem heróis e bestas com vida antes de aplicar buffs
            if (!herois.isEmpty() && !bestas.isEmpty()) {
                adicionarLinhaLog(logBatalha, Buffs.aplicarBuffs(herois, bestas, turno));  // Exibe a mensagem que nenhum buff foi aplicado
            }
            turno++;
        }

        // Lógica de Fim de batalha
        Platform.runLater(() -> {
            finalizarBatalha();
            // Verifica se há heróis ou bestas em excesso e os remove
            ajustarExercitos();
            if (onFinish != null) {
                onFinish.run();
            }
        });
    }

    // Processa os turnos de jogo
    private void processarTurno(int turno) {

        // Calcula o número de turnos a serem realizados, baseado no exército com menos efetivos
        int tamanho = Math.min(herois.size(), bestas.size()); // Limita ao tamanho do menor exército

        // Cria listas temporárias para as personagens que permanecerão vivas após o turno
        List<Personagem> heroisAtivos = new ArrayList<>(tamanho);
        List<Personagem> bestasAtivas = new ArrayList<>(tamanho);

        //Adiciona linha no log de batalha com o turno
        adicionarLinhaLog(logBatalha, "\nTURNO: " + turno + "\n");

        // Combate 1x1
        for (int i = 0; i < Math.min(herois.size(), bestas.size()); i++) {

            Personagem heroi = herois.get(i);
            Personagem besta = bestas.get(i);

            System.out.println("    " + heroi.getNome() + "(vida: " + heroi.getVida() + ", Def: " + heroi.getArmadura()
                    + ") VS " + besta.getNome() + "(vida: " + besta.getVida() + ", Def: " + besta.getArmadura() + ")");

            String tipoHeroi = String.valueOf(heroi.getTipo());
            if (tipoHeroi != null && tipoHeroi.length() > 0) {
                tipoHeroi = tipoHeroi.substring(0, 1).toUpperCase() + tipoHeroi.substring(1).toLowerCase();
            }
            String tipoBesta = String.valueOf(besta.getTipo());
            if (tipoBesta != null && !tipoBesta.isEmpty()) {
                tipoBesta = tipoBesta.substring(0, 1).toUpperCase() + tipoBesta.substring(1).toLowerCase();
            }

            // Adiciona linha no log de batalha com a vida e a defesa de heróis e bestas
            adicionarLinhaLog(logBatalha, String.format("    %s, %s(Vida: %d, Def: %d) ataca %s, %s(Vida: %d, Def: %d)",
                    heroi.getNome(),
                    tipoHeroi,
                    heroi.getVida(),
                    heroi.getArmadura(),
                    besta.getNome(),
                    tipoBesta,
                    besta.getVida(),
                    besta.getArmadura()));

            // Herói ataca primeiro
            int danoHeroi = heroi.combater(besta);
            // Verifica se a besta continua viva após o ataque do herói
            int danoBesta;

            if (heroi.getVida() > 0) {
                adicionarLinhaLog(logBatalha, String.format("                %s atacou com %d causando %d de dano a %s.",
                        heroi.getNome(), heroi.getAtaque(), danoHeroi, besta.getNome()));
            }

            if (besta.getVida() > 0) { // Se a besta sobreviveu, ela contra-ataca
                danoBesta = besta.combater(heroi);
                adicionarLinhaLog(logBatalha, String.format("                %s atacou com %d causando %d de dano a %s.",
                        besta.getNome(), besta.getAtaque(), danoBesta, heroi.getNome()));
            }

            // Se o herói ainda estiver vivo, continua a participar
            if (heroi.getVida() > 0) {
                heroisAtivos.add(heroi); // Mantém o herói na lista ativa
            } else {
                System.out.println(heroi.getNome() + " foi derrotado! turn: " + turno);
                adicionarLinhaLog(logBatalha, heroi.getNome() + " foi derrotado!");
            }

            // Se a besta ainda estiver viva, continua a participar
            if (besta.getVida() > 0) {
                bestasAtivas.add(besta); // Mantém a besta na lista ativa
            } else {
                System.out.println(besta.getNome() + " foi derrotado! turn: " + turno);
                adicionarLinhaLog(logBatalha, besta.getNome() + " foi derrotado!");
            }
        }

        // Atualiza as listas de heróis e bestas com as personagens que ainda estão vivas
        herois.setAll(heroisAtivos);
        bestas.setAll(bestasAtivas);
        System.out.println("Herois: " + herois.size());
        System.out.println("Bestas: " + bestas.size());
    }

    // Ajusta listas (Heróis e bestas), garante que as listas de exércitos tenham sempre o mesmo numero de personagens
    private void ajustarExercitos() {
        /*
         * No momento em que uma personagem chegue a um nível de vida igual ou inferior a zero irá
         * produzir-se a sua morte pelo que irá eliminar-se da sua posição e irão deslocar-se todos os
         * seus companheiros em posições posteriores para cobrir a baixa. Dessa forma alguma das
         * personagens inativas poderá participar na batalha nos seguintes turnos.
         */
        if (herois.size() > bestas.size()) {
            int descartado = herois.size() - bestas.size();
            for (int i = 0; i < descartado; i++) {
                herois.remove(herois.size() - 1); // Remove o último herói extra
            }
            System.out.println("Os heróis ainda têm personagens, serão descartados.");
        } else if (bestas.size() > herois.size()) {
            int descartado = bestas.size() - herois.size();
            for (int i = 0; i < descartado; i++) {
                bestas.remove(bestas.size() - 1); // Remove a última besta extra
            }
            System.out.println("As bestas ainda têm personagens, serão descartados.");
        }
    }

    // verifica se a batalha finalizou, analisando as listas de heróis e bestas
    private void finalizarBatalha() {
        System.out.println("Finalizar batalha");
        String vencedor = herois.isEmpty() ? "Bestas Venceram!" : "Heróis Venceram!";

        adicionarLinhaLog(logBatalha, ("\n" + vencedor));

        // Obtém a Scene do battleRoot
        Scene scene = root.getScene();

        if (scene != null) {
            Node owner = root;

            Platform.runLater(() -> {
                PopupUtils.showErrorPopup("Batalha Terminada!", "Vencedores: " + vencedor, owner);
            });
            diminuirTextoListView();
        }
    }

    // Faz rolar a lista de logs ate ao final, conforme se adicionam linhas no log de batalha
    public void scrollToEnd(ListView<TextFlow> listView) {
        // Rola até o último item da ListView
        Platform.runLater(() -> {
            if (!listView.getItems().isEmpty()) {
                listView.scrollTo(listView.getItems().size() - 1); // Rola até o último item
            }
        });
    }

    // Adiciona linhas dos logs dos Buffs
    public void adicionarLinhaLog(ObservableList<TextFlow> list, String texto) {
        TextFlow linha = new TextFlow();

        // Primeiro verifica se é um turno para aplicar o estilo geral
        boolean isTurno = texto.toUpperCase().contains("TURNO");
        boolean isBuff = texto.toUpperCase().contains("BUFF");
        boolean isDerrotado = texto.toUpperCase().contains("DERROTADO");
        if (isTurno) {
            linha.getStyleClass().add("battle-log-turn-line");
        }
        if (isBuff) {
            linha.getStyleClass().add("battle-log-buff-line");
        }
        if (isDerrotado) {
            linha.getStyleClass().add("battle-log-derrotado-line");
        }

        // Processa as palavras especiais
        atualizarPalavrasLog(linha, texto);

        // Se for turno, aplica a cor azul a cada Text
        if (isTurno) {
            for (Node node : linha.getChildren()) {
                if (node instanceof Text txt) {
                    txt.getStyleClass().add("battle-log-turn-word");
                }
            }
        }

        // Adiciona ao log
        Platform.runLater(() -> {
            list.add(linha);  // Atualiza a ListView na UI
            PauseTransition pause = new PauseTransition(Duration.millis(100)); // Ajuste o valor conforme necessário
            pause.setOnFinished(event -> scrollToEnd(logView));
            pause.play();
        });
    }

    // Atualiza e estiliza algumas palavras nos logs de batalha via css
    private void atualizarPalavrasLog(TextFlow linha, String texto) {
        linha.getChildren().clear();

        // Map para as palavras a estilizar
        Map<String, String> palavrasEstilizadas = new HashMap<>();
        palavrasEstilizadas.put("BUFF", "battle-log-buff-word");
        palavrasEstilizadas.put("VENCERAM", "battle-log-vencedor-word");
        palavrasEstilizadas.put("DANO", "battle-log-dano-word");
        palavrasEstilizadas.put("VIDA", "battle-log-vida-word");
        palavrasEstilizadas.put("BÊNÇÃO", "battle-log-ganhou-word"); // Acentuado
        palavrasEstilizadas.put("DERROTADO", "battle-log-derrotado-word");
        palavrasEstilizadas.put("DEFESA", "battle-log-defesa-word");
        palavrasEstilizadas.put("PROTEÇÃO", "battle-log-ganhou-word");
        palavrasEstilizadas.put("MALDIÇÃO", "battle-log-dano-word"); // Normalizado sem espaço e acento
        palavrasEstilizadas.put("SOFREM", "battle-log-sofreu-word");
        palavrasEstilizadas.put("GANHAM", "battle-log-ganhou-word");
        palavrasEstilizadas.put("TERRAMOTO", "battle-log-dano-word"); // Exemplo de mais uma chave

        // Divida o texto em palavras, mas preserve os espaços e pontuações
        String[] palavras = texto.split("((?<= )|(?= )|(?<=\\.)|(?=\\.))");

        for (int i = 0; i < palavras.length; i++) {
            String palavra = palavras[i];
            Text txtPalavra = new Text(palavra);
            String palavraBaseUpper = palavra.replaceAll("[^\\p{L}\\d]", "").toUpperCase();

            // Verifique se a palavra é uma das palavras-chave para estilização
            if (palavrasEstilizadas.containsKey(palavraBaseUpper)) {
                txtPalavra.getStyleClass().add(palavrasEstilizadas.get(palavraBaseUpper));

                // Verifica se a próxima palavra é um número (com ou sem sinal)
                if (i < palavras.length - 1 && palavras[i + 1].matches("^[+-]?\\d+$")) {
                    // Caso a próxima palavra seja um número
                    Text txtNumero = new Text(palavras[i + 1]);
                    txtNumero.getStyleClass().add(palavrasEstilizadas.get(palavraBaseUpper)); // Usa o mesmo estilo da palavra base
                    linha.getChildren().addAll(txtPalavra, txtNumero);
                    i++; // Avançar para a próxima palavra (que é o número)
                    continue;
                }

                // Verifica se há um espaço entre a palavra e o número
                if (i < palavras.length - 2 && palavras[i + 1].matches("^\\s+$") && palavras[i + 2].matches("^[+-]?\\d+$")) {
                    // Caso haja um espaço entre a palavra e o número
                    Text txtEspaco = new Text(palavras[i + 1]);
                    Text txtNumero = new Text(palavras[i + 2]);
                    txtNumero.getStyleClass().add(palavrasEstilizadas.get(palavraBaseUpper)); // Usa o estilo da palavra base
                    linha.getChildren().addAll(txtPalavra, txtEspaco, txtNumero);
                    i += 2; // Avançar para a próxima palavra e número
                    continue;
                }
            }

            linha.getChildren().add(txtPalavra);
        }
    }

    // Diminui o texto ao voltar ao menu
    public void diminuirTextoListView() {
        // Define o estilo da fonte diretamente na ListView
        logView.setStyle("-fx-font-size: 10px;");
    }
}
