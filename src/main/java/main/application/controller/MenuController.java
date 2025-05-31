package main.application.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import main.application.listener.AtualizarJsonListener;
import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.model.Personagem;
import main.application.model.enums.Tipo;
import main.application.repository.PersonagemJsonRepository;
import main.application.service.PersonagemManager;
import main.application.utils.FormUtils;
import main.application.utils.JsonUtils;
import main.application.utils.PopupUtils;
import main.application.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class controladora para o menu
 */
public class MenuController implements AtualizarJsonListener {
    @Getter
    @Setter
    @FXML
    private BorderPane root;
    @FXML
    public Label lblJogador1, lblJogador2;
    @FXML
    public Pane pnJogador1, pnJogador2;
    @FXML
    public ComboBox<Tipo> cbbTipoJogador1, cbbTipoJogador2;
    @FXML
    public TextField txtNomeJogador1, txtVidaJogador1, txtArmaduraJogador1, txtAtaqueJogador1;
    @FXML
    public TextField txtNomeJogador2, txtVidaJogador2, txtArmaduraJogador2, txtAtaqueJogador2;
    @FXML
    public StackPane stkBatalha;
    @FXML
    public Button btnBatalhar, btnCriarPersonagem1, btnCriarPersonagem2, btnEliminarPersonagem1,
            btnEliminarPersonagem2, btnEditarPersonagem2, btnEditarPersonagem1, btnGravarPersonagem1, btnGravarPersonagem2,
            btnCimaJogador1, btnBaixoJogador1, btnCimaJogador2, btnBaixoJogador2;

    @FXML
    public ListView<Personagem> listViewHerois, listViewBestas;

    // Listas observáveis personagens
    @FXML
    private ObservableList<Personagem> observableListHerois = FXCollections.observableArrayList();
    private ObservableList<Personagem> observableListBestas = FXCollections.observableArrayList();

    @FXML
    private final PersonagemManager manager = new PersonagemManager();
    private Personagem selecionado;
    private boolean editMode;
    private Node top, bottom, left, right;

    // Construtor sem parâmetros (adicionado para que o FXMLLoader funcione)
    public MenuController() {

    }

    // Methodo Initialize, que ja faz iniciar methods de configurações iniciais
    @FXML
    public void initialize() {
        configurarHandlers();
        configInitial();
        habilitarCampos();
    }

