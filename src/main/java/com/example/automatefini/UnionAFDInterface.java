package com.example.automatefini;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UnionAFDInterface extends Application {

    private File selectedFile1;
    private File selectedFile2;
    private TextArea outputTextArea;

    @Override
    public void start(Stage primaryStage) {
        Button chooseFileButton1 = new Button("Choisir le fichier 1");
        Button chooseFileButton2 = new Button("Choisir le fichier 2");
        Button calculateButton = new Button("Calculer l'union");

        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);

        chooseFileButton1.setOnAction(e -> {
            selectedFile1 = chooseFile(primaryStage);
        });

        chooseFileButton2.setOnAction(e -> {
            selectedFile2 = chooseFile(primaryStage);
        });

        calculateButton.setOnAction(e -> {
            if (selectedFile1 != null && selectedFile2 != null) {
                try {
                    // Lire les automates à partir des fichiers
                    StringBuilder content = new StringBuilder();
                    Automaton automate1 = Automaton.readAutomatonFromFile(selectedFile1);
                    Automaton automate2 = Automaton.readAutomatonFromFile(selectedFile2);

                    // Appeler une autre classe pour effectuer l'union
                    Automaton unionAutomaton = Automaton.union(automate1, automate2);
                    List<String> formAutomate = Automaton.toMathematicalForm(unionAutomaton);
                    // Afficher le résultat dans la zone de texte
                    for (String line : formAutomate) {
                        content.append(line).append("\n");
                    }
                    outputTextArea.setText(content.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                outputTextArea.setText("Veuillez sélectionner deux fichiers.");
            }
        });

        VBox buttonsBox = new VBox(10, chooseFileButton1, chooseFileButton2, calculateButton);
        buttonsBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane(outputTextArea, null, null, buttonsBox, null);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Union d'Automates");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        return fileChooser.showOpenDialog(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
