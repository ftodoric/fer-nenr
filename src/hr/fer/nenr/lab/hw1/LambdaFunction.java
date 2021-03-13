package hr.fer.nenr.lab.hw1;

import java.util.Arrays;

public class LambdaFunction implements IIntUnaryFunction {
    private double[] lambdaValues;

    public LambdaFunction(int first, int middle, int last) {
        this.lambdaValues = new double[last + 1];

        // calculate lambdaValues
        Arrays.fill(this.lambdaValues, 0.0);

        double k1 = 1.0 / (middle - first);
        double k2 = -1.0 / (last - middle);

        for (int i = 0; i < this.lambdaValues.length; i++) {
            if (i < first) continue;
            if (i <= middle)
                this.lambdaValues[i] = k1 * (i - first); // first: x-axis offset
            else
                this.lambdaValues[i] = k2 * (i - last) + 0.0;  // last: x-axis offset; 0.0 added to remove negative zero
        }
    }

    public double valueAt(int index) {
        if (index > this.lambdaValues.length - 1) return 0.0;
        return this.lambdaValues[index];
    }
}
