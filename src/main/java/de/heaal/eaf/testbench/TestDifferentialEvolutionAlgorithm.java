package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.DifferentialEvolutionAlgorithm;
import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.DifferentialComparator;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;

import java.io.IOException;
import java.util.Comparator;

import static de.heaal.eaf.testbench.Functions.ACKLEY;

public class TestDifferentialEvolutionAlgorithm {
    private static final String BASEPATH = "/home/mirjam/Nextcloud/Uni/S7/Evolutionäre Algorithmen/Praktikum/code/EAFramework_SoSe23/src/main/resources/diff/";
    private static final int POPULATION_SIZE = 100;
    private static final int NUMBER_OF_GENERATIONS = 3000;
    private static final float COMPARATOR_INDIVIDUAL_VALUE = 0.001f;
    private static final boolean ONLY_QUIT_IF_FOUND = false;

    private static final float STEPSIZE = 0.4f; //0.4 oder 0.9
    private static final float CROSSOVER_RATE = 0.1f; //0.1 oder 1

    public static void main(String[] args) throws IOException {
        float[] min = new float[] {0.0f, 0.0f, (float) -Math.PI, 8f };
        float[] max = new float[] {+20.0f, 0.1f, (float) Math.PI, +12f };

        ComparatorIndividual comparatorIndividual = new ComparatorIndividual(COMPARATOR_INDIVIDUAL_VALUE);
        Comparator<Individual> comparator = new DifferentialComparator();

        //Test
//        Comparator<Individual> comparatorAckley = new MinimizeFunctionComparator<>(ACKLEY);
//        float[] min_Test = new float[] {-5.12f, -5.12f };
//        float[] max_Test = new float[] {+5.12f, +5.12f };
//        Algorithm test = new DifferentialEvolutionAlgorithm(
//                min_Test,
//                max_Test,
//                comparatorAckley,
//                comparatorIndividual,
//                POPULATION_SIZE,
//                NUMBER_OF_GENERATIONS,
//                ONLY_QUIT_IF_FOUND,
//                STEPSIZE,
//                CROSSOVER_RATE);
//
//        test.setGenerationWriter(BASEPATH + "diff_ackley.csv");
//        test.run();
        // Ende Test

        Algorithm algorithm = new DifferentialEvolutionAlgorithm(
                min,
                max,
                comparator,
                comparatorIndividual,
                POPULATION_SIZE,
                NUMBER_OF_GENERATIONS,
                ONLY_QUIT_IF_FOUND,
                0.9f, //0.4
                0.1f); //1

        algorithm.setGenerationWriter(BASEPATH + "first.csv");
        algorithm.run();
    }
}
