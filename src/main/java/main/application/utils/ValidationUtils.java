package main.application.utils;


import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import main.application.model.Personagem;

/**
 * Class de Util de Validações,
 * para gerir a interação do utilizador,
 * protegendo certos elementos da UI de cliques indesejados que poderiam
 */
public class ValidationUtils {

    // Vaklidar Personagen
    public static boolean validarPersonagem(Personagem personagem) {
        // Verifica se o personagem não é nulo antes de realizar a validação
        if (personagem != null) {
            return personagem.getTipo() != null          // Tipo não pode ser nulo
                    && personagem.getNome() != null      // Nome não pode ser nulo
                    && !personagem.getNome().trim().isEmpty() // Nome não pode ser vazio
                    && personagem.getVida() > 0          // Vida deve ser maior que 0
                    && personagem.getArmadura() >= 0     // Armadura não pode ser negativa
                    && personagem.getAtaque() >= 0;      // Ataque não pode ser negativo
        }
        return false;  // Retorna false se o personagem for nulo*/
    }

    // Verifica se o clique ocorreu em um node protegido ou em seus filhos
    public static boolean isClickInsideAllowedNodes(Parent parent, MouseEvent event) {
        Node target = (Node) event.getTarget();
        return isAllowedNode(target);
    }

    // Verifica se o node ou qualquer dos seus pais é um node protegido
    private static boolean isAllowedNode(Node node) {
        while (node != null) {
            if (isProtectedNodeType(node)) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }

    // Verifica tipos específicos de nodes que devem proteger ao clique
    private static boolean isProtectedNodeType(Node node) {
        return node instanceof ListView ||
                node instanceof TextInputControl ||
                node instanceof ComboBox ||
                node instanceof Labeled; // Adicionado para capturar labels dentro de botões
    }

     // Method para verificar as coordenadas do clique foi em areas protegidas
    public static boolean cliqueNasAreasProtegidas(Parent parent, MouseEvent event) {
        // Converte as coordenadas uma única vez
        Point2D sceneCoords = new Point2D(event.getSceneX(), event.getSceneY());

        // Verifica todos os filhos protegidos
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (isProtectedNodeType(child)) {
                Bounds bounds = child.localToScene(child.getBoundsInLocal());
                if (bounds.contains(sceneCoords)) {
                    return true;
                }
            }
            // Verificação recursiva para containers
            if (child instanceof Parent) {
                if (cliqueNasAreasProtegidas((Parent) child, event)) {
                    return true;
                }
            }
        }
        return false;
    }
}
