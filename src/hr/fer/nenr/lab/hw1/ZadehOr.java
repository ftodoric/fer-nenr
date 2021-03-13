package hr.fer.nenr.lab.hw1;

public class ZadehOr implements IBinaryFunction {
    public double valueAt(double value1, double value2) {
        return Math.max(value1, value2);
    }
}
