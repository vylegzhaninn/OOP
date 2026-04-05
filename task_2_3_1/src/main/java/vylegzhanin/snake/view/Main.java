package vylegzhanin.snake.view;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Точка входа JavaFX приложения. Отвечает за загрузку FXML и показ окна игры.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/vylegzhanin/snake/MainView.fxml")));
            primaryStage.setTitle("Snake Game OOP - Levels & Items");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error launching Game");
            alert.setHeaderText("Could not load MainView.fxml");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
