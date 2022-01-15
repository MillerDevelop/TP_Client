package client;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SceneContentHandler {
    protected static void SetBoxVisible(VBox box){
        box.setVisible(true);
        box.setManaged(true);
    }

    protected static void SetBoxVisible(AnchorPane pane) {
        pane.setVisible(true);
        pane.setManaged(true);
    }

    protected static void SetBoxUnVisible(AnchorPane pane) {
        pane.setVisible(false);
        pane.setManaged(false);
    }

    protected static void SetBoxUnVisible(VBox box){
        box.setVisible(false);
        box.setManaged(false);
    }

    protected static void SetBoxUnVisible(Parent parent) {
        parent.setVisible(false);
        parent.setManaged(false);
    }

    protected static void SetLabelVisible(Label label) {
        label.setManaged(true);
        label.setVisible(true);
    }

    protected static void SetLabelUnVisible(Label label) {
        label.setManaged(false);
        label.setVisible(false);
    }

}
