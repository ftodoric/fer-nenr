package hr.fer.nenr.lab.hw7;

public abstract class MutationOperator {
    protected double stdDev;

    public MutationOperator(double stdDev) {
        this.stdDev = stdDev;
    }

    public abstract Solution mutate(Solution s, double pm);
}
