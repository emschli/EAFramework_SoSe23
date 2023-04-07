/*
 * Evolutionary Algorithms Framework
 *
 * Copyright (c) 2023 Christian Lins <christian.lins@haw-hamburg.de>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.HillClimbingMutation;
import java.util.function.Function;

/**
 * Test bench for the Hill Climbing algorithm.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class TestHillClimbing {
    private static Function<Individual, Float> sphere2D = (ind) -> {
        var x0 = ind.getGenome().array()[0];
        var x1 = ind.getGenome().array()[1];
        return x0*x0 + x1*x1;
    };

    private static Function<Individual, Float> ackley = (ind) -> {
        var x = ind.getGenome().array()[0];
        var y = ind.getGenome().array()[1];

        return (float) (-20.0f * Math.exp(-0.2* Math.sqrt(0.5 * (Math.pow(x, 2) + Math.pow(y, 2)) ))
        - Math.exp(0.5 * (Math.cos(2*Math.PI*x) + Math.cos(2*Math.PI*y)) ) + Math.E + 20);
    };
    private static void doTheRun(Function<Individual, Float> function) {
        float[] min = {-5.12f, -5.12f};
        float[] max = {+5.12f, +5.12f};

        var comparator = new MinimizeFunctionComparator(function);

        var algo = new HillClimbingAlgorithm(min, max,
                comparator, new HillClimbingMutation(min, max), new ComparatorIndividual(0.001f));
        algo.run();
    }
    public static void main(String[] args) {
     // doTheRun(sphere2D);
        doTheRun(ackley);
    }
}
