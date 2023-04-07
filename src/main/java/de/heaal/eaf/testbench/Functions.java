package de.heaal.eaf.testbench;

import de.heaal.eaf.base.Individual;

import java.util.function.Function;

public class Functions {
    public static Function<Individual, Float> SPHERE_2D = (ind) -> {
        var x0 = ind.getGenome().array()[0];
        var x1 = ind.getGenome().array()[1];
        return x0*x0 + x1*x1;
    };

    public static Function<Individual, Float> ACKLEY = (ind) -> {
        var x = ind.getGenome().array()[0];
        var y = ind.getGenome().array()[1];

        return (float) (-20.0f * Math.exp(-0.2* Math.sqrt(0.5 * (Math.pow(x, 2) + Math.pow(y, 2)) ))
                - Math.exp(0.5 * (Math.cos(2*Math.PI*x) + Math.cos(2*Math.PI*y)) ) + Math.E + 20);
    };
}
