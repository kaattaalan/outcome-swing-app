package test;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SampleGrid extends GridPane implements EventHandler {

    private final Text actiontarget;
    private final Text title;

    public SampleGrid() {

        setAlignment(Pos.BOTTOM_LEFT);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25,25,25,25));

        title = new Text("OutCome Finder");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        add(title, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        add(userName, 0, 1);

        TextField userTextField = new TextField();
        add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        add(pwBox, 1, 2);


        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        add(hbBtn, 1, 4);

        actiontarget = new Text();
        add(actiontarget, 1, 6);
        btn.setOnAction(this);

    }

    @Override
    public void handle(Event event) {
        actiontarget.setFill(Color.FIREBRICK);
        actiontarget.setText("Sign in button pressed");
    }
}
