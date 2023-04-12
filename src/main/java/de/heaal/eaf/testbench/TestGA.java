package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.GeneticAlgorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.crossover.MixCombination;
import de.heaal.eaf.crossover.SinglePointCrossover;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.RandomMutation;
import de.heaal.eaf.serialization.ToCSV;

import java.util.Random;
import java.util.function.Function;

public class TestGA {
    private static void runAlgo(Function<Individual, Float> eval){
        float[] min = {-5.12f, -5.12f};
        float[] max = {+5.12f, +5.12f};

        var comparator = new MinimizeFunctionComparator(eval);
        var rng = new Random();
        var toCSV = new ToCSV("ga_out.csv");

        var algo = new GeneticAlgorithm(
                min, max, comparator,
                new RandomMutation(min, max),
                new ComparatorIndividual(0.0f),
                new SinglePointCrossover(),
                rng, 10, toCSV);
        algo.run();
    }

    public static void main(String[] args) {
        runAlgo(TestUtils.evalSphere());
    }
}
