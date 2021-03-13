package hr.fer.nenr.lab.hw1;

import java.util.Arrays;

public class LFunction implements IIntUnaryFunction {
    private double[] lValues;

    public LFunction(int first, int last) {
        this.lValues = new double[last + 1];

        // calculate lambdaValues
        Arrays.fill(this.lValues, 1.0);

        double k = -1.0 / (last - first);

        for (int i = 0; i < this.lValues.length; i++) {
            if (i < first) continue;
            this.lValues[i] = k * (i - last) + 0.0; // last: x-axis offset; 0.0 added to remove negative zero
        }
    }

    public double valueAt(int index) {
        if (index > this.lValues.length - 1) return 0.0;
        return this.lValues[index];
    }
}