    // Handlers para os botões
    @FXML
    private void configurarHandlers() {

        // Para o botão que cria o Heroi
        btnCriarPersonagem1.setOnAction(actionEvent -> {
            // Cria uma nova instância de Heroi
            Personagem heroi = new Heroi();
            criarPersonagem(heroi, observableListHerois, listViewHerois);
        });

        // Para o botão que cria o Besta
        btnCriarPersonagem2.setOnAction(actionEvent -> {
            // Cria uma nova instância de Heroi
            Personagem besta = new Besta();
            criarPersonagem(besta, observableListBestas, listViewBestas);
        });

        // Handler de clique para a ListView de Heróis (Eventos de Seleção das ListViews: Configura a visibilidade dos botões de editar e criar com base na seleção das ListViews.)
        listViewHerois.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean itemSelecionado = newSelection != null;
            btnEditarPersonagem1.setVisible(itemSelecionado);
            btnCriarPersonagem1.setVisible(!itemSelecionado);
        });

        // Handler de clique para a ListView de Bestas (Eventos de Seleção das ListViews: Configura a visibilidade dos botões de editar e criar com base na seleção das ListViews.)
        listViewBestas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean itemSelecionado = newSelection != null;
            btnEditarPersonagem2.setVisible(itemSelecionado);
            btnCriarPersonagem2.setVisible(!itemSelecionado);
        });

        btnEliminarPersonagem1.setOnAction(actionEvent -> eliminarPersonagem(listViewHerois, observableListHerois));
        btnEliminarPersonagem2.setOnAction(actionEvent -> eliminarPersonagem(listViewBestas, observableListBestas));

        btnEditarPersonagem1.setOnAction(actionEvent -> editarPersonagem(0));  // Muda para editar
        btnEditarPersonagem2.setOnAction(actionEvent -> editarPersonagem(1));  // Muda para editar

        // Para o botão que gravar o Heroi
        btnGravarPersonagem1.setOnAction(actionEvent -> {
            // Cria uma nova instância de Heroi
            Personagem heroi = new Heroi();
            gravarPersonagem(heroi, observableListHerois, listViewHerois);
        });

        // Para o botão que gravar o Besta
        btnGravarPersonagem2.setOnAction(actionEvent -> {
            // Cria uma nova instância de Heroi
            Personagem besta = new Besta();
            gravarPersonagem(besta, observableListBestas, listViewBestas);
        });

        // para botões que alteram a posição (para Cima e para baixo) dos personagens da lista de heróis
        btnCimaJogador1.setOnAction(actionEvent -> FormUtils.moverItemParaCima(listViewHerois, observableListHerois));
        btnBaixoJogador1.setOnAction(actionEvent -> FormUtils.moverItemParaBaixo(listViewHerois, observableListHerois));

        // para botões que alteram a posição (para Cima e para baixo) dos personagens da lista de Bestas
        btnCimaJogador2.setOnAction(actionEvent -> FormUtils.moverItemParaCima(listViewBestas, observableListBestas));
        btnBaixoJogador2.setOnAction(actionEvent -> FormUtils.moverItemParaBaixo(listViewBestas, observableListBestas));

        //  Filtro para verificar o clique fora das ListViews
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            // Verifica se o clique foi fora das ListViews e de outros nós permitidos
            if (!ValidationUtils.isClickInsideAllowedNodes(root, event)) {

                // Desmarcar a seleção nas ListViews
                listViewHerois.getSelectionModel().clearSelection();
                listViewBestas.getSelectionModel().clearSelection();

                // cancelar as edições
                cancelarEdicao();
            }
        });

        // botão de iniciar batalha
        btnBatalhar.setOnAction(actionEvent -> batalha());
    }

    // configurações iniciais, como por exemplo: povioar comboBox, atualizar listeners e repõe Botões
    @FXML
    public void configInitial() {
        editMode = false;
        // Obter todos os valores do enum Tipo
        Tipo[] todosOsTipos = Tipo.values();

        //Filtrar apenas os tipos que são heróis
        List<Tipo> herois = Arrays.stream(todosOsTipos) // Cria uma stream a partir do array
                .filter(Tipo::isHeroi) // Filtra usando o metodo isHeroi() do enum
                .collect(Collectors.toList()); // Coleta os resultados numa nova lista

        //Definir os itens do ComboBox com a lista filtrada
        cbbTipoJogador1.setItems(FXCollections.observableArrayList(herois));

        // Limpa a seleção actual da comboBox
        cbbTipoJogador1.getSelectionModel().select(null);

        //Filtrar apenas os tipos que são bestas
        List<Tipo> bestas = Arrays.stream(todosOsTipos) // Cria uma stream a partir do array
                .filter(Tipo::isBesta) // Filtra usando o metodo isBesta() do enum
                .collect(Collectors.toList()); // Coleta os resultados numa nova lista

        //Definir os itens do ComboBox com a lista filtrada
        cbbTipoJogador2.setItems(FXCollections.observableArrayList(bestas));

        // Limpa a seleção actual da comboBox
        cbbTipoJogador2.getSelectionModel().select(null);

        // atualiza listeners dos Json
        atualizarJsonListener();

        // repor botões
        resetarButons();
    }

    // Habilita campos para registo de novos personagens
    @FXML
    public void habilitarCampos() {
        cbbTipoJogador1.setDisable(false);
        txtNomeJogador1.setDisable(false);
        txtVidaJogador1.setDisable(false);
        txtArmaduraJogador1.setDisable(false);
        txtAtaqueJogador1.setDisable(false);

        cbbTipoJogador2.setDisable(false);
        txtNomeJogador2.setDisable(false);
        txtVidaJogador2.setDisable(false);
        txtArmaduraJogador2.setDisable(false);
        txtAtaqueJogador2.setDisable(false);
    }

    // Atualiza os Listeners dos Json
    @Override
    public void atualizarJsonListener() {
        JsonUtils.setListener(this);
        carregarListas();
    }

    // Carrega as listas de personagens
    @FXML
    public void carregarListas() {
        // Carrega listas através dos Jsons
        observableListHerois = FXCollections.observableArrayList(PersonagemJsonRepository.carregarHerois());
        observableListBestas = FXCollections.observableArrayList(PersonagemJsonRepository.carregarBestas());

        // Carrega as listviews
        listViewHerois.setItems(observableListHerois);
        listViewBestas.setItems(observableListBestas);

        // Atualizar as listas com os dados mais recentes
        FormUtils.atualizarListView(listViewHerois, lblJogador1, cbbTipoJogador1, txtNomeJogador1, txtVidaJogador1, txtArmaduraJogador1, txtAtaqueJogador1);
        FormUtils.atualizarListView(listViewBestas, lblJogador2, cbbTipoJogador2, txtNomeJogador2, txtVidaJogador2, txtArmaduraJogador2, txtAtaqueJogador2);

        // Listeners para Heróis selecionados
        listViewHerois.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) lblJogador1.setText(novo.getNome());
            else lblJogador1.setText(" ");
        });

        // Listeners para Bestas selecionadas
        listViewBestas.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) lblJogador2.setText(novo.getNome());
            else lblJogador2.setText(" ");
        });
    }

    // Repõe os botões de origem
    @FXML
    public void resetarButons() {
        // Resetar a seleção de personagem
        selecionado = null;

        // Exibir botões de criar personagem
        btnCriarPersonagem1.setVisible(true);
        btnCriarPersonagem2.setVisible(true);

        // Ocultar botões de editar, eliminar e gravar personagem
        btnEditarPersonagem1.setVisible(false);
        btnEditarPersonagem2.setVisible(false);
        btnEliminarPersonagem1.setVisible(false);
        btnEliminarPersonagem2.setVisible(false);
        btnGravarPersonagem1.setVisible(false);
        btnGravarPersonagem2.setVisible(false);

        // Resetar o texto do botão de editar
        btnEditarPersonagem1.setText("Editar");
        btnEditarPersonagem2.setText("Editar");
    }

    // Cancelar edições, evita editar um personagem indesejado
    @FXML
    private void cancelarEdicao() {
        System.out.println("Cancelando edição...");
        selecionado = null; // Remove a seleção do personagem selecionado da list view
        editMode = false; // Anula o modo de edição

        // Limpa os formulários
        FormUtils.limparFormulario(pnJogador1);
        FormUtils.limparFormulario(pnJogador2);

        resetarButons(); // Chama a função para restaurar o estado inicial dos botões

        habilitarCampos(); // habilita campos para novos registos de personagens
    }

    @FXML
    // Metodo para criar Personagem
    private void criarPersonagem(Personagem personagem, List<Personagem> lista, ListView<Personagem> listView) {
        if (personagem == null) {
            PopupUtils.showErrorPopup("Erro", "Personagem não pode ser nulo.", getRoot());
            return;
        }

        try {

            // Preenche a entidade de acordo com o tipo de personagem (Heroi ou Besta)
            if (personagem instanceof Heroi) {
                Tipo tipo = cbbTipoJogador1.getValue();
                String nome = txtNomeJogador1.getText();
                Integer vida = Integer.valueOf(txtVidaJogador1.getText());
                Integer armadura = Integer.valueOf(txtArmaduraJogador1.getText());
                Integer ataque = Integer.valueOf(txtAtaqueJogador1.getText());

                // Modifica o personagem existente
                personagem.setTipo(tipo);
                personagem.setNome(nome);
                personagem.setVida(vida);
                personagem.setArmadura(armadura);
                personagem.setAtaque(ataque);

                // Adiciona personagem no Json
                manager.adicionarHeroi((Heroi) personagem);

            } else if (personagem instanceof Besta) {
                Tipo tipo = cbbTipoJogador2.getValue();
                String nome = txtNomeJogador2.getText();
                Integer vida = Integer.valueOf(txtVidaJogador2.getText());
                Integer armadura = Integer.valueOf(txtArmaduraJogador2.getText());
                Integer ataque = Integer.valueOf(txtAtaqueJogador2.getText());

                // Modifica o personagem existente
                personagem.setTipo(tipo);
                personagem.setNome(nome);
                personagem.setVida(vida);
                personagem.setArmadura(armadura);
                personagem.setAtaque(ataque);

                // Adiciona personagem no Json
                manager.adicionarBesta((Besta) personagem);
            }

            // Verifique se o personagem foi preenchido corretamente
            if (!ValidationUtils.validarPersonagem(personagem)) {
                PopupUtils.showErrorPopup("Erro", "Campos do Personagem não preenchidos corretamente.", getRoot());
                return;
            }

            // Adiciona a personagem na lista
            lista.add(personagem);

            // Atualiza a ListView
            listView.refresh();

            // Exibe mensagem de sucesso
            PopupUtils.showSuccessPopup("Sucesso", "Personagem " + personagem.getNome() + " adicionado com sucesso!", getRoot());

        } catch (NumberFormatException e) {
            PopupUtils.showErrorPopup("Error", "Vida, Armadura e ataque tem que ser inteiros positivos", getRoot());
        }
    }

    @FXML
    // Method para eliminar personagens
    private void eliminarPersonagem(ListView<Personagem> listView, List<Personagem> lista) {
        System.out.println("Botão Eliminar clicado");
        try {
            // personagem selecionado na list View
            selecionado = listView.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                PopupUtils.showErrorPopup("Erro", "Nenhum personagem selecionado!", getRoot());
                return;
            }

            boolean confirmar = PopupUtils.showConfirmationPopup(
                    "Confirmação",
                    "Eliminar Personagem " + selecionado.getNome(),
                    "Tem certeza que deseja eliminar este personagem?",
                    getRoot()
            );
            if (!confirmar) return;

            if (selecionado instanceof Heroi) {
                // usa o manager para remover o Herói
                manager.removerHeroi(selecionado.getId());
            } else if (selecionado instanceof Besta) {
                // usa o manager para atualizar o Besta
                manager.removerBesta(selecionado.getId());
            }

            // Atualiza a lista e Id do personagem
            lista.removeIf(p -> p.getId().equals(selecionado.getId()));

            // Remove pelo id do personagem que foi selecionado
            listView.getItems().removeIf(p -> p.getId().equals(selecionado.getId()));

            // atualiza as list view
            listView.refresh();

            // Mensagem de sucesso
            PopupUtils.showSuccessPopup("Sucesso", "Personagem removido com sucesso!", getRoot());

            cancelarEdicao();

        } catch (Exception e) {
            e.printStackTrace();
            PopupUtils.showErrorPopup("Erro", "Exceção ao remover: " + e.getMessage(), getRoot());
        }
    }

    // Method para Editar personagens
    @FXML
    private void editarPersonagem(int index) {

        // Usa ArrayList para armazenar as ListViews de personagens (heróis e bestas).
        ArrayList<ListView<Personagem>> listViews = new ArrayList<>();
        listViews.add(listViewHerois);
        listViews.add(listViewBestas);

        // Lógica que será executada ao clicar no botão de editar
        System.out.println("Botão Editar Personagem " + (index + 1) + " Clicado");

        // Verifica se está em modo de edição, caso sim, cancela a edição e retorna aos valores originais
        if (editMode) {
            cancelarEdicao(); // Cancela a edição
        } else {
            // Se não está em modo de edição, tenta selecionar um personagem da ListView correspondente
            selecionado = listViews.get(index).getSelectionModel().getSelectedItem();

            // Se um personagem foi selecionado, entra no modo de edição
            if (selecionado != null) {
                editMode = true; // Ativa o modo de edição

                habilitarCampos(); // habilita os campos para registar novos personagens

                // Atualiza apenas os botões correspondentes ao index
                if (index == 0) {
                    btnEditarPersonagem1.setText("Voltar");

                    btnGravarPersonagem1.setVisible(true);
                    btnCriarPersonagem1.setVisible(false);
                    btnEliminarPersonagem1.setVisible(true);

                    // Desativa apenas os botões do outro grupo
                    btnGravarPersonagem2.setVisible(false);
                    btnCriarPersonagem2.setVisible(false);
                    btnEliminarPersonagem2.setVisible(false);


                } else {
                    btnEditarPersonagem2.setText("Voltar");

                    btnGravarPersonagem2.setVisible(true);
                    btnCriarPersonagem2.setVisible(false);
                    btnEliminarPersonagem2.setVisible(true);

                    // Desativa apenas os botões do outro grupo
                    btnGravarPersonagem1.setVisible(false);
                    btnCriarPersonagem1.setVisible(false);
                    btnEliminarPersonagem1.setVisible(false);

                }
            }
        }
    }

    // Method para gravar/atualizar personagens
    @FXML
    public void gravarPersonagem(Personagem personagem, List<Personagem> lista, ListView<Personagem> listView) {
        try {
            // personagem selecionado na list View
            personagem = listView.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                PopupUtils.showErrorPopup("Erro", "Nenhum personagem selecionado!", getRoot());
                return;
            }
            Personagem personagemAtualizado = selecionado;

            if (selecionado instanceof Heroi) {

                personagemAtualizado = new Heroi();
                Tipo tipo = cbbTipoJogador1.getValue();
                String nome = txtNomeJogador1.getText();
                Integer vida = Integer.valueOf(txtVidaJogador1.getText());
                Integer armadura = Integer.valueOf(txtArmaduraJogador1.getText());
                Integer ataque = Integer.valueOf(txtAtaqueJogador1.getText());

                // Modifica o personagem existente
                selecionado.setTipo(tipo);
                selecionado.setNome(nome);
                selecionado.setVida(vida);
                selecionado.setArmadura(armadura);
                selecionado.setAtaque(ataque);

                // usa o manager para atualizar o Herói
                manager.atualizarHeroi((Heroi) selecionado);

            } else if (selecionado instanceof Besta) {

                personagemAtualizado = new Besta();
                Tipo tipo = cbbTipoJogador2.getValue();
                String nome = txtNomeJogador2.getText();
                Integer vida = Integer.valueOf(txtVidaJogador2.getText());
                Integer armadura = Integer.valueOf(txtArmaduraJogador2.getText());
                Integer ataque = Integer.valueOf(txtAtaqueJogador2.getText());

                // Modifica o personagem existente
                selecionado.setTipo(tipo);
                selecionado.setNome(nome);
                selecionado.setVida(vida);
                selecionado.setArmadura(armadura);
                selecionado.setAtaque(ataque);

                // usa o manager para atualizar o Herói
                manager.atualizarBesta((Besta) selecionado);
            }

            // Verifique se o personagem foi preenchido corretamente
            if (!ValidationUtils.validarPersonagem(selecionado)) {
                PopupUtils.showErrorPopup("Erro", "Campos do Personagem não preenchidos corretamente.", getRoot());
                return;
            }

            // Atualizar a ListView
            listView.refresh();

            // Salvar os dados
            manager.salvarTodos();

            // Mensagem de sucesso
            PopupUtils.showSuccessPopup("Sucesso", "Personagem atualizado com sucesso!", getRoot());

            // Sair do modo edição
            cancelarEdicao();

        } catch (Exception e) {
            e.printStackTrace();
            PopupUtils.showErrorPopup("Erro", "Exceção ao atualizar: " + e.getMessage(), getRoot());
        }
    }

    // Method que inicia a batalha
    @FXML
    public void batalha() {
        try {
            // Verificação mais robusta das listas
            if (listViewHerois.getItems() == null || listViewBestas.getItems() == null ||
                    listViewHerois.getItems().isEmpty() || listViewBestas.getItems().isEmpty()) {

                PopupUtils.showErrorPopup("Seleção Incompleta",
                        "Por favor, carregue e selecione pelo menos um herói e uma besta válidos.", root);
                return;
            }

            // Clone dos personagens
            List<Personagem> heroisCopia = new ArrayList<>();
            List<Personagem> bestasCopia = new ArrayList<>();

            for (Personagem heroi : listViewHerois.getItems()) {
                heroisCopia.add(clonarPersonagem(heroi));
            }

            for (Personagem besta : listViewBestas.getItems()) {
                bestasCopia.add(clonarPersonagem(besta));
            }

            // Criação do controlador para a batalha
            BatalhaController batalhaController = new BatalhaController(() -> {
                Platform.runLater(() -> {
                    System.out.println("Batalha finalizada - Restaurando UI");
                    restaurarElementosUI();

                    // Atualiza as ListViews para refletir possíveis mudanças
                    listViewHerois.refresh();
                    listViewBestas.refresh();
                });
            }, heroisCopia, bestasCopia);

            // Configuração da UI
            root.setCenter(batalhaController.getRoot());
            batalhaController.iniciarBatalha();
            esconderElementosUI();

        } catch (Exception e) {
            handleInitializationError(e);
            PopupUtils.showErrorPopup("Erro na Batalha",
                    "Ocorreu um erro ao iniciar a batalha: " + e.getMessage(), root);
        }
    }

    // Method auxiliar para clonar personagens
    private Personagem clonarPersonagem(Personagem original) {
        if (original instanceof Heroi heroi) {
            return new Heroi(heroi.getId(), heroi.getTipo(), heroi.getNome(),
                    heroi.getVida(), heroi.getArmadura(), heroi.getAtaque());
        } else if (original instanceof Besta besta) {
            return new Besta(besta.getId(), besta.getTipo(), besta.getNome(),
                    besta.getVida(), besta.getArmadura(), besta.getAtaque());
        }
        throw new IllegalArgumentException("Tipo de personagem desconhecido");
    }

    // Armazena e esconde os elementos do menu, para a batalha iniciar dinamicamente
    private void esconderElementosUI() {
        // Armazenar os elementos antes de removê-los
        top = root.getTop();
        bottom = root.getBottom();
        left = root.getLeft();
        right = root.getRight();

        // Remover os elementos do BorderPane
        root.setTop(null);
        root.setBottom(null);
        root.setLeft(null);
        root.setRight(null);
    }

    // Restaura UI depois da Batalha
    private void restaurarElementosUI() {
        // Restaurar os elementos para o BorderPane
        root.setTop(top);
        root.setBottom(bottom);
        root.setLeft(left);
        root.setRight(right);
    }

    // Trata erros durante a inicialização da batalha e exibe uma mensagem de erro
    private void handleInitializationError(Exception e) {
        System.err.println("Erro na inicialização da batalha:");
        e.printStackTrace();

        PopupUtils.showErrorPopup("Erro de Inicialização",
                "Não foi possível preparar a batalha. Verifique os dados e tente novamente.", root);
    }
}
