package hr.fer.nenr.lab.hw7;

import hr.fer.nenr.lab.hw5.Matrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Dataset {
    private int numOfSamples;
    private Matrix X, y;

    public Dataset(String filepath) {
        String fileString = "";
        try {
            fileString = Files.readString(Paths.get(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse design matrix and label matrix
        String[] lines = fileString.split("\n");
        numOfSamples = lines.length;
        String[] lineSegments;

        X = new Matrix(lines.length, 2);
        y = new Matrix(lines.length, 3);

        for (int i = 0; i < lines.length; i++) {
            lineSegments = lines[i].split("\\s+");
            for (int j = 0; j < lineSegments.length; j++) {
                if (j == 0 || j == 1)
                    X.set(i, j, Double.parseDouble(lineSegments[j]));
                else
                    y.set(i, j - 2, Double.parseDouble(lineSegments[j]));
            }
        }
    }

    public int getNumberOfSamples() {
        return this.numOfSamples;
    }

    public Matrix getInputData() {
        return this.X;
    }

    public Matrix getLabels() {
        return this.y;
    }

    public Matrix getSample(int index) {
        double[][] rowValues = new double[1][X.getCols()];
        for (int i = 0; i < X.getCols(); i++) {
            rowValues[0][i] = X.get(index, i);
        }
        return new Matrix(rowValues);
    }

    public Matrix getLabel(int index) {
        double[][] rowValues = new double[1][y.getCols()];
        for (int i = 0; i < y.getCols(); i++) {
            rowValues[0][i] = y.get(index, i);
        }
        return new Matrix(rowValues);
    }
}
