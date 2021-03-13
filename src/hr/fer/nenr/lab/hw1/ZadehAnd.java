package hr.fer.nenr.lab.hw1;

public class ZadehAnd implements IBinaryFunction {
    public double valueAt(double value1, double value2) {
        return Math.min(value1, value2);
    }
}
