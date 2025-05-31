package main.application.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.application.model.Besta;
import main.application.model.Heroi;
import main.application.model.Personagem;
import main.application.model.enums.Tipo;
import main.application.utils.execptions.FormValidationException;

import java.lang.reflect.Field;
import java.util.Map;

import static main.application.utils.ValidationUtils.validarPersonagem;

/**
 * Class Util, para formulários,
 * faz a manipulação dos dados dos formulários,
 * como atualizar as ListViews,
 * limpar os campos dos formulários,
 * manipula ainda as ListViews, para mover as posições dos personagens
 */
public class FormUtils {


    // atualizar, estilizar ListViews e desabilita campos de entrada
    public static void atualizarListView(ListView<Personagem> personagens, Label labelPersonagem,
                                         ComboBox<Tipo> cbbTipo, TextField txtNome, TextField txtVida,
                                         TextField txtArmadura, TextField txtAtaque) {

        // Aplica o CSS uma única vez
        personagens.getStyleClass().add("main-listview");

        personagens.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Personagem personagem, boolean empty) {
                super.updateItem(personagem, empty);

                if (empty || personagem == null) {
                    setText(null);
                    setStyle(""); // Limpar estilo quando não houver item
                } else {
                    // Converter o tipo para a primeira letra maiúscula
                    String tipo = personagem.getTipo().toString();
                    String tipoComPrimeiraMaiuscula = tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();

                    setText(personagem.getNome() + ", " + tipoComPrimeiraMaiuscula +
                            " (Vida: " + personagem.getVida() +
                            ", Armadura: " + personagem.getArmadura() +
                            ", Ataque: " + personagem.getAtaque() + ")");

                    // Aplica o estilo via CSS
                    if (!getStyleClass().contains("custom-cell")) {
                        getStyleClass().add("custom-cell");
                    }
                }
            }
        });

        personagens.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Atualiza a Label com informações do personagem selecionado
                labelPersonagem.setText(newValue.getNome());
                labelPersonagem.setTextFill(Color.BLUE);
                labelPersonagem.setFont(Font.font("Comic Sans MS", 18)); // Considere mover isso para o CSS

                cbbTipo.setValue(newValue.getTipo());
                txtNome.setText(newValue.getNome());
                txtVida.setText(String.valueOf(newValue.getVida()));
                txtArmadura.setText(String.valueOf(newValue.getArmadura()));
                txtAtaque.setText(String.valueOf(newValue.getAtaque()));

                // desabilita os campos de edição
                if (personagens.getSelectionModel().getSelectedItem() instanceof Heroi || personagens.getSelectionModel().getSelectedItem() instanceof Besta) {
                    cbbTipo.setDisable(true);
                    txtNome.setDisable(true);
                    txtVida.setDisable(true);
                    txtArmadura.setDisable(true);
                    txtAtaque.setDisable(true);
                }
            }
        });
    }

    /**
     * Limpa os campos de um formulário dentro de um contêiner (Pane).
     *
     * @param container O contêiner que contém os campos a serem limpos.
     */
    public static void limparFormulario(Pane container) {
        for (Node node : container.getChildren()) {
            // Verifica o tipo de cada componente e limpa seu valor
            if (node instanceof TextInputControl) {
                ((TextInputControl) node).clear();  // Limpa campos de texto
            } else if (node instanceof ComboBox) {
                ((ComboBox<?>) node).getSelectionModel().clearSelection();  // Limpa seleção de ComboBox
            } else if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);  // Desmarca CheckBox
            } else if (node instanceof Pane) {
                limparFormulario((Pane) node);  // Limpeza recursiva se for um contêiner dentro de outro contêiner
            }
        }
    }

    public static <T> void moverItemParaCima(ListView<T> listView, ObservableList<T> lista) {
        System.out.println("mover cima");
        int indice = listView.getSelectionModel().getSelectedIndex();

        // Verifica se o índice selecionado é válido e se a lista não está vazia
        if (indice > 0 && !lista.isEmpty()) {
            T item = lista.remove(indice);  // Remove o item da posição atual
            lista.add(indice - 1, item);     // Insere o item na posição anterior

            // Seleciona o item na nova posição
            listView.getSelectionModel().select(indice - 1);

            // Ajusta o scroll para que o item movido fique visível
            listView.scrollTo(indice - 1);

            // Garante que o item selecionado seja destacado corretamente
            listView.requestFocus();  // Força a atualização do foco

            // Força a atualização da ListView para refletir as mudanças
            listView.refresh();  // Atualiza a interface visual da ListView
        } else {
            System.out.println("Índice inválido ou lista vazia.");
        }
    }

    public static <T> void moverItemParaBaixo(ListView<T> listView, ObservableList<T> lista) {
        System.out.println("mover baixo");
        int indice = listView.getSelectionModel().getSelectedIndex();
        // Verifica se o índice selecionado é válido e se a lista não está vazia
        if (indice >= 0 && indice < lista.size() - 1) {
            T item = lista.remove(indice); // Remove o item da posição atual

            // Seleciona o item na nova posição
            lista.add(indice + 1, item);

            // Seleciona o item na nova posição
            listView.getSelectionModel().select(indice + 1);

            // Ajusta o scroll para que o item movido fique visível
            listView.scrollTo(indice + 1);

            // Garante que o item selecionado seja destacado corretamente
            listView.requestFocus();  // Força a atualização do foco

            // Força a atualização da ListView para refletir as mudanças
            listView.refresh();  // Atualiza a interface visual da ListView
        } else {
            System.out.println("Índice inválido ou lista vazia.");
        }
    }
}

