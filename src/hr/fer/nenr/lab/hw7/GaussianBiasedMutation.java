package hr.fer.nenr.lab.hw7;

import java.util.Random;

public class GaussianBiasedMutation extends MutationOperator {
    public GaussianBiasedMutation(double stdDev) {
        super(stdDev);
    }

    @Override
    public Solution mutate(Solution s, double pm) {
        int n = s.getSize();
        double[] newValues = new double[n];

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            newValues[i] = s.get(i);
            if (random.nextFloat() < pm) {
                newValues[i] += random.nextGaussian() * stdDev;
            }
        }

        return new Solution(newValues);
    }
}
