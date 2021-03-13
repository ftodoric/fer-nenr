package hr.fer.nenr.lab.hw1;

import java.util.Arrays;

public class GammaFunction implements IIntUnaryFunction {
    private double[] gammaValues;

    public GammaFunction(int first, int last) {
        this.gammaValues = new double[last + 1];

        // calculate lambdaValues
        Arrays.fill(this.gammaValues, 0.0);

        double k = 1.0 / (last - first);

        for (int i = 0; i < this.gammaValues.length; i++) {
            if (i < first) continue;
            this.gammaValues[i] = k * (i - first); // first: x-axis offset
        }
    }

    public double valueAt(int index) {
        if (index > this.gammaValues.length - 1) return 1.0;
        return this.gammaValues[index];
    }
}
