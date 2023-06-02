package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.selection.SelectionUtils;
import de.heaal.eaf.serialization.ToCSV;

import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm extends Algorithm {

    private static final int MAX_GENS = 1000;
    private final IndividualFactory indFac;
    private final ComparatorIndividual terminationCriterion;
    private final MutationOptions options;
    private final Combination combination;
    private final int elitism;
    private final ToCSV toCSV;
    private int gen;

    public GeneticAlgorithm(
            float[] min,
            float[] max,
            Comparator<Individual> comparator,
            Mutation mutator,
            ComparatorIndividual terminationCriterion,
            Combination combination,
            Random rng,
            int elitism,
            ToCSV toCSV)
    {
        super(comparator, mutator, rng);
        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.options = new MutationOptions();
        this.combination = combination;
        this.combination.setRandom(rng);
        options.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 0.3f);
        this.elitism = elitism;
        this.toCSV = toCSV;
        gen = 0;
    }

    @Override
    public void nextGeneration() {
        super.nextGeneration();

        //create next generation
        Population nextPopulation = new Population(population.size());
        //simply copy the amount of Parents specified by elitism
        for (int i = 0; i < elitism; i++) {
            nextPopulation.set(i, population.get(i));
        }
        //generate the rest of the children by combining 2 parents
        for (int i = elitism; i < population.size(); i++) {
            Individual parent1 = SelectionUtils.selectNormal(population, rng, null);
            Individual parent2 = SelectionUtils.selectNormal(population, rng, parent1);
            Individual child = combination.combine(new Individual[]{
                    parent1,
                    parent2
            });

            mutator.mutate(child, options);

            nextPopulation.set(i, child);
        }

        population = nextPopulation;

        //Sort population by fitness
        population.sort(comparator);
    }

    @Override
    public boolean isTerminationCondition() {
        // Because the population is sorted
        // the first individual is our current best.
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public void run() {
        initialize(indFac, 100);

        while (!isTerminationCondition() && gen < MAX_GENS) {
            System.out.println("Next gen: " + gen);
            nextGeneration();
            toCSV.writePopulationToFile(population);
            gen++;
        }

        toCSV.flush();
    }
}