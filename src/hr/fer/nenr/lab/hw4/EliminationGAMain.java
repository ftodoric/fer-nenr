package hr.fer.nenr.lab.hw4;

import java.util.Random;

public class EliminationGAMain {
    public static void main(String[] args) {
        int numberOfParams = 5;
        BitVectorDecoder decoder = new BitVectorDecoder(0.0001, numberOfParams);
        String dataset = "C:/IntelliJ-Workspace/NENR-LAB/resources/evolution_datasets/zad4-dataset1.txt";
        LossFunction lf = new LossFunction(dataset);

        // Setting the parameters
        int popSize = 100;
        double pm = 0.01;   // permutation chance
        EliminationGA ega = new EliminationGA(lf, decoder, popSize, pm, new Random(), 100000);

        double[] params = ega.run();
        System.out.println("BEST SOLUTION of Elimination Genetic Algorithm");
        for (int i = 0; i < params.length; i++) {
            System.out.print("beta" + i + " = ");
            System.out.println(params[i]);
        }
    }
}
