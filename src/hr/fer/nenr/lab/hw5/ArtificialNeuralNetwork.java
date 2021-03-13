package hr.fer.nenr.lab.hw5;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class ArtificialNeuralNetwork implements ActionListener {
    private int M;
    private final int[] architecture;
    private final int algorithmNumber;
    private GestureDataCollector collector;
    private JLabel outputLabel;
    private Path gestureDataFilePath;
    private final Matrix[] Ws;
    private final Matrix[] W0;
    @SuppressWarnings("FieldCanBeLocal")
    private final double learningRate = 2.0;
    @SuppressWarnings("unused")
    private final double epsilon = 0.5;
    @SuppressWarnings("FieldCanBeLocal")
    private final int iterationNumber = 5000;

    /**
     * Algorithm numbers:
     * 1| Backpropagation (Batch) learning
     * 2| Stohastic backpropagation (online) learning
     * 3| Mini-Batch Backpropagation learning
     */
    public ArtificialNeuralNetwork(int[] architecture, int algorithmNumber) {
        this.architecture = architecture;
        this.algorithmNumber = algorithmNumber;
        this.Ws = new Matrix[architecture.length - 1];
        this.W0 = new Matrix[architecture.length - 1];
        for (int i = 0; i < architecture.length - 1; i++) {
            this.Ws[i] = new Matrix(architecture[i], architecture[i + 1]);
            W0[i] = new Matrix(1, architecture[i + 1]);
        }
    }

    public void setCollector(GestureDataCollector collector) {
        this.collector = collector;
        this.M = collector.getM();
        this.gestureDataFilePath = collector.getGestureDataFilePath();
        Random r = new Random();
        double value;
        for (Matrix w : this.Ws) {
            for (int n = 0; n < w.getRows(); n++) {
                for (int m = 0; m < w.getCols(); m++) {
                    value = r.nextGaussian();
                    w.set(n, m, value * (2 * this.M));
                }
            }
        }
    }

    public void setOutputLabel(JLabel label) {
        this.outputLabel = label;
    }

    // FIT DATA AND GET OPTIMAL PARAMETERS (WEIGHTS)
    public void fit() {
        // COLLECT DATA FROM THE FILE
        String fileString = "";
        try {
            fileString = Files.readString(this.gestureDataFilePath);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        String[] lines = fileString.split("\n");
        Matrix input = new Matrix(lines.length, 2 * this.M);
        Matrix labels = new Matrix(lines.length, 5);
        String[] line;
        String[] label;
        for (int i = 0; i < lines.length; i++) {
            line = lines[i].split("\\s+");
            for (int j = 0; j < line.length - 1; j++) {
                input.set(i, j, Double.parseDouble(line[j]));
            }
            label = line[line.length - 1].split(",");
            for (int j = 0; j < label.length; j++) {
                labels.set(i, j, Double.parseDouble(label[j]));
            }
        }

        // BATCH ALGORITHM
        Matrix y, one, a, b, yk, deltaOutput, delta, deltaPrevious, l;
        if (this.algorithmNumber == 1) {
            int iterNum = 0;
            while (iterNum < this.iterationNumber) {
                iterNum++;
                totalSquareErrorHigherThanEpsilon(input, labels);

                // calculation for the last layer
                y = predict(input);
                one = new Matrix(y.getRows(), y.getCols(), 1.0);
                a = one.sub(y);
                b = labels.sub(y);
                deltaOutput = y.dot(a.dot(b));

                yk = getOutputOfLayer(architecture.length - 2, input);
                double updatedWeights;
                for (int i = 0; i < this.Ws[this.Ws.length - 1].getRows(); i++) {
                    for (int j = 0; j < this.Ws[this.Ws.length - 1].getCols(); j++) {
                        double sum = 0.0;
                        for (int n = 0; n < input.getRows(); n++) {
                            sum += deltaOutput.get(n, j) * yk.get(n, i);
                        }
                        updatedWeights = this.Ws[this.Ws.length - 1].get(i, j) + this.learningRate * sum;
                        this.Ws[this.Ws.length - 1].set(i, j, updatedWeights);
                    }
                }

                // Update last layer biases
                double updatedBiases;
                for (int i = 0; i < this.W0[this.W0.length - 1].getCols(); i++) {
                    double biasSum = 0.0;
                    for (int n = 0; n < input.getRows(); n++) {
                        biasSum += deltaOutput.get(n, i);
                    }
                    updatedBiases = this.W0[this.W0.length - 1].get(0, i) + this.learningRate * biasSum;
                    this.W0[this.W0.length - 1].set(0, i, updatedBiases);
                }

                one = new Matrix(yk.getRows(), yk.getCols(), 1.0);
                a = one.sub(yk);
                b = deltaOutput.matrixDot(this.Ws[this.Ws.length - 1].transpose());
                deltaPrevious = yk.dot(a.dot(b));

                // backpropagation
                for (int i = this.Ws.length - 2; i >= 0; i--) {
                    yk = getOutputOfLayer(i, input);
                    one = new Matrix(yk.getRows(), yk.getCols(), 1.0);
                    a = one.sub(yk);
                    b = deltaPrevious.matrixDot(this.Ws[i].transpose());
                    delta = yk.dot(a.dot(b));
                    for (int i2 = 0; i2 < this.Ws[i].getRows(); i2++) {
                        for (int j2 = 0; j2 < this.Ws[i].getCols(); j2++) {
                            double sum = 0.0;
                            for (int n = 0; n < input.getRows(); n++) {
                                sum += deltaPrevious.get(n, j2) * yk.get(n, i2);
                            }
                            updatedWeights = this.Ws[i].get(i2, j2) + this.learningRate * sum;
                            this.Ws[i].set(i2, j2, updatedWeights);
                        }
                    }
                    // Update biases
                    for (int j2 = 0; j2 < this.W0[i].getCols(); j2++) {
                        double sum = 0.0;
                        for (int n = 0; n < input.getRows(); n++) {
                            sum += deltaPrevious.get(n, j2);
                        }
                        updatedWeights = this.Ws[i].get(0, j2) + this.learningRate * sum;
                        this.W0[i].set(0, j2, updatedWeights);
                    }

                    deltaPrevious = delta.copy();
                }
            }
        } else if (this.algorithmNumber == 2) {
            int iterNum = 0;
            while (iterNum < this.iterationNumber) {
                iterNum++;
                totalSquareErrorHigherThanEpsilon(input, labels);

                Matrix sample = new Matrix(1, input.getCols());
                for (int sample_i = 0; sample_i < input.getRows(); sample_i++) {
                    // get one sample
                    for (int i = 0; i < input.getCols(); i++) {
                        sample.set(0, i, input.get(sample_i, i));
                    }

                    // calculation for the last layer
                    y = predict(sample);
                    one = new Matrix(y.getRows(), y.getCols(), 1.0);
                    a = one.sub(y);
                    // get label of a sample
                    l = new Matrix(1, labels.getCols());
                    for (int i = 0; i < labels.getCols(); i++) {
                        l.set(0, i, labels.get(sample_i, i));
                    }
                    b = l.sub(y);
                    deltaOutput = y.dot(a.dot(b));

                    yk = getOutputOfLayer(architecture.length - 2, sample);
                    double updatedWeights;
                    for (int i = 0; i < this.Ws[this.Ws.length - 1].getRows(); i++) {
                        for (int j = 0; j < this.Ws[this.Ws.length - 1].getCols(); j++) {
                            double sum = 0.0;
                            for (int n = 0; n < 1; n++) {
                                sum += deltaOutput.get(n, j) * yk.get(n, i);
                            }
                            updatedWeights = this.Ws[this.Ws.length - 1].get(i, j) + this.learningRate * sum;
                            this.Ws[this.Ws.length - 1].set(i, j, updatedWeights);
                        }
                    }

                    one = new Matrix(yk.getRows(), yk.getCols(), 1.0);
                    a = one.sub(yk);
                    b = deltaOutput.matrixDot(this.Ws[this.Ws.length - 1].transpose());
                    deltaPrevious = yk.dot(a.dot(b));

                    // backpropagation
                    for (int i = this.Ws.length - 2; i >= 0; i--) {
                        yk = getOutputOfLayer(i, sample);
                        one = new Matrix(yk.getRows(), yk.getCols(), 1.0);
                        a = one.sub(yk);
                        b = deltaPrevious.matrixDot(this.Ws[i].transpose());
                        delta = yk.dot(a.dot(b));
                        for (int i2 = 0; i2 < this.Ws[i].getRows(); i2++) {
                            for (int j2 = 0; j2 < this.Ws[i].getCols(); j2++) {
                                double sum = 0.0;
                                for (int n = 0; n < 1; n++) {
                                    sum += deltaPrevious.get(n, j2) * yk.get(n, i2);
                                }
                                updatedWeights = this.Ws[i].get(i2, j2) + this.learningRate * sum;
                                this.Ws[i].set(i2, j2, updatedWeights);
                            }
                        }
                        deltaPrevious = delta.copy();
                    }
                }
            }
        } else if (this.algorithmNumber == 3) {
            int iterNum = 0;
            while (iterNum < this.iterationNumber) {
                iterNum++;
                totalSquareErrorHigherThanEpsilon(input, labels);

                Matrix group = new Matrix(10, input.getCols());
                for (int group_i = 0; group_i < input.getRows() / 10.0; group_i++) {
                    // get two samples of for each symbol
                    for (int n = 0; n < 10; n += 2) {
                        for (int i = 0; i < 5; i++) {
                            int index = i * 100 / 5 + group_i * 2;
                            for (int j = 0; j < input.getCols(); j++) {
                                group.set(n, j, input.get(index, j));
                                group.set(n + 1, j, input.get(index + 1, j));
                            }
                        }
                    }
                    // get label of a a group
                    double[][] array = {{1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                            {0, 1, 0, 0, 0},
                            {0, 1, 0, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 0, 1, 0},
                            {0, 0, 0, 1, 0},
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1}};
                    l = new Matrix(array);

                    // calculation for the last layer
                    y = predict(group);
                    one = new Matrix(y.getRows(), y.getCols(), 1.0);
                    a = one.sub(y);
                    b = l.sub(y);
                    deltaOutput = y.dot(a.dot(b));

                    yk = getOutputOfLayer(architecture.length - 2, group);
                    double updatedWeights;
                    for (int i = 0; i < this.Ws[this.Ws.length - 1].getRows(); i++) {
                        for (int j = 0; j < this.Ws[this.Ws.length - 1].getCols(); j++) {
                            double sum = 0.0;
                            for (int n = 0; n < 1; n++) {
                                sum += deltaOutput.get(n, j) * yk.get(n, i);
                            }
                            updatedWeights = this.Ws[this.Ws.length - 1].get(i, j) + this.learningRate * sum;
                            this.Ws[this.Ws.length - 1].set(i, j, updatedWeights);
                        }
                    }

                    one = new Matrix(yk.getRows(), yk.getCols(), 1.0);
                    a = one.sub(yk);
                    b = deltaOutput.matrixDot(this.Ws[this.Ws.length - 1].transpose());
                    deltaPrevious = yk.dot(a.dot(b));

                    // backpropagation
                    for (int i = this.Ws.length - 2; i >= 0; i--) {
                        yk = getOutputOfLayer(i, group);
                        one = new Matrix(yk.getRows(), yk.getCols(), 1.0);
                        a = one.sub(yk);
                        b = deltaPrevious.matrixDot(this.Ws[i].transpose());
                        delta = yk.dot(a.dot(b));
                        for (int i2 = 0; i2 < this.Ws[i].getRows(); i2++) {
                            for (int j2 = 0; j2 < this.Ws[i].getCols(); j2++) {
                                double sum = 0.0;
                                for (int n = 0; n < 1; n++) {
                                    sum += deltaPrevious.get(n, j2) * yk.get(n, i2);
                                }
                                updatedWeights = this.Ws[i].get(i2, j2) + this.learningRate * sum;
                                this.Ws[i].set(i2, j2, updatedWeights);
                            }
                        }
                        deltaPrevious = delta.copy();
                    }
                }
            }
        }

        System.out.println("Training done.");
    }

    // FIND AN OUTPUT FOR A GIVEN INPUT
    public Matrix predict(Matrix x) {
        // RUN THROUGH ALL LAYERS
        for (int i = 0; i < this.architecture.length - 1; i++) {
            x = x.matrixDot(this.Ws[i]);
            x = addBias(x, this.W0[i]);
            x = sigmoid(x);
        }
        return x;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        this.collector.setPredictingPhase();
        fit();
    }

    public Matrix sigmoid(Matrix a) {
        double[][] array = new double[a.getRows()][a.getCols()];
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getCols(); j++) {
                array[i][j] = 1.0 / (1.0 + Math.exp(-a.get(i, j)));
            }
        }
        return new Matrix(array);
    }

    public Matrix addBias(Matrix unbiased, Matrix bias) {
        int n = unbiased.getRows(), m = unbiased.getCols();
        Matrix biased = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                biased.set(i, j, unbiased.get(i, j) + bias.get(0, j));
            }
        }
        return biased;
    }

    public Matrix getOutputOfLayer(int layer, Matrix inputData) {
        if (layer == 0) return inputData.copy();
        Matrix x = inputData.copy();
        for (int i = 0; i < layer; i++) {
            x = x.matrixDot(this.Ws[i]);
            x = addBias(x, this.W0[i]);
            x = sigmoid(x);
        }
        return x;
    }

    public void totalSquareErrorHigherThanEpsilon(Matrix input, Matrix labels) {
        Matrix y = predict(input);
        Matrix errorMatrix = labels.sub(y);
        double error = 0.0;
        for (int i = 0; i < errorMatrix.getRows(); i++) {
            for (int j = 0; j < errorMatrix.getCols(); j++) {
                error += Math.abs(errorMatrix.get(i, j));
            }
        }
        error = 1.0 / (2 * input.getRows()) * error;
        System.out.println(error);
    }

    public void setLabel(Matrix result) {
        result.print("Prediction");
        double max = result.get(0, 0);
        int classIndex = 0;
        for (int j = 1; j < result.getCols(); j++) {
            if (result.get(0, j) > max) {
                max = result.get(0, j);
                classIndex = j;
            }
        }
        String predictedClass = switch (classIndex) {
            case 0 -> "\u03b1";
            case 1 -> "\u03b2";
            case 2 -> "\u03b3";
            case 3 -> "\u03b4";
            case 4 -> "\u03b5";
            default -> "Not classified";
        };
        this.outputLabel.setText(predictedClass);
    }
}
