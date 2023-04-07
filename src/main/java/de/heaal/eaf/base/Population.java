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

package de.heaal.eaf.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class representing an algorithm's population.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 * @param <T>
 */
public class Population<T extends Individual> implements Iterable<T> {
    
    protected List<T> individuals;
    
    /**
     * Create and initialize this population with num individuals using the
     * given factory.
     * 
     * @param iFak
     * @param num 
     */
    public Population(IndividualFactory<T> iFak, int num) {
        individuals = (List<T>) Stream.generate(() -> iFak.create())
                            .limit(num)
                            .collect(Collectors.toList());
    }
    
    /**
     * Create an empty population.The internal list is constructed using 
     * the initial capacity.
     * 
     * @param initialCapacity
     */
    public Population(int initialCapacity) {
        individuals = new ArrayList<>(initialCapacity);
    }
    
    public void add(T ind) {
        individuals.add(ind);
    }
    
    public T get(int idx) {
        return individuals.get(idx);
    }
    
    public <T> T get(int idx, Class<T> clazz) {
        return (T)get(idx);
    }
    
    /**
     * Returns a copy of this population individuals list.
     * 
     * @return Flat-copy of the individuals list. 
     */
    public List<T> asList() {
        return new ArrayList<>(individuals);
    }
    
    public void set(int idx, T ind) {
        individuals.set(idx, ind);
    }
    
    public int size() {
        return individuals.size();
    }
    
    /**
     * Should be called by the algorithm when a new generation is about to start.
     * Resets the evaluation cache for every individual.
     */
    public void nextGeneration() {
        individuals.forEach((ind) -> ind.clearCache());
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        individuals.forEach(action);
    }

    @Override
    public Iterator<T> iterator() {
        return individuals.iterator();
    }
    
    public void sort(Comparator<T> cmp) {
        // This should sort descending
        individuals.sort(cmp.reversed());
    }
    
    /**
     * Filters the population with the given predicate and returns the
     * result as List of T.
     * 
     * @param prdct
     * @return 
     */
    public List<T> filter(Predicate<T> prdct) {
        return individuals.stream()
                .filter(prdct).collect(Collectors.toList());
    }

    public void replace(List<T> newPopulation) {
        individuals = newPopulation;
    }
}
