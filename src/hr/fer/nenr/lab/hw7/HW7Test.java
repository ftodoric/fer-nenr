package hr.fer.nenr.lab.hw7;

import java.util.Random;

public class HW7Test {
    public static void main(String[] args) {
        Dataset dataset = new Dataset("C:/IntelliJ-Workspace/NENR-LAB/resources/neuro-evolution/test-dataset.txt");

        // CREATE NEURAL NETWORK
        int[] architecture = {2, 2, 3};
        NeuralNetwork nn = new NeuralNetwork(architecture);

        double[] allParams = {
                1, 3,
                2, 4,

                1, 3, 5,
                2, 4, 6,

                0, 0, 0,

                3, 3,
                3, 3
        };

        System.out.println(nn.calcError(allParams, dataset));
    }
}
