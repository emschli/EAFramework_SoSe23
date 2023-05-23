package de.heaal.eaf.vl;

import io.jenetics.Mutator;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.*;
import io.jenetics.prog.regression.Error;
import io.jenetics.prog.regression.LossFunction;
import io.jenetics.prog.regression.Regression;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.util.ArrayList;
import java.util.List;

public class GeneticProgramming {
    private static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL,
            MathOp.DIV
    );

    static final ISeq<Op<Double>> TERMINALS = ISeq.of(
            Var.of("x", 0),
            EphemeralConst.of(() ->
                    (double) RandomRegistry.random().nextInt(11))
    );

    private static final Regression<Double> REGRESSION = Regression.of(
            Regression.codecOf(OPERATIONS, TERMINALS, 5),
            Error.of(LossFunction::mse),
            generateSamples(-6, 6 , 0.25)
    );

    private static Double formula(Double x) {
        return Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x + 1;
    }

    private static final List<Sample<Double>> generateSamples(double start, double end, double step) {
        List<Sample<Double>> result = new ArrayList<>();
        for (double i = start; i <= end ; i+=step) {
            result.add(Sample.ofDouble(i, formula(i)));
        }
        return result;
    }

    public static void main(final String[] args) {
        final Engine<ProgramGene<Double>, Double> engine = Engine
                .builder(REGRESSION)
                .minimizing()
                .alterers(
                        new SingleNodeCrossover<>(0.9),
                        new Mutator<>(0.1))
                .build();

        final EvolutionResult<ProgramGene<Double>, Double> result = engine
                .stream()
                .limit(Limits.byFitnessThreshold(0.01))
                //.limit(Limits.byFixedGeneration(100))
                .collect(EvolutionResult.toBestEvolutionResult());

        final ProgramGene<Double> program = result.bestPhenotype()
                .genotype()
                .gene();

        final TreeNode<Op<Double>> tree = program.toTreeNode();
        MathExpr.rewrite(tree); // Simplify result program.
        System.out.println("Generations: " + result.totalGenerations());
        System.out.println("Function:    " + new MathExpr(tree));
        System.out.println("Error:       " + REGRESSION.error(tree));
    }
}
