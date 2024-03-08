package com.example.automatefini;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class AffichageMathématiqueParInteraction extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AFD Application");

        Label titleLabel = new Label("Saisir les détails de l'AFD");

        // Champs de texte pour saisir les détails de l'AFD
        TextArea statesTextArea = createTextArea("États...");
        TextArea alphabetTextArea = createTextArea("Alphabet...");
        TextArea transitionFunctionTextArea = createTextArea("Fonction de transition...");
        TextArea initialStateTextArea = createTextArea("État initial...");
        TextArea finalStatesTextArea = createTextArea("États finaux...");

        Button submitButton = new Button("Afficher la forme mathématique de l'AFD");
        Button fileButton = new Button("À travers un fichier");

        // Ajout des boutons à droite des zones de texte
        Button typeLangageButton = new Button("Type Langage");
        Button etapeCalculButton = new Button("Étape Calcul");
        Button expressionRegulierButton = new Button("Expression Régulière");
        Button operationButton = new Button("Complémentaire");
        Button RetourMenu = new Button("Retour");
        VBox rightButtons = new VBox(10, typeLangageButton, etapeCalculButton, expressionRegulierButton,
                operationButton,RetourMenu);
        Label resultLabel = new Label();

        RetourMenu.setOnAction(e -> {
            // Rediriger vers la première interface
            HelloApplication firstInterface = new HelloApplication();
            firstInterface.start(new Stage());
            primaryStage.close();
        });

        // Action lorsque le bouton est cliqué
        submitButton.setOnAction(e -> {
            // Récupérer les valeurs saisies par l'utilisateur
            String states = statesTextArea.getText();
            String alphabet = alphabetTextArea.getText();
            String transitionFunction = transitionFunctionTextArea.getText();
            String initialState = initialStateTextArea.getText();
            String finalStates = finalStatesTextArea.getText();

            // Formater les détails de l'AFD sous forme mathématique
            String formattedAFD = formatAFD(states, alphabet, transitionFunction, initialState, finalStates);

            // Afficher le résultat dans le label

            resultLabel.setText(formattedAFD);
        });

        fileButton.setOnAction(e -> {
            // Rediriger vers la deuxième interface
            AffichageFromFile secondInterface = new AffichageFromFile();
            secondInterface.start(new Stage());
            primaryStage.close();
        });

        HBox hbox = new HBox();
        hbox.getChildren().add(fileButton);
        HBox.setHgrow(hbox, Priority.ALWAYS);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(titleLabel, statesTextArea, alphabetTextArea, transitionFunctionTextArea,
                initialStateTextArea, finalStatesTextArea, submitButton, resultLabel);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hbox);
        BorderPane.setAlignment(hbox, Pos.TOP_RIGHT);
        borderPane.setCenter(vbox);
        borderPane.setRight(rightButtons);

        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextArea createTextArea(String promptText) {
        TextArea textArea = new TextArea();
        textArea.setPromptText(promptText);
        textArea.setPrefRowCount(1); // Réduire le nombre de lignes affichées
        textArea.setMaxHeight(50); // Limiter la hauteur maximale
        return textArea;
    }

    // Méthode pour formater les détails de l'AFD sous forme mathématique

    // Méthode pour formater les détails de l'AFD sous forme mathématique
    private String formatAFD(String states, String alphabet, String transitionFunction, String initialState,
                             String finalStates) {
        // Implémenter la logique de formatage des détails de l'AFD ici

        // Exemple de formatage des détails de l'AFD
        StringBuilder formattedAFD = new StringBuilder();
        formattedAFD.append("Q = {" + states + "}\n");
        formattedAFD.append("Σ = {" + alphabet + "}\n");
        formattedAFD.append("δ = {" + transitionFunction + "}\n");
        formattedAFD.append("q0 = {" + initialState + "}\n");
        formattedAFD.append("F = {" + finalStates + "}\n");

        return formattedAFD.toString();
    }
}
