package hr.fer.nenr.lab.hw4;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class LossFunction {
    private final List<Double> x = new ArrayList<>();
    private final List<Double> y = new ArrayList<>();
    private final List<Double> f = new ArrayList<>();

    public LossFunction(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // check if file's not empty
            while ((line = br.readLine()) != null) {
                String[] temp = line.split("\\s+");
                x.add(Double.parseDouble(temp[0]));
                y.add(Double.parseDouble(temp[1]));
                f.add(Double.parseDouble(temp[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double meanSquareError(double[] params) {
        double meanSquareError = 0;

        double calculatedF, sum = 0;
        for (int i = 0; i < this.f.size(); i++) {
            calculatedF = evaluateForSample(i, params);
            sum += Math.pow((this.f.get(i) - calculatedF), 2);
        }

        meanSquareError = sum / this.f.size();

        return meanSquareError;
    }

    public double calculateFitness(double[] params) {
        double meanSquareError = 0;

        double calculatedF, sum = 0;
        for (int i = 0; i < this.f.size(); i++) {
            calculatedF = evaluateForSample(i, params);
            sum += Math.pow((this.f.get(i) - calculatedF), 2);
        }

        meanSquareError = sum / this.f.size();

        return 1.0 / meanSquareError;
    }

    public double evaluateForSample(int i, double[] params) {
        double result;

        double sinPart = Math.sin(params[0] + params[1] * this.x.get(i));
        double cosPart = params[2] * Math.cos(this.x.get(i) * (params[3] + this.y.get(i)))
                * (1.0 / (1 + Math.exp(Math.pow(this.x.get(i) - params[4], 2))));

        return sinPart + cosPart;
    }
}
