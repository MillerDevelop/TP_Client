package client;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Class responsible for changing the content of the GUI window */

public class SceneContentHandler {

    /**
     * Sets the box visible and manageable with a VBox given.
     *
     * @param box the box passed from controller.
     */

    protected static void SetBoxVisible(VBox box){
        box.setVisible(true);
        box.setManaged(true);
    }

    /**
     * Sets box visible and manageable with AnchorPane given.
     *
     * @param pane the anchor pane passed from controller.
     */

    protected static void SetBoxVisible(AnchorPane pane) {
        pane.setVisible(true);
        pane.setManaged(true);
    }

    /**
     * Sets box invisible and unmanageable with AnchorPane given.
     *
     * @param pane the anchor pane passed from controller.
     */

    protected static void SetBoxUnVisible(AnchorPane pane) {
        pane.setVisible(false);
        pane.setManaged(false);
    }

    /**
     * Sets box invisible and unmanageable with VBox given.
     *
     * @param box the vertical box passed from controller.
     */

    protected static void SetBoxUnVisible(VBox box){
        box.setVisible(false);
        box.setManaged(false);
    }

    /**
     * Sets box invisible and unmanageable with Parent given.
     *
     * @param parent the parent of the element clicked passed by controller.
     */

    protected static void SetBoxUnVisible(Parent parent) {
        parent.setVisible(false);
        parent.setManaged(false);
    }

    /**
     * Sets label visible and manageable.
     *
     * @param label the label passed by controller.
     */

    protected static void SetLabelVisible(Label label) {
        label.setManaged(true);
        label.setVisible(true);
    }

    /**
     * Sets label invisible and unmanageable.
     *
     * @param label the label passed by controller.
     */

    protected static void SetLabelUnVisible(Label label) {
        label.setManaged(false);
        label.setVisible(false);
    }

}
