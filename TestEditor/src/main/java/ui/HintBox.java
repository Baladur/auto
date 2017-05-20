package ui;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.Hint;
import org.fxmisc.richtext.CodeArea;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import util.Resources;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by roman on 18.05.2017.
 */
public class HintBox extends Popup {
    private CodeArea parent;
    private List<HBox> hints = new ArrayList<>();
    private ListView<HBox> hintList = new ListView();

    public HintBox(CodeArea codeArea) {
        parent = codeArea;
        getContent().add(new VBox(hintList));
        EventStreams.nonNullValuesOf(parent.caretBoundsProperty())
                .subscribe(boundsOptional -> {
                    if (boundsOptional.isPresent()) {
                        Bounds bounds = boundsOptional.get();
                        setX(bounds.getMaxX());
                        setY(bounds.getMaxY());
                    }

                });
        hintList.getStylesheets().add(Resources.MAIN_STYLESHEET);
    }

    public void setVisible() {
        show(parent, 0, 0);
    }

    public void setHints(Set<Hint> hintSet) {
        hints = hintSet.stream()
                .filter(hint ->
                hint.getLabel().length() > 1)
                .sorted((hint1, hint2) -> Math.abs(hint1.getPath().get(hint1.getPath().size()-1)) - Math.abs(hint2.getPath().get(hint2.getPath().size()-1)))
                .map(hint -> new HBox(new Label(hint.getLabel()), new Label(""))).collect(Collectors.toList());
        hintList.getItems().setAll(hints);
//        hintList.getItems().clear();
//        hintList.getItems().addAll(hints.stream().map(hint -> new HBox(new Label(hint.getLabel()), new Label(""))).collect(Collectors.toSet()));
//        ExecutorService executor = Executors.newSingleThreadExecutor();

    }
}
