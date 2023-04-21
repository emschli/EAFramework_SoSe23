package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.selection.SelectionUtils;

import java.io.IOException;
import java.util.*;

public class GeneticAlgorithm extends Algorithm {
    private final IndividualFactory individualFactory;
    private final ComparatorIndividual comparatorIndividual;
    private final MutationOptions mutationOptions;
    private int generationCounter;
    private final int numberOfGenerations;
    private final boolean onlyQuitIfFound;
    private final Combination<Individual> combinator;

    //
    private final int populationSize;
    private final int numberOfElites;

    public GeneticAlgorithm(float[] min,
                            float[] max,
                            Comparator<Individual> comparator,
                            Mutation mutator,
                            ComparatorIndividual comparatorIndividual,
                            int populationSize,
                            Combination<Individual> combinator,
                            MutationOptions mutationOptions,
                            int numberOfGenerations,
                            boolean onlyQuitIfFound,
                            int numberOfElites) {
        super(comparator, mutator);
        this.numberOfGenerations = numberOfGenerations;
        this.onlyQuitIfFound = onlyQuitIfFound;
        this.numberOfElites = numberOfElites;
        this.individualFactory = new GenericIndividualFactory(min, max);
        this.comparatorIndividual = comparatorIndividual;
        this.generationCounter = 0;
        this.populationSize = populationSize;
        this.combinator = combinator;
        this.mutationOptions = mutationOptions;
    }

    @Override
    public void nextGeneration() {
        super.nextGeneration();
        population.sort(comparator);

        List<Individual> elites = population.asList().subList(0, numberOfElites);
        List<Individual> children = new ArrayList<>(elites);

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
        if (!onlyQuitIfFound) {
            return generationCounter == numberOfGenerations;
        } else {
            Individual best = population.get(0);
            return comparator.compare(best, comparatorIndividual) > 0;
        }
    }

    @Override
    public void run() throws IOException {
        initialize(individualFactory, populationSize);
        population.sort(comparator);

        while (!isTerminationCondition()) {
            generationCounter++;
            System.out.println("Generation " + generationCounter);
            nextGeneration();
            if (!onlyQuitIfFound) {
               writeGeneration();
            }
        }

        System.out.println("Done!");
        System.out.println("Found Minimum at: " + population.get(0).getGenome());

        closeWriter();
    }
}
