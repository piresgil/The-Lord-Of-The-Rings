package main.application.utils;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Classe utilitária para exibição de alertas na interface gráfica (JavaFX).
 * Essa classe contém métodos estáticos para mostrar mensagens de erro,
 * sucesso e confirmação ao utilizador.
 */
public class PopupUtils {

    /**
     * Exibe um popup de erro com um título e uma mensagem.
     *
     * @param title   O título da janela do popup.
     * @param message A mensagem de erro a ser exibida.
     * @param owner   O Stage principal da aplicação (janela principal)
     */
    public static void showErrorPopup(String title, String message, Node owner) {
        // Criar um novo Stage para o popup
        Stage popupStage = new Stage();
        popupStage.initOwner(owner.getScene().getWindow());  // Definir a janela principal como dono do popup
        popupStage.initModality(Modality.APPLICATION_MODAL);  // Impedir interação com a janela de fundo
        popupStage.initStyle(StageStyle.TRANSPARENT);

        // Layout do popup
        VBox vbox = new VBox();
        // aplica css
        vbox.getStyleClass().add("popup-vbox");

        // Adicionando cabeçalho e mensagem ao popup
        javafx.scene.control.Label headerLabel = new javafx.scene.control.Label(title);
        javafx.scene.control.Label messageLabel = new javafx.scene.control.Label(message);
        // Estilizando o header e a mensagem com o estilo CSS
        headerLabel.getStyleClass().add("popup-header");
        messageLabel.getStyleClass().add("popup-message");

        // Botão de OK para fechar o popup
        Button btnOk = new Button("OK");
        btnOk.setAlignment(Pos.CENTER_RIGHT);
        btnOk.setOnAction(event -> popupStage.close());  // Fechar o popup ao clicar no botão
        // Estilizando os botões
        btnOk.getStyleClass().add("popup-button-ok");

        // Criando o HBox para os botões, alinhando-os à direita
        HBox hbox = new HBox(10);  // Espaçamento de 10 entre os botões
        hbox.setAlignment(Pos.CENTER_RIGHT);  // Alinha os botões à direita

        // Adicionando os botões no HBox
        hbox.getChildren().addAll(btnOk);

        // Adicionando os elementos ao layout
        vbox.getChildren().addAll(headerLabel, messageLabel, hbox);

        // Configura a cena
        Scene scene = new Scene(vbox, 350, 100);
        scene.getStylesheets().add(PopupUtils.class.getResource("/styles/styles.css").toExternalForm());  // Aplica o CSS ao popup
        popupStage.setScene(scene);

        // Exibir o popup
        popupStage.showAndWait();
    }


    /**
     * Exibe um popup de sucesso/informação com um título e uma mensagem.
     * Aparece apenas durante alguns segundos com a informação passada
     *
     * @param title   O título da janela do popup.
     * @param message A mensagem informativa a ser exibida.
     * @param owner node onde a mensagem vai aparecer.
     */
    public static void showSuccessPopup(String title, String message, Node owner) {

        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Criar um novo Stage para o popup
        Stage popupStage = new Stage();
        popupStage.initOwner(owner.getScene().getWindow());  // Definir a janela principal como dono do popup
        // popupStage.initModality(Modality.APPLICATION_MODAL);  // Impedir interação com a janela de fundo
        popupStage.initStyle(StageStyle.TRANSPARENT);

        // Layout do popup
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getStyleClass().add("popup-vbox");

        // Adicionando cabeçalho e mensagem ao popup
        javafx.scene.control.Label headerLabel = new javafx.scene.control.Label(title);
        javafx.scene.control.Label messageLabel = new javafx.scene.control.Label(message);
        headerLabel.getStyleClass().add("popup-header");
        messageLabel.getStyleClass().add("popup-message");

        // Adicionando os elementos ao layout
        vbox.getChildren().addAll(headerLabel, messageLabel);

        // Carrega o arquivo de estilos CSS
        Scene scene = new Scene(vbox, 350, 100);
        scene.getStylesheets().add(PopupUtils.class.getResource("/styles/styles.css").toExternalForm());  // Aplica o CSS ao popup

        popupStage.setScene(scene);

        // Fade in: Aplica um efeito de transição de opacidade de 0 a 1
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), popupStage.getScene().getRoot());
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Fade out automático após 1.5s
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), popupStage.getScene().getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(3));

        // Quando o fadeOut terminar, fechamos a janela
        fadeOut.setOnFinished(e -> popupStage.close());

        // Exibir o popup com fade in
        popupStage.show();
        fadeIn.play();

        // Iniciar o fade out após um tempo
        fadeOut.play();
    }

    /**
     * Exibe um popup de sucesso/informação com um título e uma mensagem.
     * Com 2 buttons(ok, cancelar)
     *
     * @param title   O título da janela do popup.
     * @param message A mensagem informativa a ser exibida.
     * @param owner node onde a mensagem vai aparecer.
     */
    public static boolean showConfirmationPopup(String title, String header, String message, Node owner) {
        // Criar uma nova janela de popup (Stage)
        Stage popupStage = new Stage();
        popupStage.initOwner(owner.getScene().getWindow());  // Setando o owner corretamente para a janela principal
        popupStage.initModality(Modality.APPLICATION_MODAL);  // Impede a interação com a janela de fundo
        popupStage.initStyle(StageStyle.TRANSPARENT);

        // Layout do popup
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        // aplica css
        vbox.getStyleClass().add("popup-vbox");

        // Adicionando cabeçalho e mensagem ao popup
        javafx.scene.control.Label headerLabel = new javafx.scene.control.Label(header);
        javafx.scene.control.Label messageLabel = new javafx.scene.control.Label(message);

        // Estilizando o header e a mensagem com o estilo CSS
        headerLabel.getStyleClass().add("popup-header");
        messageLabel.getStyleClass().add("popup-message");

        // Botões para ação do usuário
        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");

        // Criando o HBox para os botões, alinhando-os à direita
        HBox hbox = new HBox(10);  // Espaçamento de 10 entre os botões
        hbox.setAlignment(Pos.CENTER_RIGHT);  // Alinha os botões à direita

        // Adicionando os botões no HBox
        hbox.getChildren().addAll(btnOk, btnCancel);

        // Estilizando os botões
        btnOk.getStyleClass().add("popup-button-ok");
        btnCancel.getStyleClass().add("popup-button-cancel");

        // Variável para armazenar a escolha do usuário
        final boolean[] result = {false}; // Falso por padrão

        // Ação do botão OK
        btnOk.setOnAction(event -> {
            result[0] = true;  // Usuário confirmou
            popupStage.close();  // Fecha o popup
        });

        // Ação do botão Cancelar
        btnCancel.setOnAction(event -> {
            result[0] = false;  // Usuário cancelou
            popupStage.close();  // Fecha o popup
        });

        // Adiciona os botões e labels ao layout
        vbox.getChildren().addAll(headerLabel, messageLabel, hbox);

        // Carrega o arquivo de estilos CSS
        Scene scene = new Scene(vbox, 350, 100);
        scene.getStylesheets().add(PopupUtils.class.getResource("/styles/styles.css").toExternalForm());  // Aplica o CSS ao popup

        popupStage.setScene(scene);

        // Exibir o popup
        popupStage.showAndWait();

        // Retorna true ou false dependendo do que o usuário fez
        return result[0];  // Retorna a escolha do usuário
    }

}
