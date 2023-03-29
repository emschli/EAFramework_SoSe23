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

package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.GenericIndividualFactory;
import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.IndividualFactory;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of the Hill Climbing algorithm.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class HillClimbingAlgorithm extends Algorithm {

    private final IndividualFactory indFac;
    private final ComparatorIndividual terminationCriterion;

    private final MutationOptions options;
    private int generationCounter;
    
    public HillClimbingAlgorithm(float[] min, float[] max, 
            Comparator<Individual> comparator, Mutation mutator, 
            ComparatorIndividual terminationCriterion) 
    {
        super(comparator, mutator);
        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.options = new MutationOptions();
        options.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 1.0f);
        this.generationCounter = 0;
    }
    
    @Override
    public void nextGeneration() {
        super.nextGeneration();

        Individual x0 = population.get(0);
        Individual x1 = x0.copy();

        mutator.mutate(x1, options);

        if (comparator.compare(x1, x0) > 0) {
            population.set(0, x1);
        }

    }
  
    @Override
    public boolean isTerminationCondition() {
        // Because we only have a population of 1 individual we know that
        // this individual is our current best.
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public void run() {
        initialize(indFac, 1);
        generationCounter = 0;
        
        while(!isTerminationCondition()) {
            generationCounter++;
            System.out.println("Generation " + generationCounter);
            nextGeneration();
        }

        System.out.println("Done!");
        System.out.println("Found Minimum at: " + population.get(0).getGenome());
    }

}
