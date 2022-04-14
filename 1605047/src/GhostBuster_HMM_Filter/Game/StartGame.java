// --module-path /usr/lib/jvm/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml
// --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml
package GhostBuster_HMM_Filter.Game;


import GhostBuster_HMM_Filter.Game.Controllers.GameControl;
import GhostBuster_HMM_Filter.Game.Controllers.PriningInTextAera;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.PrintStream;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;

/**
 *This is the launcher of client side game part, show GUI & calls Controller class
 * @author  Ahsanul Ameen Sabit
 */


public class StartGame extends Application {

    private static Stage huntStage;
    private static Stage stage = null;

    public static int BOARD_SIZE = 8;
    private static final Label bLabel = new Label();
    private static final Label wLabel = new Label();

    @Override//no need to override.no follow the sequence of javaFx
    public void init() {}

    // overridden start method
    @Override
    public void start(Stage primaryStage) {
        huntStage = primaryStage;
        showBoard();
    }

    //show board
    public void showBoard() {
        stage = huntStage;
        showLogin();
    }

    //show login window
    public void showLogin() {
        stage = huntStage;
        stage.setTitle("Game Configuration");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the SIZE text field
        final TextField gridSize = new TextField();
        gridSize.setPromptText("Grid size [3...20]: ");
        gridSize.setPrefColumnCount(10);
        gridSize.getText();
        GridPane.setConstraints(gridSize, 0, 0);
        grid.getChildren().add(gridSize);
        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);
        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);


        //Adding a Label
        final Label label = new Label("Enter grid size of your room!");
        GridPane.setConstraints(label, 0, 3);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        //Setting an action for the Submit button
        submit.setOnAction(e -> {
            if ((gridSize.getText() != null && !gridSize.getText().isEmpty()) &&
                    gridSize.getText().chars().allMatch(Character::isDigit)) {
                BOARD_SIZE = Integer.parseInt(gridSize.getText());
                if(BOARD_SIZE < 3 || BOARD_SIZE > 20) {
                    label.setText("You have not provide valid input");
                } else huntPage();
            } else {
                label.setText("You have not provide valid input");
            }
        });

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            gridSize.clear();
            gridSize.clear();
            label.setText("Enter grid size of your room!");
        });

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    public void huntPage() {
        // initialize the layout, create a CustomControl and add it to the layout
        StackPane stackLayout = new StackPane();
        // private fields for this class
        GameControl gameControl = new GameControl();
        stackLayout.getChildren().add(gameControl);

        bLabel.setTextFill(Color.BLACK);
        wLabel.setTextFill(Color.PAPAYAWHIP);
        bLabel.setText("Grid size : (" + BOARD_SIZE + " x " + BOARD_SIZE + ")");
        wLabel.setText("@Copyright : Ahsanul Ameen Sabit");
        bLabel.setStyle("-fx-font-size: 15;");
        wLabel.setStyle("-fx-font-size: 15;");

        huntStage.setTitle("GhostBuster (1605047)");
        huntStage.setScene(new Scene(stackLayout, 650, 550));

        // create text box
        TextArea status = new TextArea();
        status.setEditable(false);
        status.setEffect(new InnerShadow());
        status.setPromptText("Status Updates");
        status.setStyle("-fx-font-size: 13;");
        status.setWrapText(true);
        status.setPrefWidth(200);
        status.setPrefHeight(800);

        //making situation for printing in the TextArea
        PrintStream ps = System.out;
        System.setOut(new PriningInTextAera(status, ps));

        //add status in a VBox
        VBox vb1 = new VBox();
        vb1.getChildren().add(status);
        vb1.setPrefWidth(200);

        BorderPane bp = new BorderPane();
        HBox hb1 = new HBox();
        HBox hb2 = new HBox();

        Button timer = new Button("TIME+1");
        Button buster = new Button("BUST");
        timer.setStyle("-fx-background-color: #0b0b00; -fx-border-color: #00f0f0; -fx-border-width: 3px; -fx-text-fill: #fffffa");
        buster.setStyle("-fx-background-color: #000a0a; -fx-border-color: #a0f00f; -fx-border-width: 3px; -fx-text-fill: #fffaff");

        hb1.setAlignment(Pos.CENTER);//make it resizable
        hb1.getChildren().addAll(bLabel,timer, buster);
        hb1.setSpacing(60);
        hb1.setStyle("-fx-background-color: #D3D3D3");
        hb1.setLayoutY(30);

        hb2.setAlignment(Pos.CENTER);//make it resizable
        hb2.getChildren().addAll(wLabel);
        hb2.setSpacing(60);
        hb2.setStyle("-fx-background-color: #2C2C2C");
        hb2.setLayoutY(30);

        huntStage.setScene(new Scene(bp, 670, 520 ));
        bp.setCenter(stackLayout);
        bp.setTop(hb1);
        bp.setBottom(hb2);
        bp.setRight(vb1);

        huntStage.setMaxHeight(700);
        huntStage.setMaxWidth(900);
        huntStage.setMinWidth(500);
        huntStage.setMinHeight(400);
        huntStage.show();

        timer.setOnAction(e -> {
            gameControl.updateTableTimeStamp();
        });

        buster.setOnAction(e -> {
            gameControl.bustTheGhost();
        });
    }

    // overridden stop method
    @Override
    public void stop(){}

    //Launch Pp Pp
    public static void main(String[] args) {
        launch(args);
    }
}
