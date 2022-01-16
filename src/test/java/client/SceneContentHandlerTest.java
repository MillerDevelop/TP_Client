package client;


import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SceneContentHandlerTest {
    private VBox box1 = new VBox();
    private VBox box2 = new VBox();
    private AnchorPane pane1 = new AnchorPane();
    private AnchorPane pane2 = new AnchorPane();


    @BeforeEach
    void SetUp() {
        box1.setManaged(false);
        box1.setVisible(false);
        box2.setVisible(true);
        box2.setManaged(true);
        pane1.setVisible(false);
        pane1.setManaged(false);
        pane2.setManaged(true);
        pane2.setVisible(true);
    }

    @Test
    void testSetBoxVisible() {
        SceneContentHandler.SetBoxVisible(box1);

        assertEquals(true, box1.isVisible());
        assertEquals(true, box1.isManaged());
    }

    @Test
    void testSetBoxVisibleAnchorPane() {
        SceneContentHandler.SetBoxVisible(pane1);

        assertTrue(pane1.isVisible());
        assertTrue(pane1.isManaged());
    }

    @Test
    void testSetBoxUnVisible() {
        SceneContentHandler.SetBoxUnVisible(box2);

        assertFalse(box2.isVisible());
        assertFalse(box2.isManaged());
    }

    @Test
    void testSetBoxUnVisibleAnchor() {
        SceneContentHandler.SetBoxUnVisible(pane2);

        assertFalse(pane2.isManaged());
        assertFalse(pane2.isVisible());
    }

}