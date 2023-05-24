package de.heaal.eaf.geneticProgramming;

import io.jenetics.prog.op.MathExpr;

import java.util.Comparator;

public class TrainFitness implements Comparable<TrainFitness> {
    private static Comparator<TrainFitness> COMPARE_ONLY_DISTANCE = (f1, f2) -> {
        double diff = f1.getDistanceToDestination() - f2.getDistanceToDestination();
        if(diff < 0) {
            return 1;
        } else if (diff > 0) {
            return -1;
        } else {
            return 0;
        }
    };
    private Double distanceToDestination;
    private Double timeTraveled;
    private Double energyUsed;
    private Comparator<TrainFitness> comparator = COMPARE_ONLY_DISTANCE;

    public TrainFitness(Double distanceToDestination, Double timeTraveled, Double energyUsed) {
        this.distanceToDestination = distanceToDestination;
        this.timeTraveled = timeTraveled;
        this.energyUsed = energyUsed;
    }

    public static TrainFitness calculateFitness(MathExpr expr) {
        double distanceToDestination = Double.MAX_VALUE;
        double timeTraveled = 0.0;
        double energyUsed = 0.0;

        return new TrainFitness(distanceToDestination, timeTraveled, energyUsed);
    }

    @Override
    public int compareTo(TrainFitness o) {
        return comparator.compare(this, o);
    }

    public Double getDistanceToDestination() {
        return distanceToDestination;
    }

    public Double getTimeTraveled() {
        return timeTraveled;
    }

    public Double getEnergyUsed() {
        return energyUsed;
    }
}
