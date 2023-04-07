package de.heaal.eaf.testbench;
import de.heaal.eaf.algorithm.GeneticAlgorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.crossover.MeanCrossover;
import de.heaal.eaf.crossover.SinglePointCrossover;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.mutation.SmallStepMutation;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

import static de.heaal.eaf.testbench.Functions.*;

public class TestGeneticAlgorithm {
    private static final String BASEPATH = "/home/mirjam/Nextcloud/Uni/S7/Evolutionäre Algorithmen/Praktikum/code/EAFramework_SoSe23/src/main/resources/evolution/";
    private static final int POPULATION_SIZE = 100;
    private static final int NUMBER_OF_GENERATIONS = 1000;
    private static final float COMPARATOR_INDIVIDUAL_VALUE = 0.001f;
    private static final boolean ONLY_QUIT_IF_FOUND = true;

    private final ComparatorIndividual comparatorIndividual;
    private final float[] min;
    private final float[] max;
    private final Mutation mutation;

    public TestGeneticAlgorithm() {
        this.comparatorIndividual = new ComparatorIndividual(COMPARATOR_INDIVIDUAL_VALUE);
        this.min = new float[]{-5.12f, -5.12f};
        this.max = new float[]{+5.12f, +5.12f};
        this.mutation = new SmallStepMutation(min, max);
    }

    public void doRun(String fileName,
                      Comparator<Individual> comparator,
                      Combination<Individual> crossover,
                      MutationOptions mutationOptions,
                      int numberOfElites) throws IOException{

        GenerationWriter writer = new GenerationWriter(BASEPATH + fileName);

        var algorithm = new GeneticAlgorithm(
                min,
                max,
                comparator,
                mutation,
                comparatorIndividual,
                POPULATION_SIZE,
                crossover,
                mutationOptions,
                writer,
                NUMBER_OF_GENERATIONS,
                ONLY_QUIT_IF_FOUND,
                numberOfElites);

        algorithm.run();
    }

    public void doRun(String fileName,
                      Comparator<Individual> comparator,
                      Combination<Individual> crossover,
                      MutationOptions mutationOptions) throws IOException {
        doRun(fileName, comparator, crossover, mutationOptions, 0);
    }

    public static void main(String[] args) throws IOException {

        Comparator<Individual> comparatorSphere2D = new MinimizeFunctionComparator<>(SPHERE_2D);
        Comparator<Individual> comparatorAckley = new MinimizeFunctionComparator<>(ACKLEY);

        Random random = new Random();
        Combination<Individual> singlePointCrossover = new SinglePointCrossover<>();
        singlePointCrossover.setRandom(random);
        Combination<Individual> meanCrossover = new MeanCrossover();
        meanCrossover.setRandom(random);

        MutationOptions mutationOptions1 = new MutationOptions();
        mutationOptions1.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 0.005f);
        MutationOptions mutationOptions2 = new MutationOptions();
        mutationOptions2.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 0.3f);

        TestGeneticAlgorithm test = new TestGeneticAlgorithm();

        //Rekombination
        //SinglePointCrossover & Sphere2D
        test.doRun(
                "GA_crossover_singlePoint.csv",
                comparatorSphere2D,
                singlePointCrossover,
                mutationOptions1
        );

        //MeanCrossover & Sphere 2D
        test.doRun(
                "GA_crossover_mean.csv",
                comparatorSphere2D,
                meanCrossover,
                mutationOptions1
        );

        //Elitism
        //SinglePointCrossover & Sphere2D
        test.doRun(
                "GA_crossover_singlePoint.csv",
                comparatorSphere2D,
                singlePointCrossover,
                mutationOptions1,
                1
        );
    }
}
