package hr.fer.nenr.lab.hw7;

import java.util.Random;

public class CrossoverOperators {
    public static Solution BLXAlpha(Solution p1, Solution p2, double alpha) {
        int n = p1.getSize();
        double[] childValues = new double[n];
        double cmin, cmax, I, lowerLimit, upperLimit;

        Random random = new Random();
        double r;

        for (int i = 0; i < n; i++) {
            // calculate lower and upper limit
            cmin = Math.min(p1.get(i), p2.get(i));
            cmax = Math.max(p1.get(i), p2.get(i));
            I = cmax - cmin;

            lowerLimit = cmin - I * alpha;
            upperLimit = cmax + I * alpha;

            // assign child values
            r = random.nextDouble() * (upperLimit - lowerLimit) + lowerLimit;
            childValues[i] = r;
        }

        return new Solution(childValues);
    }

    public static Solution Arithmetical(Solution p1, Solution p2) {
        Random r = new Random();
        double a;
        double[] childValues = new double[p1.getSize()];
        for (int i = 0; i < p1.getSize(); i++) {
            a = r.nextFloat();
            childValues[i] = a * p1.get(i) + (1.0 - a) * p2.get(i);
        }
        return new Solution(childValues);
    }

    public static Solution Heuristic(Solution p1, Solution p2) {
        Solution better, other;
        // find which parent is better
        if (p1.getFitness() > p2.getFitness()) {
            better = p1.copy();
            other = p2.copy();
        } else {
            better = p2.copy();
            other = p1.copy();
        }

        Random r = new Random();
        double a;
        double[] values = new double[p1.getSize()];
        for (int i = 0; i < p1.getSize(); i++) {
            a = r.nextFloat();
            values[i] = a * (better.get(i) - other.get(i)) + better.get(i);
        }
        return new Solution(values);
    }
}
