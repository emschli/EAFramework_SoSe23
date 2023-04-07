package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.selection.SelectionUtils;

import java.util.*;

public class GeneticAlgorithm extends Algorithm<Individual> {
    private final IndividualFactory<Individual> individualFactory;
    private final ComparatorIndividual comparatorIndividual;
    private final int populationSize;
    private int generationCounter;
    private final Combination<Individual> combinator;
    private final MutationOptions mutationOptions;

    public GeneticAlgorithm(float[] min,
                            float[] max,
                            Comparator<Individual> comparator,
                            Mutation mutator,
                            ComparatorIndividual comparatorIndividual,
                            int populationSize,
                            Combination<Individual> combinator,
                            MutationOptions mutationOptions) {
        super(comparator, mutator);
        this.individualFactory = new GenericIndividualFactory<>(min, max);
        this.comparatorIndividual = comparatorIndividual;
        this.generationCounter = 0;
        this.populationSize = populationSize;
        this.combinator = combinator;
        this.mutationOptions = mutationOptions;
    }

    @Override
    public void nextGeneration() {
        super.nextGeneration();
        List<Individual> children = new ArrayList<>();

        while (children.size() < population.size())  {
            Individual parent1 = SelectionUtils.selectNormal(population, rng, null);
            Individual parent2 = SelectionUtils.selectNormal(population, rng, parent1);
            Individual[] parents = new Individual[] { parent1, parent2 };
            Individual child1 = combinator.combine(parents);
            Individual child2 = combinator.combine(parents);
            children.add(child1);
            children.add(child2);
        }

        for (Individual child : children) {
            mutator.mutate(child, mutationOptions);
        }

        population.replace(children);
        population.sort(comparator);
    }

    @Override
    protected boolean isTerminationCondition() {
        Individual best = population.get(0);
        return comparator.compare(best, comparatorIndividual) > 0;
    }

    @Override
    protected void run() {
        initialize(individualFactory, populationSize);
        population.sort(comparator);

        while (!isTerminationCondition()) {
            generationCounter++;
            System.out.println("Generation " + generationCounter);
            nextGeneration();
        }

        System.out.println("Done!");
        System.out.println("Found Minimum at: " + population.get(0).getGenome());
    }
}
