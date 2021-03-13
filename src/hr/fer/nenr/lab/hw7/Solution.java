package hr.fer.nenr.lab.hw7;

import java.util.Arrays;
import java.util.Objects;

public class Solution {
    private double[] values;
    private double fitness;

    public Solution(double[] values) {
        this.values = values;
    }

    // GETTERS & SETTERS
    public double[] getValues() {
        return this.values;
    }

    public int getSize() {
        return this.values.length;
    }

    public double get(int index) {
        return this.values[index];
    }

    public void set(int index, double newValue) {
        this.values[index] = newValue;
    }

    public double getFitness() {
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    // UTILITY METHODS
    public void print(String heading) {
        StringBuilder sb = new StringBuilder(heading + " = [");
        for (int i = 0; i < this.getSize(); i++) {
            sb.append(Double.toString(this.get(i)));
            if (i != this.getSize() - 1) sb.append(", ");
        }
        System.out.println(sb.append("] fit = ").append(fitness).toString());
    }

    public Solution copy() {
        double[] copiedValues = new double[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {
            copiedValues[i] = this.get(i);
        }
        Solution newSolution = new Solution(copiedValues);
        newSolution.setFitness(this.getFitness());
        return newSolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return Double.compare(solution.fitness, fitness) == 0 && Arrays.equals(values, solution.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fitness);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
