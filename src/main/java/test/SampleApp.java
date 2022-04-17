package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SampleApp  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sample App");
        SampleGrid gridPane = new SampleGrid();
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
