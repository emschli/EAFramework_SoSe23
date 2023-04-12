package de.heaal.eaf.serialization;

import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.Population;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToCSV {
    private final PrintWriter writer;

    public ToCSV(String filename){
        File csvOutput = new File(filename);
        try {
            writer = new PrintWriter(csvOutput);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cant create CSV file");
        }
    }

    public void writePopulationToFile(Population<Individual> population){
        String line = population.asList().stream().map((ind) -> {
            return Float.toString(ind.getCache());
        }).collect(Collectors.joining(","));
        writer.println(line);
    }

    public void flush(){
        writer.flush();
    }
}
