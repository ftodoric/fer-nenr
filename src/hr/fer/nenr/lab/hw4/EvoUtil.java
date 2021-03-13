package hr.fer.nenr.lab.hw4;

import java.util.Random;

public class EvoUtil {
    public static BitVector[] initializePopulation(int popSize, Random rand, BitVectorDecoder decoder) {
        BitVector[] initPop = new BitVector[popSize];
        int[] bits = new int[decoder.getM_tot()];
        BitVector temp;
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < decoder.getM_tot(); j++) {
                double r = rand.nextDouble();
                bits[j] = r < 0.5 ? 0 : 1;
            }
            temp = new BitVector(bits);
            initPop[i] = temp.copy();
        }

        return initPop;
    }

    public static BitVector findBest(BitVector[] population) {
        // the higher the fitness the better the solution

        BitVector best = population[0];
        double bestFitness = population[0].getFitness();

        for (BitVector solution : population) {
            if (solution.getFitness() > bestFitness) {
                best = solution;
                bestFitness = solution.getFitness();
            }
        }

        return best;
    }

    // SELECTION OPERATORS
    public static BitVector[] proportionalSimpleChoose(BitVector[] population, Random rand, int howMany) {
        BitVector[] parents = new BitVector[howMany];

        // calculate sum of all fitness
        double sum = 0;
        for (int i = 0; i < population.length; i++) {
            sum += population[i].getFitness();
        }

        // for howMany number of parents
        for (int parentIndex = 0; parentIndex < howMany; parentIndex++) {
            // generate random number from [0, sum]
            double r = rand.nextDouble();
            double limit = r * sum;
            // find a solution which corresponding to the limit
            int chosen = 0;
            double upperLimit = population[chosen].getFitness();
            while (limit > upperLimit && chosen < population.length - 1) {
                chosen++;
                upperLimit += population[chosen].getFitness();
            }
            parents[parentIndex] = population[chosen].copy();
        }

        return parents;
    }

    public static void tripleTournamentSelection(BitVector[] population, LossFunction lf, BitVectorDecoder decoder,
                                                 Random rand, double pm) {
        // choose 3 random solutions from the population
        BitVector[] triple = new BitVector[3];
        int[] threeIndices = new int[3];
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
        BitVector parent1 = findBest(triple);
        BitVector parent2 = findBest(removeBest(triple));
        BitVector child = uniformCrossover(parent1, parent2, rand);
        child.setFitness(lf.calculateFitness(decoder.decode(child)));

        // mutate child
        binaryMutate(child, rand, pm);

        // new child takes the third parent's place
        BitVector[] winners = {parent1, parent2, child};

        // return modified three solutions back into the population
        for (int i = 0; i < threeIndices.length; i++) {
            population[threeIndices[i]] = winners[i].copy();
        }
    }

    // CROSSOVER OPERATORS
    public static BitVector uniformCrossover(BitVector parent1, BitVector parent2, Random rand) {
        int[] bits = new int[parent1.getLength()];

        for (int i = 0; i < parent1.getLength(); i++) {
            bits[i] = rand.nextFloat() < 0.5 ? parent1.getBit(i) : parent2.getBit(i);
        }

        return new BitVector(bits);
    }

    // MUTATION OPERATORS
    public static void binaryMutate(BitVector solution, Random rand, double pm) {
        float fpm = (float) pm;
        for (int i = 0; i < solution.getLength(); i++) {
            if (rand.nextFloat() < fpm) {
                solution.setBit(i);
            }
        }
    }

    // UTILITY FUNCTIONS
    public boolean dEq(double a, double b) {
        double epsilon = 10e-4;
        return Math.abs(a - b) < epsilon;
    }

    public static BitVector[] removeBest(BitVector[] population) {
        BitVector best = EvoUtil.findBest(population);
        int toRemove = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].equals(best)) {
                toRemove = i;
            }
        }
        BitVector[] newPop = new BitVector[population.length - 1];
        int k = 0;
        for (int i = 0; i < population.length; i++) {
            if (i != toRemove) newPop[k++] = population[i];
        }
        return newPop;
    }
}
