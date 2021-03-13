package hr.fer.nenr.lab.hw4;

import java.util.Random;

public class GenerativeGA {
    private LossFunction lf;
    private BitVectorDecoder decoder;
    int popSize;
    double pm;
    Random rand;
    int generationLimit;
    private boolean elitism;

    public GenerativeGA(LossFunction lf, BitVectorDecoder decoder, int popSize, double pm, Random rand,
                        int generationLimit, boolean elitism) {
        this.lf = lf;
        this.decoder = decoder;
        this.popSize = popSize;
        this.pm = pm;
        this.rand = rand;
        this.generationLimit = generationLimit;
        this.elitism = elitism;
    }

    public double[] run() {
        BitVector[] population = EvoUtil.initializePopulation(this.popSize, this.rand, this.decoder);

        // set initial fitness
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness(lf.calculateFitness(decoder.decode(population[i])));
            System.out.println(population[i].getFitness());
        }

        BitVector previousBest = EvoUtil.findBest(population);
        BitVector currentBest;

        // iterate until generationLimit reached
        for (int generation = 1; generation <= this.generationLimit; generation++) {
            // new generation
            BitVector[] nextPopulation = new BitVector[this.popSize];

            int elitism_dependant_index = 0;
            // move the best solution into the next population (if elitism is TRUE)
            if (this.elitism) {
                BitVector best = EvoUtil.findBest(population);
                nextPopulation[0] = best;
                elitism_dependant_index = 1;
            }

            // depending on elitism, generate remaining new solutions
            for (int i = elitism_dependant_index; i < this.popSize; i++) {
                BitVector[] parents = EvoUtil.proportionalSimpleChoose(population, this.rand, 2);
                BitVector child = EvoUtil.uniformCrossover(parents[0], parents[1], this.rand);
                EvoUtil.binaryMutate(child, this.rand, this.pm);
                child.setFitness(lf.calculateFitness(decoder.decode(child)));
                nextPopulation[i] = child;
            }

            // current population becomes the new one
            population = nextPopulation;

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
