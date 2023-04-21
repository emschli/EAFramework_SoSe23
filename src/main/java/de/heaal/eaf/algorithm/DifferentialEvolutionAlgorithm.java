package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.selection.SelectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DifferentialEvolutionAlgorithm extends Algorithm {
    private final IndividualFactory individualFactory;
    private final ComparatorIndividual comparatorIndividual;
    private int generationCounter;
    private final int numberOfGenerations;
    private final boolean onlyQuitIfFound;

    private float stepSizeParameter;
    private float crossOverRate;

    //
    private final int populationSize;

    public DifferentialEvolutionAlgorithm(float[] min,
                                          float[] max,
                                          Comparator<Individual> comparator,
                                          ComparatorIndividual comparatorIndividual,
                                          int populationSize,
                                          int numberOfGenerations,
                                          boolean onlyQuitIfFound,
                                          float stepSizeParameter,
                                          float crossOverRate) {
        super(comparator);
        this.stepSizeParameter = stepSizeParameter;
        this.numberOfGenerations = numberOfGenerations;
        this.onlyQuitIfFound = onlyQuitIfFound;
        this.crossOverRate = crossOverRate;
        this.individualFactory = new GenericIndividualFactory(min, max);
        this.comparatorIndividual = comparatorIndividual;
        this.generationCounter = 0;
        this.populationSize = populationSize;
    }
    @Override
    protected boolean isTerminationCondition() {
        if (!onlyQuitIfFound) {
            return generationCounter == numberOfGenerations;
        } else {
            Individual best = population.get(0);
            return comparator.compare(best, comparatorIndividual) > 0;
        }
    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();
        List<Individual> children = new ArrayList<>();

        for (Individual x : population) {
            Individual r1 = SelectionUtils.selectUniform(population, rng, new Individual[] {x});
            Individual r2 = SelectionUtils.selectUniform(population, rng, new Individual[] {x, r1});
            Individual r3 = SelectionUtils.selectUniform(population, rng, new Individual[] {x, r1, r2});

            VecN mutantVector = r1.getGenome().add((r2.getGenome().sub(r3.getGenome()).mul(stepSizeParameter)));
            VecN trialVector = mutate(x.getGenome(), mutantVector);
            Individual child = new GenericIndividual(trialVector);

            if (comparator.compare(child, x) > 0) {
                children.add(child);
            } else {
                children.add(x);
            }
        }
        population.replace(children);
    }

    @Override
    public void run() throws IOException {
        initialize(individualFactory, populationSize);

        while (!isTerminationCondition()) {
            generationCounter++;
            System.out.println("Generation " + generationCounter);
            nextGeneration();
            if (!onlyQuitIfFound) {
                writeGeneration();
            }
        }

        population.sort(comparator);
        System.out.println("Done!");
        System.out.println("Found Minimum at: " + population.get(0).getGenome());

        closeWriter();
    }

    private VecN mutate(VecN x, VecN v) {
        VecN u = x.copy();
        int dimensionCount = u.len();

        int safeMutationIndex = rng.nextInt(0, dimensionCount);

        for (int i = 0; i < dimensionCount; i++) {
            float random = rng.nextFloat();

            if (random < crossOverRate || i == safeMutationIndex) {
                u.array()[i] = v.array()[i];
            }
        }

        return u;
    }
}
