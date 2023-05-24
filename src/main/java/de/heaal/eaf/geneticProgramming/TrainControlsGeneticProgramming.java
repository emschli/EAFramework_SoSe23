package de.heaal.eaf.geneticProgramming;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.engine.*;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.*;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

public class TrainControlsGeneticProgramming {
    private static final Double MAX_VELOCITY = 10.0;
    private static final Double DESTINATION = 10_000.0;
    private static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL,
            MathOp.DIV
    );

    static final ISeq<Op<Double>> TERMINALS = ISeq.of(
            Var.of("s", 0),
            Var.of("v", 1),
            Var.of("t", 2),
            Const.of("max_v", MAX_VELOCITY),
            Const.of("dest", DESTINATION),
            EphemeralConst.of(() ->
                    (double) RandomRegistry.random().nextInt(11))
    );

    static TrainFitness fitness(Genotype<ProgramGene<Double>> genotype) {
        ProgramGene<Double> gene = genotype.gene();
        TreeNode<Op<Double>> tree = gene.toTreeNode();
        MathExpr.rewrite(tree);
        MathExpr expr = new MathExpr(tree);
        return TrainFitness.calculateFitness(expr);
    }
    static final ProgramChromosome<Double> PROGRAM = ProgramChromosome.of(5, OPERATIONS, TERMINALS);

    public static void main(String[] args) {
        final Engine<ProgramGene<Double>, TrainFitness> engine = Engine
                .builder(TrainControlsGeneticProgramming::fitness, PROGRAM)
                .maximizing()
                .alterers(
                        new SingleNodeCrossover<>(),
                        new Mutator<>()
                )
                .build();

        final EvolutionResult<ProgramGene<Double>, TrainFitness> result = engine
                .stream()
//                .limit(Limits.byFitnessThreshold(0.01))
                .limit(Limits.byFixedGeneration(100))
                .collect(EvolutionResult.toBestEvolutionResult());

        final ProgramGene<Double> program = result.bestPhenotype()
                .genotype()
                .gene();

        final TreeNode<Op<Double>> tree = program.toTreeNode();
        MathExpr.rewrite(tree); // Simplify result program.
        System.out.println("Generations: " + result.totalGenerations());
        System.out.println("Function:    " + new MathExpr(tree));
    }
}
