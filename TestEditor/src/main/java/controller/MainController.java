package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.Hint;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import ui.HintBox;
import util.DataException;
import util.Messages;
import util.Resources;
import util.SyntaxAnalyzeException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class MainController implements Initializable {
    @Setter private Stage stage;
    private EditorManager editorManager;
    private Map<String, HintBox> hintBoxMap = new HashMap<>();

    @FXML private MenuBar menuBar;
    @FXML private TabPane tabPane;
    @FXML private Label currentProjectLabel;
    @FXML private Label messageLabel;
    //private HintBox hintBox = new HintBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert menuBar != null;
        //hintBox = new HintBox();
        try {
            editorManager = new EditorManager();
            editorManager.setProjectChangedListener(project -> {
                currentProjectLabel.setText(project.getName());
                clearMessage();
            });
            tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if (oldValue != null) {
                            hintBoxMap.get(oldValue.getText()).hide();
                        }
                        if (newValue != null) {
                            editorManager.switchTest(newValue.getText());
                            hintBoxMap.get(newValue.getText()).show(stage);
                        }

                    }
                    //hintBox.setHints(editorManager.getCurrentHints());
            });
        } catch (DataException e) {
            printError(e.getMessage(), e);
        }

    }

    @FXML
    private void openProjectCreationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.PROJECT_CREATION_WINDOW));
            Parent root = loader.load();
            ProjectController projectController = loader.getController();
            projectController.setListener(project -> {
                try {
                    editorManager.initProject(project);
                    currentProjectLabel.setText(project.getName());
                    printMessage(String.format(Messages.PROJECT_CREATION_SUCCESS, project.getName()));
                } catch (DataException e) {
                    printError(e.getMessage(), e);
                }

            });
            Stage projectStage = new Stage();
            projectStage.initModality(Modality.APPLICATION_MODAL);
            projectStage.initStyle(StageStyle.UNIFIED);
            projectStage.setScene(new Scene(root));
            projectStage.setTitle("Create project");
            projectController.setStage(projectStage);
            projectStage.show();
        } catch (IOException e) {
            printError(Messages.INTERNAL_ERROR, e);
        }
    }

    @FXML
    private void openTestCreationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.TEST_CREATION_WINDOW));
            Parent root = loader.load();
            TestController testController = loader.getController();
            testController.setListener(testName -> {
                testName += ".test";
                try {
                    editorManager.openNewTest(testName);
                } catch (DataException e) {
                    printError(e.getMessage(), e);
                }
                CodeArea codeArea = new CodeArea();
                editorManager.addInputEventListener(character -> {
                    codeArea.appendText(character.toString());
                });
                editorManager.addContextEndEventListener(() -> {
                    codeArea.appendText("\n");
                    editorManager.setPosition(codeArea.getCurrentParagraph(), codeArea.getCaretColumn());
                });
                editorManager.addMovePositionEventListener(position -> {
                    codeArea.requestFocus();
                    codeArea.moveTo(position.line - 1, position.column - 1);
                });
                HintBox hintBox = new HintBox(codeArea);
                hintBoxMap.put(testName, hintBox);
                codeArea.setOnKeyTyped(event -> {
                    if (event.isControlDown() || event.isAltDown()) {
                        return;
                    }
                    //log.info("Typed: {}.", event.getCharacter());
                    editorManager.appendInput(event.getCharacter());
                    String input = editorManager.getInput();
                    //log.info("Input: {}.", input);
                    Set<Hint> hintsAfterInput = editorManager.getCurrentHints()
                            .stream().filter(hint -> hint.getLabel().startsWith(input)).collect(Collectors.toSet());
                    //hintsAfterInput.stream().forEach(hint -> log.info("Hint after input: {}.", hint.getLabel()));
                    switch (hintsAfterInput.size()) {
                        case 0:
                            //log.info("No hints match.");
                            codeArea.deletePreviousChar();
                            editorManager.deleteLastChar();
                            hintBox.setHints(editorManager.getCurrentHints());
                            break;
                        case 1:
                            try {
                                //log.info("1 hint matches. Analyzing hint: {}.", hintsAfterInput.iterator().next().getLabel());
                                IntStream.range(0, input.length()).forEach(i -> codeArea.deletePreviousChar());
                                editorManager.analyzeContext(hintsAfterInput.iterator().next());
                                hintBox.setHints(editorManager.getCurrentHints());
                            } catch (SyntaxAnalyzeException e) {
                                printError(e.getMessage(), e);
                            }
                            break;
                        default:
                            //log.info("Many hints match. Set hints to hintbox.");
                            hintBox.setHints(hintsAfterInput);
                    }
                    //editorManager.getCurrentHints().forEach(hint -> log.info("Hint: {}.", hint.getLabel()));

                });
                hintBox.setHints(editorManager.getCurrentHints());
                codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
                VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
                codeArea.requestFocus();
                codeArea.requestFollowCaret();

                Tab tab = new Tab();
                tab.setText(testName);
                tab.setContent(scrollPane);
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().selectLast();
            });
            Scene scene = new Scene(root, 500, 300);
            Stage stage = new Stage();
            stage.setTitle("Create test");
            stage.setScene(scene);
            testController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            printError(Messages.INTERNAL_ERROR, e);
        }
    }

    @FXML
    private void openFile() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("project file", ".json");
        fc.setSelectedExtensionFilter(filter);
        File chosenFile = fc.showOpenDialog(stage);
        if (chosenFile == null) {
            //no file was chosen
            return;
        }
        try {
            editorManager.openProject(chosenFile.toPath());
        } catch (DataException e) {
            printError(e.getMessage(), e);
        }
    }

    private void printMessage(String message) {
        log.info(message);
        messageLabel.setTextFill(Paint.valueOf("black"));
        messageLabel.setText(message);
    }

    private void clearMessage() {
        messageLabel.setText("");
    }

    private void printError(String errorMessage, Exception e) {
        log.error(errorMessage, e);
        messageLabel.setTextFill(Paint.valueOf("red"));
        messageLabel.setText(errorMessage);
    }
}
