package de.heaal.eaf.train;

import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.*;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Train {
    static final double MAX_SPEED = 10.0;
    static final double MAX_ACCELERATION = 1.0;
    static final double TRACK_LENGTH = 1000.0;
    static final double FRICTION = 0.95;
    static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL,
            MathOp.MAX
            //MathOp.ABS,
           // MathOp.MIN
    );

    static final ISeq<Op<Double>> TERMINALS = ISeq.of(
            Var.of("speed", 0),
            Var.of("distance", 1),
            Var.of("energy", 2),
            Const.of("max_speed", MAX_SPEED),
            Const.of("friction", FRICTION),
            EphemeralConst.of(() ->
                    (double) RandomRegistry.random().nextInt(11))
    );
    static final ProgramChromosome<Double> program =
            ProgramChromosome.of(10, OPERATIONS, TERMINALS);

    static boolean visualize = false;
    static Double trainSimulation(MathExpr expr){
        double distance = TRACK_LENGTH;
        double speed = 0.0;
        double energy = 0.0;

        double time = 0.0;

        do {
            time += 1.0;
            speed = Math.max(speed * FRICTION, 0.0);

            double acceleration = expr.eval(speed, distance, energy);
            speed += acceleration;
            speed = Math.min(speed, MAX_SPEED);

            distance -= speed;
            energy += Math.pow(Math.abs(acceleration), 2.0);

            if (visualize) visualize_train(distance, speed);
        } while (speed > 0.0 && time < 1000.0);

        if (visualize) System.out.printf("\n Distance: %f, energy: %f, time: %f\n", distance, energy, time);
        return Math.abs(Math.pow(distance, 2.0)) + energy + time;
    }

    static int VISUAL_TRACK_LENGTH = 100;
    static void visualize_train(double distance, double speed){
        try {
            Thread.sleep(50);
            long train_pos = Math.max(Math.round((distance / TRACK_LENGTH) * VISUAL_TRACK_LENGTH), 0);
            String start_to_train = Stream.generate(() -> "_").limit(VISUAL_TRACK_LENGTH - train_pos).collect(Collectors.joining());
            String train_to_station = Stream.generate(() -> "_").limit(train_pos).collect(Collectors.joining());
            String train = String.format("( )( )( )[%f]>", speed / MAX_SPEED);
            System.out.printf("\r Start>%s%s%s<Station", start_to_train, train, train_to_station);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static Double fitness(Genotype<ProgramGene<Double>> genotype) {
        ProgramGene<Double> gene = genotype.gene();
        TreeNode<Op<Double>> tree = gene.toTreeNode();
        MathExpr.rewrite(tree);
        MathExpr expr = new MathExpr(tree);

        return trainSimulation(expr);
    }
    static final ProgramChromosome<Double> PROGRAM = ProgramChromosome.of(5, OPERATIONS, TERMINALS);

    public static void main(String[] args) throws InterruptedException {
        final Engine<ProgramGene<Double>, Double> engine = Engine
                .builder(Train::fitness, PROGRAM)
                .minimizing()
                .alterers(
                        new SingleNodeCrossover<>(),
                        new Mutator<>()
                )
                .build();

        AtomicInteger count = new AtomicInteger();
        final EvolutionResult<ProgramGene<Double>, Double> result = engine
                .stream()
                .limit(Limits.byFixedGeneration(400))
                .peek((stuff) -> System.out.println("Generation: " + count.getAndIncrement()))
                .collect(EvolutionResult.toBestEvolutionResult());

        final ProgramGene<Double> program = result.bestPhenotype()
                .genotype()
                .gene();

        final TreeNode<Op<Double>> tree = program.toTreeNode();
        MathExpr.rewrite(tree);
        MathExpr expr = new MathExpr(tree);
        System.out.println("Generations: " + result.totalGenerations());
        System.out.println("Function:    " + expr);
        System.out.println(result.bestFitness());

        visualize = true;
        trainSimulation(expr);
    }
}
