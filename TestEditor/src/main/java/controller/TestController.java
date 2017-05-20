package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by roman on 07.05.2017.
 */
public class TestController implements Initializable {
    @Setter private Stage stage;
    @Setter private TestCreationListener listener;

    @FXML private TextField testNameTextField;
    @FXML private Label testNameErrorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void createTest() {
        if (!isTestNameValid()) {
            return;
        }
        listener.handle(testNameTextField.getText().trim());
        stage.close();
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    private boolean isTestNameValid() {
        boolean isValid = true;
        if (testNameTextField.getText().trim().length() == 0) {
            testNameErrorLabel.setText("Test name can't be empty");
            isValid = false;
        }
        if (testNameTextField.getText().contains(" ")) {
            testNameErrorLabel.setText("Test name can't have spaces");
            isValid = false;
        }

        return isValid;
    }
}
