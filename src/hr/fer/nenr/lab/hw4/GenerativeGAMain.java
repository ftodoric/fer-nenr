package hr.fer.nenr.lab.hw4;

import java.util.Random;

public class GenerativeGAMain {
    public static void main(String[] args) {
        int numberOfParams = 5;
        BitVectorDecoder decoder = new BitVectorDecoder(0.001, numberOfParams);
        String dataset = "C:/IntelliJ-Workspace/NENR-LAB/resources/evolution-datasets/zad4-dataset2.txt";
        LossFunction lf = new LossFunction(dataset);

        // Setting the parameters
        int popSize = 100;
        double pm = 0.01;   // permutation chance
        GenerativeGA gga = new GenerativeGA(lf, decoder, popSize, pm, new Random(), 5000, true);

        double[] params = gga.run();
        System.out.println("BEST SOLUTION of Generative Genetic Algorithm");
        for (int i = 0; i < params.length; i++) {
            System.out.print("beta" + i + " = ");
            System.out.println(params[i]);
        }
    }
}
