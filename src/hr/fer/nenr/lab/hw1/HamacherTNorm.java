package hr.fer.nenr.lab.hw1;

public class HamacherTNorm implements IBinaryFunction {
    private double v;

    public HamacherTNorm(double parameter) {
        this.v = parameter;
    }

    public double valueAt(double a, double b) {
        return (a * b) / (v + (1 - v) * (a + b - a * b));
    }
}
