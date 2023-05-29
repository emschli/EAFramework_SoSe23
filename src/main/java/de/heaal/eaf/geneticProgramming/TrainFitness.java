package de.heaal.eaf.geneticProgramming;

import io.jenetics.prog.op.MathExpr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static de.heaal.eaf.geneticProgramming.TrainControlsGeneticProgramming.MAX_VELOCITY;
import static de.heaal.eaf.geneticProgramming.TrainControlsGeneticProgramming.DESTINATION;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class TrainFitness implements Comparable<TrainFitness> {
    static Double STEP = 1.0;
    static Double MAX_TIME = 100_000.0;
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
    private static Double Z = 1.0;

    private static Comparator<TrainFitness> COMPARE_ALL = (f1, f2) -> {
      double value1 = X * f1.timeTraveled + Y * f1.energyUsed + Z * f1.distanceToDestination;
      double value2 = X * f2.timeTraveled + Y * f2.energyUsed + Z * f2.distanceToDestination;
      return Double.compare(value1, value2);
    };
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
            s = getDistance(a, t, v, s);
            v = getVelocity(a, t, v);
            velocityOverTime.add(v);
        } while (s < TrainControlsGeneticProgramming.DESTINATION && v > 0.0 && t < MAX_TIME && v <= MAX_VELOCITY);

        if (t >= MAX_TIME || v > MAX_VELOCITY) {
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

    static double getDistance(double a, double t, double v, double s) {
        return 0.5 * a * Math.pow(t, 2) + v * t + s;
    }

    static double getVelocity(double a, double t, double v) {
        return a * t + v;
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
