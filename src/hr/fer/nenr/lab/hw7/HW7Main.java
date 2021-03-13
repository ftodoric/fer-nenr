package hr.fer.nenr.lab.hw7;

import hr.fer.nenr.lab.hw5.Matrix;

public class HW7Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        // CREATE DATASET
        Dataset learningDataset = new Dataset("C:/IntelliJ-Workspace/NENR-LAB/resources/neuro-evolution/zad7-dataset.txt");

        // ZAD 3 Parameters
        double[] assumedParameters = {
                0.12, 0.38, 0.62, 0.88, 0.12, 0.38, 0.62, 0.88,
                0.75, 0.75, 0.75, 0.75, 0.25, 0.25, 0.25, 0.25,

                0.1, 0.9, 0.1,
                0.1, 0.1, 0.9,
                0.9, 0.1, 0.3,
                0.3, 0.9, 0.1,
                0.9, 0.3, 0.1,
                0.1, 0.9, 0.3,
                0.1, 0.1, 0.9,
                0.9, 0.1, 0.1,

                0, 0, 0, 0, 0, 0, 0, 0,

                0, 0, 0,

                0.13, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13,
                0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25
        };

        // CREATE NEURAL NETWORK
        int[] architecture = {2, 1, 6, 3};
        NeuralNetwork nn = new NeuralNetwork(architecture);

        NeuralNetworkGA nnAlgorithm = new NeuralNetworkGA(5000, 1e-7, 100, nn.getNumOfParams());
        nnAlgorithm.setNeuralNetworkAndDataset(nn, learningDataset);

        MutationOperator M1 = new GaussianBiasedMutation(0.002);
        MutationOperator M2 = new GaussianBiasedMutation(0.005);
        MutationOperator M3 = new SimpleGaussianMutation(0.1);
        MutationOperator[] mutationOperators = {M1, M2, M3};
        double[] pms = {0.1, 0.1, 0.1}, desirabilities = {4.0, 1.0, 1.0};
        nnAlgorithm.setMutationParameters(mutationOperators, pms, desirabilities);

        double[] params = nnAlgorithm.run();

        // PRINT NEURAL NETWORK PARAMETERS
        System.out.println("\nNeural network parameters:");

        for (double param : params) {
            System.out.printf("%.2f, ", param);
        }
        System.out.println("\n");

        // Print prediction comparison to given dataset labels
        Dataset predictionDataset = new Dataset("C:/IntelliJ-Workspace/NENR-LAB/resources/neuro-evolution/zad7-dataset.txt");
        Matrix prediction = nn.predict(params, predictionDataset);
        Matrix labels = predictionDataset.getLabels();
        int correctlyClassified = 0, incorrectlyClassified = 0;
        System.out.println("Predictions | Dataset Labels");
        for (int i = 0; i < prediction.getRows(); i++) {
            boolean correct = true;
            for (int j = 0; j < prediction.getCols(); j++) {
                System.out.print(prediction.get(i, j) + " ");
                if (Math.abs(prediction.get(i, j) - labels.get(i, j)) > 1e-7) {
                    correct = false;
                }
            }
            if (correct)
                correctlyClassified++;
            else
                incorrectlyClassified++;
            System.out.print("| ");
            for (int j = 0; j < prediction.getCols(); j++) {
                System.out.print(labels.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println("Number of correctly classified: " + correctlyClassified);
        System.out.println("Number of incorrectly classified: " + incorrectlyClassified);

        //Calculate time elapsed
        long endTime = System.nanoTime();
        long elapsed = endTime - startTime;
        System.out.println("Time elapsed: " + elapsed * 1e-9 + " sec");
    }
}
