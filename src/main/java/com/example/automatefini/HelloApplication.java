package com.example.automatefini;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn1 = new Button("Affichage");
        Button btn2 = new Button("L'union de deux automates");
        Button btn3 = new Button("L'intersection de deux automates");
        Button btn4 = new Button("la difference entre deux automates");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(btn1, btn2, btn3, btn4);

        btn1.setOnAction(this::handleButtonAction);
        btn2.setOnAction(this::handleButtonAction);
        btn3.setOnAction(this::handleButtonAction);
        btn4.setOnAction(this::handleButtonAction);


        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Automate Options");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonAction(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Button btn = (Button) event.getSource();
        String option = btn.getText();
        switch (option) {
            case "Affichage":
                AffichageMathématiqueParInteraction AffichageMathématiqueParInteraction=new AffichageMathématiqueParInteraction();
                Stage affichageMathStage = new Stage();
                AffichageMathématiqueParInteraction.start(affichageMathStage);

                break;
            case "L'union de deux automates":

                break;
            case "L'intersection de deux automates":
                // Ajoutez votre logique pour cette option ici
                break;
            case "la difference entre deux automates":
                // Ajoutez votre logique pour cette option ici
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
