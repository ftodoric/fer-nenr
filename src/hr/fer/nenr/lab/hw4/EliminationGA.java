package hr.fer.nenr.lab.hw4;

import java.util.Random;

public class EliminationGA {
    private final LossFunction lf;
    private final BitVectorDecoder decoder;
    int popSize;
    double pm;
    Random rand;
    int generationLimit;

    public EliminationGA(LossFunction lf, BitVectorDecoder decoder, int popSize, double pm,
                         Random rand, int generationLimit) {
        this.lf = lf;
        this.decoder = decoder;
        this.popSize = popSize;
        this.pm = pm;
        this.rand = rand;
        this.generationLimit = generationLimit;
    }

    public double[] run() {
        BitVector[] population = EvoUtil.initializePopulation(this.popSize, this.rand, this.decoder);

        // set initial fitness
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness(lf.calculateFitness(decoder.decode(population[i])));
            System.out.println(population[i]);
            System.out.println(population[i].getFitness());
        }

        BitVector previousBest = EvoUtil.findBest(population);
        BitVector currentBest;

        // iterate until generationLimit reached
        for (int generation = 1; generation <= this.generationLimit; generation++) {
            // generate next population
            // triple-elimination tournament
            EvoUtil.tripleTournamentSelection(population, lf, decoder, this.rand, pm);

            // if the best is new best -> print generation number, best and best's fitness
            currentBest = EvoUtil.findBest(population);
            if (!currentBest.equals(previousBest)) {
                System.out.print(generation + ". GENERATION:\nNew best:\t\t\t");
                currentBest.printDecoded(decoder);
                System.out.println("Mean square error:\t"
                        + lf.meanSquareError(decoder.decode(currentBest)) + "\n");
            }
            previousBest = currentBest.copy();
        }

        return decoder.decode(EvoUtil.findBest(population));
    }
}
