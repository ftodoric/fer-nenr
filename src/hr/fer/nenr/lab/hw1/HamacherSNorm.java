package hr.fer.nenr.lab.hw1;

public class HamacherSNorm implements IBinaryFunction {
    private double v;

    public HamacherSNorm(double parameter) {
        this.v = parameter;
    }

    public double valueAt(double a, double b) {
        return (a + b - (2 - v) * a * b) / (1 - (1 - v) * a * b);
    }
}
