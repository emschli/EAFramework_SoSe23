package de.heaal.eaf.crossover;

import de.heaal.eaf.base.Individual;

import java.util.Random;

public class MeanCrossover implements Combination<Individual> {
    protected Random rng;
    @Override
    public void setRandom(Random rng) {
        this.rng = rng;
    }

    @Override
    public Individual combine(Individual[] parents) {
        int dimensions = parents[0].getGenome().len();

        Individual child = parents[0].copy();
        for (int i = 0; i < dimensions; i++) {
            float sum = 0;
            for (int j = 0; j < parents.length; j++) {
                sum += parents[j].getGenome().array()[i];
            }
            float childValue = sum / parents.length;
            child.getGenome().array()[i] = childValue;
        }

        return child;
    }
}
