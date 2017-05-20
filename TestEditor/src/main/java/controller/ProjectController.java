package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Setter;
import model.Project;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * Created by roman on 05.05.2017.
 */
public class ProjectController implements Initializable {
    @Setter private ProjectCreationListener listener;
    private Path rootPath;
    private Path srcPath;
    @Setter private Stage stage;

    @FXML private TextField projectNameTextField;
    @FXML private Label projectNameErrorLabel;

    @FXML private TextField packageNameTextField;
    @FXML private Label packageNameErrorLabel;

    @FXML private TextField rootPathTextField;
    @FXML private Label rootPathErrorLabel;

    @FXML private TextField srcPathTextField;
    @FXML private Label srcPathErrorLabel;
    @FXML private Button sourcePathButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sourcePathButton.setDisable(true);
    }

    @FXML
    private void chooseRootPath() {
        DirectoryChooser dc = new DirectoryChooser();
        File chosenFile = dc.showDialog(stage);
        if (!chosenFile.toPath().equals(rootPath)) {
            //if user changes root path, clear src path as it depends on root
            srcPath = null;
            srcPathTextField.setText("");
        }
        rootPath = chosenFile.toPath();
        rootPathTextField.setText(rootPath.toString());
        rootPathErrorLabel.setText("");
        sourcePathButton.setDisable(false);
    }

    @FXML
    private void chooseSourcePath() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(rootPath.toFile());
        File chosenFile = dc.showDialog(stage);
        if (!chosenFile.getAbsolutePath().startsWith(rootPath.toString())) {
            srcPathErrorLabel.setText("Source directory must be root child");
            return;
        }
        srcPath = rootPath.relativize(chosenFile.toPath());
        srcPathTextField.setText(srcPath.toString());
        srcPathErrorLabel.setText("");
    }

    @FXML
    private void createProject() {
        clearErrorLabels();
        if (!isInputValid()) {
            return;
        }
        Project project = new Project();
        project.setName(projectNameTextField.getText());
        project.setRoot(rootPath);
        project.setPackageName(packageNameTextField.getText());
        project.setSrc(srcPath);
        listener.handle(project);
        stage.close();
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    private boolean isInputValid() {
        boolean isValid = true;
        if (projectNameTextField.getText().trim().length() == 0) {
            projectNameErrorLabel.setText("Project name can't be empty");
            isValid = false;
        }
        if (projectNameTextField.getText().trim().contains(" ")) {
            projectNameErrorLabel.setText("Project name can't contain spaces.");
            isValid = false;
        }
        if (packageNameTextField.getText().trim().length() == 0) {
            packageNameErrorLabel.setText("Package can't be empty");
            isValid = false;
        }
        if (rootPath == null) {
            rootPathErrorLabel.setText("Root path is not specified");
            isValid = false;
        }
        if (srcPath == null) {
            srcPathErrorLabel.setText("Source path is not specified");
        }

        return isValid;
    }

    private void clearErrorLabels() {
        projectNameErrorLabel.setText("");
        packageNameErrorLabel.setText("");
        rootPathErrorLabel.setText("");
        srcPathErrorLabel.setText("");
    }
}
