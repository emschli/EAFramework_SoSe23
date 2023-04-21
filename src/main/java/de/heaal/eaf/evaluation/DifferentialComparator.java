package de.heaal.eaf.evaluation;

import de.heaal.eaf.base.Individual;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DifferentialComparator implements Comparator<Individual> {
    private static final String PATH_TO_CSV = "/home/mirjam/Nextcloud/Uni/S7/Evolutionäre Algorithmen/Praktikum/code/EAFramework_SoSe23/src/main/resources/sensordata.csv";
    private final List<Float> values;
    public DifferentialComparator() throws IOException {
        this.values = readMeasurementsFromFile();
    }

    private List<Float> readMeasurementsFromFile() throws IOException {
        List<Float> result = new ArrayList<>();
        FileReader fileReader = new FileReader(PATH_TO_CSV);
        BufferedReader reader = new BufferedReader(fileReader);

        reader.readLine();
        String line = reader.readLine();
        int count = 0;
        while (line != null) {
            if (count >= 1000) break;
            Float value = Float.parseFloat(line.split(";")[4].replace(",", "."));
            result.add(value);
            line = reader.readLine();
            count++;
        }

        reader.close();

        return result;
    }

    private float getFunctionValue(float a, float f, float t, float phi, float d) {
        return (float) (a * Math.sin(2.0f*Math.PI * f * t + phi) + d);
    }

    private float getFitnessForIndividual(Individual individual) {
        float a = individual.getGenome().array()[0];
        float f = individual.getGenome().array()[1];
        float phi = individual.getGenome().array()[2];
        float d = individual.getGenome().array()[3];

        float sum = 0;
        for (int t = 0; t < values.size(); t++) {
            float actualValue = values.get(t);
            float functionValue = getFunctionValue(a, f, t, phi, d);
            sum+= Math.pow((actualValue - functionValue), 2);
        }

        return sum;
    }
    @Override
    public int compare(Individual i0, Individual i1) {
        float ev0 = i0.hasCache() ? i0.getCache() : getFitnessForIndividual(i0);
        float ev1 = i1.hasCache() ? i1.getCache() : getFitnessForIndividual(i1);

        i0.setCache(ev0);
        i1.setCache(ev1);

        if (ev0 < ev1) { // because smaller is better for a minimization problem
            return 1;
        } else if (ev1 < ev0) {
            return -1;
        } else {
            return 0;
        }
    }
}
