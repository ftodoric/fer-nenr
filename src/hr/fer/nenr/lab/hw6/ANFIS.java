package hr.fer.nenr.lab.hw6;

import hr.fer.nenr.lab.hw5.Matrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class ANFIS {
    /**
     * A rule has 7 parameters.
     * Each rules has it's parameters in a row of Matrix params.
     * Matrix params dimensions: numOfRules x 7
     */
    private int numOfRules;
    private Matrix params;
    double[][] dataset;
    int numOfSamples;
    int learningAlgorithm; // 1 - stohastic "online" algorithm, 2 - batch algorithm
    double etaAC = 1e-7;
    double etaBD = 1e-9;
    double etaPQR = 1e-4;
    int numOfEpochs = 50000;
    StringBuilder epochMseString;

    public ANFIS(int numOfRules, String datasetFilepath, int learningAlgorithm, int numOfEpochs, double etaAC, double etaBD, double etaPQR) {
        this.numOfRules = numOfRules;
        this.params = new Matrix(numOfRules, 4 + 3);
        this.learningAlgorithm = learningAlgorithm;
        this.numOfEpochs = numOfEpochs;
        this.etaAC = etaAC;
        this.etaBD = etaBD;
        this.etaPQR = etaPQR;

        // Parse the dataset
        try {
            String datasetString = Files.readString(Paths.get(datasetFilepath));
            String[] lines = datasetString.split("\n");
            numOfSamples = lines.length;
            dataset = new double[numOfSamples][3];

            String[] lineSegments;
            for (int i = 0; i < lines.length; i++) {
                lineSegments = lines[i].split("\\s+");
                for (int j = 0; j < lineSegments.length; j++) {
                    dataset[i][j] = Double.parseDouble(lineSegments[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FIT THE DATA AND FIND OPTIMAL PARAMETERS
    public void fit() {
        // Generate initial parameters
        Random random = new Random();
        double stdDev = 1.0;
        for (int i = 0; i < params.getRows(); i++) {
            for (int j = 0; j < params.getCols(); j++) {
                if (j == params.getCols() - 1)
                    continue;
                params.set(i, j, random.nextGaussian() * stdDev);
            }
        }

        // Run chosen learning algorithm
        if (learningAlgorithm == 1)
            runStohasticAlgorithm();
        else if (learningAlgorithm == 2)
            runBatchAlgorithm();
    }

    public void runStohasticAlgorithm() {
        double ai, bi, ci, di, pi, qi, ri;
        double x, y, output, trueOutput, outputDiff, sumOfAlphas, sumOfAlphas2, sumOfAlphaZDiff, ua, ub, alphai;
        epochMseString = new StringBuilder();
        for (int epoch = 1; epoch <= numOfEpochs; epoch++) {
            if (epoch % 1000 == 1)
                System.out.println(calcMse());
            for (int k = 0; k < numOfSamples; k++) {
                // REQUIRED DATA
                x = dataset[k][0];
                y = dataset[k][1];

                output = calcOutput(x, y);
                trueOutput = dataset[k][2];
                outputDiff = trueOutput - output;

                sumOfAlphas = 0.0;
                for (int i = 0; i < numOfRules; i++) {
                    sumOfAlphas += calcAlpha(i, x, y);
                }
                sumOfAlphas2 = Math.pow(sumOfAlphas, 2);

                for (int i = 0; i < numOfRules; i++) {
                    // FOR EACH RULE
                    ai = params.get(i, 0);
                    bi = params.get(i, 1);
                    ci = params.get(i, 2);
                    di = params.get(i, 3);
                    pi = params.get(i, 4);
                    qi = params.get(i, 5);
                    ri = params.get(i, 6);

                    alphai = calcAlpha(i, x, y);

                    sumOfAlphaZDiff = calcSumOfAlphasZDiff(i, x, y);

                    ua = calcU(x, ai, bi);
                    ub = calcU(y, ci, di);

                    // ai update
                    params.set(i, 0, ai + etaAC * outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ua * (1 - ua) * bi * ub);

                    // bi update
                    params.set(i, 1, bi + etaBD * outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ua * (1 - ua) * (ai - x) * ub);

                    // ci update
                    params.set(i, 2, ci + etaAC * outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ub * (1 - ub) * di * ua);

                    // di update
                    params.set(i, 3, di + etaBD * outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ub * (1 - ub) * (ci - x) * ua);

                    // pi update
                    params.set(i, 4, pi + etaPQR * outputDiff * (alphai) / (sumOfAlphas) * x);

                    // qi update
                    params.set(i, 5, qi + etaPQR * outputDiff * (alphai) / (sumOfAlphas) * y);

                    // ri update
                    params.set(i, 6, ri + etaPQR * outputDiff * (alphai) / (sumOfAlphas));
                }
            }
            epochMseString.append(epoch).append(" ").append(calcMse()).append("\n");
        }
    }

    public void runBatchAlgorithm() {
        double ai, bi, ci, di, pi, qi, ri;
        double x, y, output, trueOutput, outputDiff, sumOfAlphas, sumOfAlphas2, sumOfAlphaZDiff, ua, ub, alphai;
        epochMseString = new StringBuilder();
        for (int epoch = 1; epoch <= numOfEpochs; epoch++) {
            if (epoch % 1000 == 1)
                System.out.println(calcMse());
            for (int i = 0; i < numOfRules; i++) {
                // FOR EACH RULE
                ai = params.get(i, 0);
                bi = params.get(i, 1);
                ci = params.get(i, 2);
                di = params.get(i, 3);
                pi = params.get(i, 4);
                qi = params.get(i, 5);
                ri = params.get(i, 6);

                double aiGrad = 0.0, biGrad = 0.0, ciGrad = 0.0, diGrad = 0.0, piGrad = 0.0, qiGrad = 0.0, riGrad = 0.0;
                for (int k = 0; k < numOfSamples; k++) {
                    // REQUIRED DATA
                    x = dataset[k][0];
                    y = dataset[k][1];

                    output = calcOutput(x, y);
                    trueOutput = dataset[k][2];
                    //System.out.println(output - trueOutput);
                    outputDiff = trueOutput - output;

                    ua = calcU(x, ai, bi);
                    ub = calcU(y, ci, di);

                    sumOfAlphas = 0.0;
                    for (int r = 0; r < numOfRules; r++) {
                        sumOfAlphas += calcAlpha(r, x, y);
                    }
                    sumOfAlphas2 = Math.pow(sumOfAlphas, 2);

                    sumOfAlphaZDiff = calcSumOfAlphasZDiff(i, x, y);

                    alphai = calcAlpha(i, x, y);

                    // update ai gradient
                    aiGrad += outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ua * (1 - ua) * bi * ub;

                    // update bi gradient
                    biGrad += outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ua * (1 - ua) * (ai - x) * ub;

                    // update ci gradient
                    ciGrad += outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ub * (1 - ub) * di * ua;

                    // update di gradient
                    diGrad += outputDiff * (sumOfAlphaZDiff) / (sumOfAlphas2) * ub * (1 - ub) * (ci - x) * ua;

                    // update pi gradient
                    piGrad += outputDiff * (alphai) / (sumOfAlphas) * x;

                    // update qi gradient
                    qiGrad += outputDiff * (alphai) / (sumOfAlphas) * y;

                    // update ri gradient
                    riGrad += outputDiff * (alphai) / (sumOfAlphas);
                }

                // ai update
                params.set(i, 0, ai + etaAC * aiGrad);

                // bi update
                params.set(i, 1, bi + etaBD * biGrad);

                // ci update
                params.set(i, 2, ci + etaAC * ciGrad);

                // di update
                params.set(i, 3, di + etaBD * diGrad);

                // pi update
                params.set(i, 4, pi + etaPQR * piGrad);

                // qi update
                params.set(i, 5, qi + etaPQR * qiGrad);

                // ri update
                params.set(i, 6, ri + etaPQR * riGrad);
            }
            epochMseString.append(epoch).append(" ").append(calcMse()).append("\n");
        }
    }

    public double[] predict() {
        double[] predicted = new double[numOfSamples];
        for (int i = 0; i < numOfSamples; i++) {
            predicted[i] = calcOutput(dataset[i][0], dataset[i][1]);
        }
        return predicted;
    }

    public double calcMse() {
        double[] predicted = new double[numOfSamples];
        double mse = 0.0;
        for (int i = 0; i < numOfSamples; i++) {
            predicted[i] = calcOutput(dataset[i][0], dataset[i][1]);
            mse += Math.pow(predicted[i] - dataset[i][2], 2);
        }
        return mse / numOfSamples;
    }

    // PLOT DATA FUNCTIONS
    public void getAnfisFuncPlotData(String inputFile, String outputFile) {
        int numOfSamples = 0;
        double[][] dataset = new double[1][1];
        // Parse the dataset
        try {
            String datasetString = Files.readString(Paths.get(inputFile));
            String[] lines = datasetString.split("\n");
            numOfSamples = lines.length;
            dataset = new double[numOfSamples][3];

            String[] lineSegments;
            for (int i = 0; i < lines.length; i++) {
                lineSegments = lines[i].split("\\s+");
                for (int j = 0; j < lineSegments.length; j++) {
                    dataset[i][j] = Double.parseDouble(lineSegments[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Get predictions
            StringBuilder plotDataString = new StringBuilder();
            for (int i = 0; i < numOfSamples; i++) {
                dataset[i][2] = calcOutput(dataset[i][0], dataset[i][1]);
                plotDataString.append(dataset[i][0]).append(" ").append(dataset[i][1]).append(" ").append(dataset[i][2]).append("\n");
            }
            Files.writeString(Paths.get(outputFile), plotDataString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getErrorPlotData(String inputFile, String outputFile) {
        int numOfSamples = 0;
        double[][] dataset = new double[1][1];
        // Parse the dataset
        try {
            String datasetString = Files.readString(Paths.get(inputFile));
            String[] lines = datasetString.split("\n");
            numOfSamples = lines.length;
            dataset = new double[numOfSamples][3];

            String[] lineSegments;
            for (int i = 0; i < lines.length; i++) {
                lineSegments = lines[i].split("\\s+");
                for (int j = 0; j < lineSegments.length; j++) {
                    dataset[i][j] = Double.parseDouble(lineSegments[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Get predictions
            StringBuilder plotDataString = new StringBuilder();
            double delta;
            for (int i = 0; i < numOfSamples; i++) {
                delta = calcOutput(dataset[i][0], dataset[i][1]) - dataset[i][2];
                plotDataString.append(dataset[i][0]).append(" ").append(dataset[i][1]).append(" ").append(delta).append("\n");
            }
            Files.writeString(Paths.get(outputFile), plotDataString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMiFuncPlotData(String inputFile, String outputDir) {
        int numOfSamples = 0;
        double[] dataset = new double[1];
        // Parse the dataset
        try {
            String datasetString = Files.readString(Paths.get(inputFile));
            String[] lines = datasetString.split("\n");
            numOfSamples = lines.length;
            dataset = new double[numOfSamples];

            for (int i = 0; i < lines.length; i++) {
                dataset[i] = Double.parseDouble(lines[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Iterate for all rules
            for (int i = 0; i < numOfRules; i++) {
                // Plot data for mi(A)
                StringBuilder plotDataString = new StringBuilder();
                double mi;
                for (int j = 0; j < numOfSamples; j++) {
                    mi = calcU(dataset[j], params.get(i, 0), params.get(i, 1));
                    plotDataString.append(dataset[j]).append(" ").append(mi).append("\n");
                }
                Files.writeString(Paths.get(outputDir + "mia" + (i + 1) + ".txt"), plotDataString.toString());

                // Plot data for mi(B)
                plotDataString = new StringBuilder();
                for (int j = 0; j < numOfSamples; j++) {
                    mi = calcU(dataset[j], params.get(i, 2), params.get(i, 3));
                    plotDataString.append(dataset[j]).append(" ").append(mi).append("\n");
                }
                Files.writeString(Paths.get(outputDir + "mib" + (i + 1) + ".txt"), plotDataString.toString());

                // Plot data for z
                plotDataString = new StringBuilder();
                for (int j = 0; j < numOfSamples; j++) {
                    mi = calcU(dataset[j], params.get(i, 2), params.get(i, 3));
                    plotDataString.append(dataset[j]).append(" ").append(mi).append("\n");
                }
                Files.writeString(Paths.get(outputDir + "mib" + (i + 1) + ".txt"), plotDataString.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEpochMsePlotData(String outputFile) {
        try {
            Files.writeString(Paths.get(outputFile), epochMseString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UTILITY FUNCTIONS
    public double calcSumOfAlphasZDiff(int ruleIndex, double x, double y) {
        double sum = 0.0;
        double zi = calcZ(ruleIndex, x, y);
        for (int i = 0; i < numOfRules; i++) {
            if (i == ruleIndex)
                continue;
            sum += calcAlpha(i, x, y) * (zi - calcZ(i, x, y));
        }
        return sum;
    }

    public double calcLossFunc(double trueOut, double anfisOut) {
        return 1.0 / 2 * Math.pow(trueOut - anfisOut, 2);
    }

    public double calcOutput(double x, double y) {
        double numerator = 0.0, denominator = 0.0;
        double alpha;
        for (int i = 0; i < numOfRules; i++) {
            alpha = calcAlpha(i, x, y);
            numerator += alpha * calcZ(i, x, y);
            denominator += alpha;
        }
        return numerator / denominator;
    }

    public double calcZ(int ruleIndex, double x, double y) {
        return params.get(ruleIndex, 4) * x + params.get(ruleIndex, 5) * y + params.get(ruleIndex, 6);
    }

    public double calcAlpha(int ruleIndex, double x, double y) {
        double alpha;
        double ua = calcU(x, params.get(ruleIndex, 0), params.get(ruleIndex, 1));
        double ub = calcU(y, params.get(ruleIndex, 2), params.get(ruleIndex, 3));
        alpha = ua * ub;
        return alpha;
    }

    public double calcU(double variable, double ai, double bi) {
        return 1.0 / (1.0 + Math.exp(bi * (variable - ai)));
    }
}
