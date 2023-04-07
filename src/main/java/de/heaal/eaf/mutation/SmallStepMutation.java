package de.heaal.eaf.mutation;

import de.heaal.eaf.base.Individual;

import java.util.Random;

public class SmallStepMutation implements Mutation {
    private Random rng;
    private final float[] min;
    private final float[] max;

    public SmallStepMutation(float[] min, float[] max) {
        assert min.length == max.length;

        this.min = min;
        this.max = max;
    }

    @Override
    public void setRandom(Random rng) {
        this.rng = rng;
    }

    @Override
    public void mutate(Individual ind, MutationOptions opt) {
        float p = opt.get(MutationOptions.KEYS.MUTATION_PROBABILITY, 0.1f);
        if (p < rng.nextFloat()) {
            // Skip this individual
            return;
        }

        int dim = min.length;
        int i = opt.get(MutationOptions.KEYS.FEATURE_INDEX, rng.nextInt(dim));
        ind.getGenome().array()[i] += rng.nextFloat() * (0.1f - -0.1f) + -0.1f;
    }
}
