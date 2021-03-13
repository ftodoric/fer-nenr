package hr.fer.nenr.lab.hw7;

import java.util.Random;

public class NeuralNetworkGA {
    // Core structures
    private NeuralNetwork nn;
    private Dataset dataset;

    private int maxGenerations;
    private double epsilon = 1e-7; // 10^-7
    private int popSize;
    private int solutionLength;
    private Solution[] population;
    private double bestMSE;

    // Mutation configuration
    private MutationOperator[] mutationOperators;
    private double[] pms;
    private double[] vs;

    public NeuralNetworkGA(int maxGenerations, double epsilon, int popSize, int solutionLength) {
        this.maxGenerations = maxGenerations;
        this.epsilon = epsilon;
        this.popSize = popSize;
        this.solutionLength = solutionLength;
    }

    public void setNeuralNetworkAndDataset(NeuralNetwork nn, Dataset dataset) {
        this.nn = nn;
        this.dataset = dataset;
    }

    public void setMutationParameters(MutationOperator[] mutationOperators, double[] pms, double[] desirabilities) {
        this.mutationOperators = mutationOperators;
        this.pms = pms;

        // initialize mutation operators probabilities
        vs = new double[desirabilities.length];
        double sumOfDesirabilities = 0;
        for (int i = 0; i < desirabilities.length; i++) {
            sumOfDesirabilities += desirabilities[i];
        }
        for (int i = 0; i < desirabilities.length; i++) {
            vs[i] = desirabilities[i] / sumOfDesirabilities;
        }
    }

    public double[] run() {
        generateInitialPopulation();

        double mse;

        // set initial fitnesses
        mse = getMSE(population[0]);
        population[0].setFitness(calcFitness(mse));
        bestMSE = mse;

        for (int i = 1; i < popSize; i++) {
            mse = getMSE(population[i]);
            population[i].setFitness(calcFitness(mse));
            if (mse < bestMSE)
                bestMSE = mse;
        }

        int iter = 1;
        int generation;

        // run iterations
        do {
            // 3-tournament selection
            ternaryTournamentSelection();

            System.out.println("Iteration " + iter + " >> bestMSE = " + bestMSE);

            iter++;
            generation = iter / popSize;
        } while (generation < maxGenerations && bestMSE > epsilon);

        Solution best = findBest(population);
        return best.getValues();
    }

    // GENETIC OPERATORS
    public void generateInitialPopulation() {
        population = new Solution[popSize];
        double[] values;

        double lowerLimit = -1.0, upperLimit = 1.0;
        Random random = new Random();

        for (int i = 0; i < popSize; i++) {
            values = new double[solutionLength];
            for (int j = 0; j < solutionLength; j++) {
                values[j] = random.nextDouble() * (upperLimit - lowerLimit) + (lowerLimit);
            }
            population[i] = new Solution(values);
        }
    }

    public double calcFitness(double error) {
        return 1.0 / (error + 1e-7);
    }

    public void ternaryTournamentSelection() {
        // choose 3 random solutions from the population
        Solution[] triple = new Solution[3];
        int[] threeIndices = new int[3];
        Random rand = new Random();
        double r;
        double upper;

        for (int i = 0; i < 3; i++) {
            r = rand.nextDouble();
            for (int j = 0; j < population.length; j++) {
                upper = (j + 1) * 1.0 / population.length;
                if (r < upper) {
                    triple[i] = population[j].copy();
                    threeIndices[i] = j;
                    break;
                }
            }
        }

        // two best parents produce a child by crossover
        Solution parent1 = findBest(triple);
        Solution parent2 = findBest(removeBest(triple));
        Solution child = makeCrossover(parent1, parent2);

        // mutate child
        child = mutate(child);

        // calculate child fitness
        double mse = getMSE(child);
        child.setFitness(calcFitness(mse));
        if (mse < bestMSE)
            bestMSE = mse;

        // new child takes the third parent's place
        Solution[] winners = {parent1, parent2, child};

        // return modified three solutions back into the population
        for (int i = 0; i < threeIndices.length; i++) {
            population[threeIndices[i]] = winners[i].copy();
        }
    }

    public Solution makeCrossover(Solution p1, Solution p2) {
        double alpha = 0.5;
        return CrossoverOperators.BLXAlpha(p1, p2, alpha);
    }

    public Solution mutate(Solution s) {
        Random random = new Random();
        float r = random.nextFloat();

        double limit = 0;
        int chosen = 0;

        for (int i = 0; i < vs.length; i++) {
            limit += vs[i];
            if (r < limit) {
                chosen = i;
                break;
            }
        }

        return mutationOperators[chosen].mutate(s, pms[chosen]);
    }

    public double getMSE(Solution s) {
        return nn.calcError(s.getValues(), dataset);
    }

    // TernaryTournament Methods
    public Solution findBest(Solution[] solutions) {
        int bestIndex = 0;
        double bestFitness = solutions[0].getFitness();
        for (int i = 1; i < solutions.length; i++) {
            if (solutions[i].getFitness() > bestFitness) {
                bestFitness = solutions[i].getFitness();
                bestIndex = i;
            }
        }
        return solutions[bestIndex].copy();
    }

    public Solution[] removeBest(Solution[] solutions) {
        Solution best = findBest(solutions);
        int toRemove = 0;
        for (int i = 0; i < solutions.length; i++) {
            if (solutions[i].equals(best)) {
                toRemove = i;
            }
        }
        Solution[] newSolutions = new Solution[solutions.length - 1];
        int k = 0;
        for (int i = 0; i < solutions.length; i++) {
            if (i != toRemove) newSolutions[k++] = solutions[i];
        }
        return newSolutions;
    }
}
