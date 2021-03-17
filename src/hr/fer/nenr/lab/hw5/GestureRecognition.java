package hr.fer.nenr.lab.hw5;

import java.awt.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestureRecognition extends JFrame {
    static int width = 800, height = 700;
    static String gestureDatasetFilePath = "C:\\IntelliJ-Workspace\\fer-nenr\\resources\\gestures-folder\\gesture-dataset.txt";

    public GestureRecognition() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(700, 20);
        setSize(width, height);
        setTitle("GESTURE RECOGNITION USING ARTIFICIAL NEURAL NETWORK");
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                GestureRecognition topLevel = new GestureRecognition();

                // NORTH PANEL
                JPanel northPanel = new JPanel();
                topLevel.add(northPanel, BorderLayout.NORTH);
                //northPanel.setBackground(Color.RED);
                northPanel.setLayout(new FlowLayout());

                // Instruction Label
                JLabel instructionLabel = new JLabel("Symbols: " +
                        "\u03b1 \u03b2 \u03b3 \u03b4 \u03b5");
                instructionLabel.setFont(instructionLabel.getFont().deriveFont(15.0f));
                instructionLabel.setOpaque(true);
                instructionLabel.setBackground(Color.LIGHT_GRAY);
                northPanel.add(instructionLabel);

                // Button for training a neural network with collected gestures
                JButton trainButton = new JButton("Train");
                northPanel.add(trainButton);

                // Prediction Label
                JLabel predictionTitle = new JLabel("Prediction: ");
                JLabel predictionLabel = new JLabel();
                predictionTitle.setFont(instructionLabel.getFont().deriveFont(15.0f));
                predictionLabel.setFont(instructionLabel.getFont().deriveFont(15.0f));
                predictionTitle.setOpaque(true);
                predictionLabel.setOpaque(true);
                //predictionLabel.setBackground(Color.YELLOW);
                northPanel.add(predictionTitle);
                northPanel.add(predictionLabel);

                // SOUTH PANEL
                // Panel for gesture input
                JPanel gestureInputPanel = new JPanel();
                //gestureInputPanel.setBackground(Color.BLUE);
                topLevel.add(gestureInputPanel, BorderLayout.CENTER);

                // EVENT LISTENERS
                // Gesture Data Collector
                int M = 20;
                Path gestureDataFilePath = Paths.get(gestureDatasetFilePath);
                GestureDataCollector collector = new GestureDataCollector(gestureDataFilePath, M);

                // Artificial Neural Network
                int[] architecture = {2 * M, 40, 10, 5};
                ArtificialNeuralNetwork ann = new ArtificialNeuralNetwork(architecture, 1);

                // configure collector
                collector.setANN(ann);

                // configure neural network
                ann.setCollector(collector);
                ann.setOutputLabel(predictionLabel);

                // subscribe for events
                gestureInputPanel.addMouseListener(collector);
                gestureInputPanel.addMouseMotionListener(collector);
                trainButton.addActionListener(ann);

                // DISPLAY THE WINDOW
                topLevel.setVisible(true);
            });
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
