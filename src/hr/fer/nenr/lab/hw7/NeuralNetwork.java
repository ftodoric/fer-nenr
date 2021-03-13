package hr.fer.nenr.lab.hw7;

import hr.fer.nenr.lab.hw5.Matrix;

public class NeuralNetwork {
    private final int[] architecture;
    private final int numOfParams;
    private final Matrix[] Ws;
    private final Matrix[] W0;
    private final Matrix S;

    public NeuralNetwork(int[] architecture) {
        this.architecture = architecture;

        // calculate number of required parameters
        int nParam = 4 * architecture[1];
        for (int i = 2; i < architecture.length; i++) {
            nParam += (architecture[i - 1] + 1) * architecture[i];
        }
        numOfParams = nParam;

        // initialize arrays of matrices W and W0
        Ws = new Matrix[architecture.length - 1];
        for (int i = 0; i < architecture.length - 1; i++) {
            this.Ws[i] = new Matrix(architecture[i], architecture[i + 1]);
        }
        W0 = new Matrix[architecture.length - 2];
        for (int i = 0; i < architecture.length - 2; i++) {
            this.W0[i] = new Matrix(1, architecture[i + 2]);
        }

        // initialize Matrix S
        S = new Matrix(architecture[0], architecture[1]);
    }

    public int getNumOfParams() {
        return this.numOfParams;
    }

    // order of parameters is following:
    // W parameters of all layers, W0 parameters of all layers, S Matrix
    public Matrix calcOutput(double[] allParams, Matrix input) {
        // load all parameters into class variables as matrices
        extractParameters(allParams);

        // load input values into matrix X
        int numOfSamples = input.getRows();
        Matrix X = input.copy();

        // CALCULATE NETWORK OUTPUT
        // 1. Calculate 1st hidden layer (neurons type 1)
        Matrix y = new Matrix(numOfSamples, architecture[1]);
        double sum;
        for (int i = 0; i < X.getRows(); i++) {
            for (int k = 0; k < Ws[0].getCols(); k++) {
                sum = 0.0;
                for (int j = 0; j < X.getCols(); j++) {
                    sum += Math.abs(X.get(i, j) - Ws[0].get(j, k)) / Math.abs(S.get(j, k));
                }
                y.set(i, k, 1.0 / (1.0 + sum));
            }
        }

        // 2. Calculate rest of the layers with neurons type 2
        for (int i = 1; i < this.architecture.length - 1; i++) {
            y = y.matrixDot(this.Ws[i]);
            y = addBias(y, i - 1);
            y = sigmoid(y);
        }

        return y;
    }

    public double calcError(double[] allParams, Dataset dataset) {
        double mse = 0.0;
        int N = dataset.getNumberOfSamples();
        int M = architecture[architecture.length - 1];

        Matrix output = calcOutput(allParams, dataset.getInputData());
        Matrix labels = dataset.getLabels();

        for (int s = 0; s < N; s++) {
            for (int o = 0; o < M; o++) {
                mse += Math.pow(labels.get(s, o) - output.get(s, o), 2);
            }
        }

        return mse / (double) N;
    }

    public Matrix predict(double[] params, Dataset dataset) {
        Matrix output = calcOutput(params, dataset.getInputData());
        for (int i = 0; i < output.getRows(); i++) {
            for (int j = 0; j < output.getCols(); j++) {
                if (output.get(i, j) < 0.5) output.set(i, j, 0.0);
                else output.set(i, j, 1.0);
            }
        }
        return output.copy();
    }

    // UTILITY FUNCTIONS
    public Matrix addBias(Matrix y, int layerIndex) {
        int n = y.getRows(), m = y.getCols();
        double[][] newValues = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newValues[i][j] = y.get(i, j) + W0[layerIndex].get(0, j);
            }
        }
        return new Matrix(newValues);
    }

    public void extractParameters(double[] allParams) {
        int index = 0;

        // load W params
        for (int i = 0; i < Ws.length; i++) {
            for (int r = 0; r < Ws[i].getRows(); r++) {
                for (int c = 0; c < Ws[i].getCols(); c++) {
                    Ws[i].set(r, c, allParams[index]);
                    index++;
                }
            }
            //Ws[i].print("Ws[i]");
        }

        // load W0 params
        for (int i = 0; i < W0.length; i++) {
            for (int r = 0; r < W0[i].getRows(); r++) {
                for (int c = 0; c < W0[i].getCols(); c++) {
                    W0[i].set(r, c, allParams[index]);
                    index++;
                }
            }
            //W0[i].print("W0[i]");
        }

        // load S Matrix
        for (int r = 0; r < S.getRows(); r++) {
            for (int c = 0; c < S.getCols(); c++) {
                S.set(r, c, allParams[index]);
                index++;
            }
        }
        //S.print("S");
    }

    // ACTIVATION FUNCTIONS
    public Matrix sigmoid(Matrix input) {
        int n = input.getRows(), m = input.getCols();
        double[][] newValues = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newValues[i][j] = 1.0 / (1.0 + Math.exp(-input.get(i, j)));
            }
        }
        return new Matrix(newValues);
    }
}
