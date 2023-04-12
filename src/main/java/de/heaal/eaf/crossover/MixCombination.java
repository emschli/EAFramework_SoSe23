package de.heaal.eaf.crossover;

import de.heaal.eaf.base.Individual;

import java.util.Random;

public class MixCombination <T extends Individual> implements Combination<T> {
    protected Random rng;

    @Override
    public void setRandom(Random rng) {
        this.rng = rng;
    }

    @Override
    public T combine(Individual[] parents) {
        int dim = parents[0].getGenome().len();

        T child = (T)parents[0].copy();
        child.mul(0.5f);
        T otherParent = (T)parents[1].copy();
        otherParent.mul(0.5f);

        child.add(otherParent);

        return child;
    }
}
