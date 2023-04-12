package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.RandomMutation;

import java.util.function.Function;

public class TestUtils {
    public static Function<Individual, Float> evalSphere() {
        return (ind) -> {
            var x0 = ind.getGenome().array()[0];
            var x1 = ind.getGenome().array()[1];
            return x0*x0 + x1*x1;
        };
    }

    public static Function<Individual, Float> evalAckley() {
        return (ind) -> {
            var x = ind.getGenome().array()[0];
            var y = ind.getGenome().array()[1];
            return (float) (-20.0 * Math.exp(-0.2 * Math.sqrt(0.5 * (Math.pow(x, 2.0) + Math.pow(y, 2.0))))
                    - Math.exp(0.5 * (Math.cos(2.0 * Math.PI * x) + Math.cos(2.0 * Math.PI * y))) + Math.E + 20.0);

        };
    }
}
