package de.heaal.eaf.geneticProgramming;

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

import static de.heaal.eaf.geneticProgramming.TrainFitness.*;

public class TrainControlsGeneticProgramming {
    public static final Double MAX_VELOCITY = 10.0;
    public static final Double DESTINATION = 1000.0;
    private static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL,
            MathOp.DIV,
            MathOp.MAX,
            MathOp.MIN,
            MathOp.POW
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

    public static void main(String[] args) throws InterruptedException {
        final Engine<ProgramGene<Double>, TrainFitness> engine = Engine
                .builder(TrainControlsGeneticProgramming::fitness, PROGRAM)
                .minimizing()
                .alterers(
                        new SingleNodeCrossover<>(),
                        new Mutator<>()
                )
                .build();

        final EvolutionResult<ProgramGene<Double>, TrainFitness> result = engine
                .stream()
                .limit(Limits.byFixedGeneration(1000))
//                .limit(Limits.byFitnessThreshold(new TrainFitness(0.0, 500.0, 500.0)))
                .collect(EvolutionResult.toBestEvolutionResult());

        final ProgramGene<Double> program = result.bestPhenotype()
                .genotype()
                .gene();

        final TreeNode<Op<Double>> tree = program.toTreeNode();
        MathExpr.rewrite(tree); // Simplify result program.
        MathExpr expr = new MathExpr(tree);
        System.out.println("Generations: " + result.totalGenerations());
        System.out.println("Function:    " + expr);
        System.out.println(result.bestFitness());
        System.out.println("\n");

        while (true) {
            makeSampleTrainRide(expr);
        }
    }

    private static void makeSampleTrainRide(MathExpr expr) throws InterruptedException {
        double s = 0.0;
        double t = 0.0;
        double v = 0.0;
        System.out.print("\r" + getRouteString(s));
        Thread.sleep(50);
        do {
            double a = expr.eval(s, v, t);
            t += STEP;
            s = getDistance(a, v, s);
            v = getVelocity(a, v);
            System.out.print("\r" + getRouteString(s));
            Thread.sleep(100);
        } while (s < DESTINATION && v > 0.0 && t < MAX_TIME && v <= MAX_VELOCITY);

        System.out.println("\n Final Velocity: " + v);
    }
    private static String train = "T";
    private static int routeSize = 100;
    private static String getRouteString(double s) {
        int pos = (int) ((s / DESTINATION) * routeSize);
        StringBuilder routeString = new StringBuilder();
        routeString.append("S");

        for (int i = 0; i <= routeSize || i <= pos; i++) {
            if (i == pos) {
                routeString.append(train);
            }
            else if (i == routeSize)  {
                routeString.append("H");
            }
            else {
                routeString.append("_");
            }
        }

        return routeString.toString();
    }
}
