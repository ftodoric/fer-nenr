package hr.fer.nenr.lab.hw6;

public class HW6Main {
    public static void main(String[] args) {
        // SET PATH TO RESOURCES OF THIS PROJECT
        String resourcesPath = "C:/IntelliJ-Workspace/NENR-LAB/resources/neuro-fuzzy/";

        // SET PARAMETERS
        int numOfRules = Integer.parseInt(args[0]);
        numOfRules = 5;
        int learningAlgorithm = 2;
        int numOfEpochs = 50000;
        double etaAC = 1e-6;
        double etaBD = 1e-6;
        double etPQR = 1e-4;

        // CONFIGURE ANFIS
        String datasetFilepath = resourcesPath + "dataset.txt";
        ANFIS anfis = new ANFIS(numOfRules, datasetFilepath, learningAlgorithm, numOfEpochs, etaAC, etaBD, etPQR);

        // TRAIN ANFIS
        anfis.fit();

        // MSE
        System.out.println("MSE = " + anfis.calcMse());

        // PLOT DATA FOR ANFIS FUNCTION
        anfis.getAnfisFuncPlotData(resourcesPath + "meshGridXY.txt", resourcesPath + "anfisFuncPlotData.txt");

        // PLOT DATA FOR SAMPLE ERRORS
        anfis.getErrorPlotData(resourcesPath + "meshGridXY.txt", resourcesPath + "sampleErrorPlotData.txt");

        // PLOT DATA FOR EACH MEMBERSHIP FUNCTION
        anfis.getMiFuncPlotData(resourcesPath + "miFuncDomain.txt", resourcesPath);

        // PLOT DATA FOR MSE FOR EACH EPOCH
        anfis.getEpochMsePlotData(resourcesPath + "mseByEpoch.txt");
    }
}
