package com.example.automatefini;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AffichageFromFile extends Application{

    private File selectedFile;
    private TextArea outputTextArea;
    private Pane drawPane;

    @Override
    public void start(Stage primaryStage) {
        Button downloadButton = new Button("Télécharger l'AFD");
        Button showMathButton = new Button("Afficher la forme mathématique");
        Button drawButton = new Button("Dessiner l'AFD");
        Button backButton = new Button("Retour");

        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setPrefRowCount(30);
        outputTextArea.setPrefWidth(1000);
        outputTextArea.setPrefHeight(1000);

        // outputTextArea.setWrapText(true);

        Button typeLangageButton = new Button("Type Langage");
        Button etapeCalculButton = new Button("Étape Calcul");
        Button expressionRegulierButton = new Button("Expression Régulière");
        Button operationButton = new Button("Complémentaire");

        VBox rightButtons = new VBox(10, typeLangageButton, etapeCalculButton, expressionRegulierButton,
                operationButton);

        drawPane = new Pane();

        downloadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Télécharger le fichier AFD");
            selectedFile = fileChooser.showOpenDialog(primaryStage);
        });

        showMathButton.setOnAction(e -> {
            if (selectedFile != null) {
                try {
                    List<String> lines = readLinesFromFile(selectedFile);
                    StringBuilder content = new StringBuilder();
                    content.append("Q = {").append(String.join(", ", lines.get(0).split(", "))).append("}\n");
                    content.append("Σ = {").append(String.join(", ", lines.get(1).split(", "))).append("}\n");

                    content.append("δ = {\n");
                    for (int i = 2; i < lines.size() - 2; i++) {
                        content.append("\t").append(lines.get(i)).append(",\n");
                    }
                    content.append("}\n");
                    content.append("q0 = ").append(lines.get(lines.size() - 2)).append("\n");
                    content.append("F = {").append(lines.get(lines.size() - 1)).append("}\n");
                    outputTextArea.setText(content.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        drawButton.setOnAction(e -> {
            drawPane.getChildren().clear();
            if (selectedFile != null) {
                try {
                    List<String> lines = readLinesFromFile(selectedFile);
                    int numStates = lines.get(0).split(", ").length;
                    double centerX = drawPane.getWidth() / 2.0;
                    double centerY = drawPane.getHeight() / 2.0;
                    double radius = Math.min(centerX, centerY) * 0.8;
                    double angleIncrement = 360.0 / numStates;
                    Map<String, Circle> stateCircles = new HashMap<>();
                    double angle = -90; // Commencer à -90 degrés (en haut)
                    for (String state : lines.get(0).split(", ")) {
                        double x = centerX + radius * Math.cos(Math.toRadians(angle));
                        double y = centerY + radius * Math.sin(Math.toRadians(angle));
                        Circle circle = new Circle(x, y, 20, Color.WHITE);
                        circle.setStroke(Color.BLACK);
                        circle.setStrokeWidth(2);
                        Text text = new Text(x - 5, y + 5, state);
                        drawPane.getChildren().addAll(circle, text);
                        stateCircles.put(state, circle);
                        angle += angleIncrement;
                    }
                    for (int i = 2; i < lines.size(); i++) {
                        String[] transition = lines.get(i).split(",");
                        if (transition.length == 3) { // Vérifier s'il y a suffisamment d'éléments dans la transition
                            Circle startCircle = stateCircles.get(transition[0]);
                            Circle endCircle = stateCircles.get(transition[2]);
                            if (startCircle != null && endCircle != null) {
                                Line line = new Line(startCircle.getCenterX(), startCircle.getCenterY(),
                                        endCircle.getCenterX(), endCircle.getCenterY());
                                Text text = new Text((startCircle.getCenterX() + endCircle.getCenterX()) / 2,
                                        (startCircle.getCenterY() + endCircle.getCenterY()) / 2, transition[1]);
                                drawPane.getChildren().addAll(line, text);
                                if (lines.get(lines.size() - 1).contains(transition[2])) { // Vérifier si l'état est
                                    // final
                                    Circle finalCircle = new Circle(endCircle.getCenterX(), endCircle.getCenterY(), 15,
                                            Color.TRANSPARENT);
                                    finalCircle.setStroke(Color.BLACK);
                                    drawPane.getChildren().add(finalCircle);
                                }
                            }
                        } else {
                            System.out.println("La ligne de transition est invalide : " + lines.get(i));
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        backButton.setOnAction(e -> {
            // Rediriger vers la première interface
            AffichageMathématiqueParInteraction firstInterface = new AffichageMathématiqueParInteraction();
            firstInterface.start(new Stage());
            primaryStage.close();
        });

        HBox buttonBox = new HBox(10, downloadButton, showMathButton, drawButton);
        buttonBox.setPadding(new Insets(10));

        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane(new HBox(outputTextArea, drawPane), null, backButtonBox, buttonBox,
                rightButtons);

        Scene scene = new Scene(root, 800, 600);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            drawPane.setPrefWidth(newValue.doubleValue());
            drawPane.setMaxWidth(newValue.doubleValue());
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            drawPane.setPrefHeight(newValue.doubleValue());
            drawPane.setMaxHeight(newValue.doubleValue());
        });

        primaryStage.setTitle("AFD JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<String> readLinesFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

