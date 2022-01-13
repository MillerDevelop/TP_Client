package com.example.chineseCheckers;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SceneContentHandler {
    protected static void SetBoxVisible(VBox box){
        box.setVisible(true);
        box.setManaged(true);
    }

    protected static void SetBoxVisible(Parent parent){
        parent.setVisible(true);
        parent.setManaged(true);
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

    protected static void SetBoxUnVisible(Parent parent){
        parent.setVisible(false);
        parent.setManaged(false);
    }

}
