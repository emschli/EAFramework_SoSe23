package de.heaal.eaf.testbench;

import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.Population;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class GenerationWriter {
    private String pathToFile;

    private BufferedWriter bufferedWriter;

    public GenerationWriter(String pathToFile) throws IOException {
        this.pathToFile = pathToFile;
        Path path = Path.of(pathToFile);
        Files.deleteIfExists(path);
        Files.createFile(path);
        FileWriter fileWriter = new FileWriter(pathToFile, true);
        this.bufferedWriter = new BufferedWriter(fileWriter);
    }

    public void writeGeneration(Population<Individual> population) throws IOException {
        int generationSize = population.size();
        int counter = 1;

        Iterator<Individual> iterator = population.iterator();
        while (iterator.hasNext() && counter < generationSize) {
            Individual individual = iterator.next();
            bufferedWriter.write(Float.toString(individual.getCache())+ " ");
            counter++;
        }

        //Write Last Individual
        Individual lastIndividual = iterator.next();
        bufferedWriter.write(Float.toString(lastIndividual.getCache()));

        bufferedWriter.newLine();
    }

    public void close() throws IOException{
        bufferedWriter.close();
    }

}
