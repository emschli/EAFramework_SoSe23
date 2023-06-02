package de.heaal.eaf.geneticProgramming;

import io.jenetics.prog.op.MathExpr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static de.heaal.eaf.geneticProgramming.TrainControlsGeneticProgramming.MAX_VELOCITY;

public class TrainFitness implements Comparable<TrainFitness> {
    static Double STEP = 1.0;
    static Double MAX_TIME = 1000.0;
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

    private static Double X = 1.0;
    private static Double Y = 1.0;
    private static Double Z = 10.0;

    private static Comparator<TrainFitness> COMPARE_ALL = (f1, f2) -> {
      double value1 = X * f1.timeTraveled + Y * f1.energyUsed + Math.pow(f1.distanceToDestination, 2);
      double value2 = X * f2.timeTraveled + Y * f2.energyUsed + Math.pow(f2.distanceToDestination, 2);
      return Double.compare(value1, value2);
    };

    private static Comparator<TrainFitness> ALTERNATIVE =
        Comparator.comparing(TrainFitness::getDistanceToDestination)
                .thenComparing(TrainFitness::getTimeTraveled)
                .thenComparing(TrainFitness::getEnergyUsed);

    private Double distanceToDestination;
    private Double timeTraveled;
    private Double energyUsed;
    private Comparator<TrainFitness> comparator = COMPARE_ALL;

    public TrainFitness(Double distanceToDestination, Double timeTraveled, Double energyUsed) {
        this.distanceToDestination = distanceToDestination;
        this.timeTraveled = timeTraveled;
        this.energyUsed = energyUsed;
    }

    public static TrainFitness calculateFitness(MathExpr expr) {
        List<Double> velocityOverTime = new ArrayList<>();

        double s = 0.0;
        double t = 0.0;
        double v = 0.0;
        velocityOverTime.add(v);

        do {
            double a = expr.eval(s, v, t);
            t += STEP;
            s = getDistance(a, v, s);
            v = getVelocity(a, v);
            velocityOverTime.add(v);
        } while (v > 0.0 && t < MAX_TIME && v <= MAX_VELOCITY);

        if (v > MAX_VELOCITY) {
            return new TrainFitness(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        }

        double distanceToDestination = Math.abs(TrainControlsGeneticProgramming.DESTINATION - s);
        double timeTraveled = t;
        double energyUsed = calculatePowerConsumption(velocityOverTime);

        return new TrainFitness(distanceToDestination, timeTraveled, energyUsed);
    }

    private static double calculatePowerConsumption(List<Double> velocities) {
        double sum = 0.0;
        for (int i = 1; i < velocities.size(); i++) {
            double v_n = velocities.get(i);
            double v_n_before = velocities.get(i-1);
            sum += (Math.pow(v_n_before, 2) + Math.pow(v_n, 2));
        }

        return 0.5 * STEP * sum;
    }

    static double getDistance(double a, double v, double s) {
        return 0.5 * a * Math.pow(STEP, 2) + v * STEP + s;
    }

    static double getVelocity(double a, double v) {
        return a * STEP + v;
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

    @Override
    public String toString() {
        return "TrainFitness{" +
                "distanceToDestination=" + distanceToDestination +
                ", timeTraveled=" + timeTraveled +
                ", energyUsed=" + energyUsed +
                '}';
    }
}
