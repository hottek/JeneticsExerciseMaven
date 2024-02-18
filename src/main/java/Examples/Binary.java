package Examples;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

public class Binary {

    // fitness function
    // Takes a Genotype as argument and returns a fitness value, that can then be either minimized of maximized.
    // The returned fitness value must implement the Comparable interface to enable Selection of offspring- and survivor populations
    // Genotype must first be transformed into an object of the problem domain and then this object is used to calculate
    // the fitness value.
    // Here: It's simply the bitCount.
    private static Integer fitness(Genotype<BitGene> gt) {
        return gt.chromosome().as(BitChromosome.class).bitCount();
    }

    public static void main(String[] args) {

        // Factories know how to create new random instances of the same type
        // Every Gene and every Chromosome is its own Factory (as in "implements Factory")
        // Here: Creation of Genotype consisting of one random BitChromosome (Genes are Bits) of length 16. Chance of '1' is 15%.
        Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(16, 0.15));
        System.out.println(gtf);

        // Evolution Engine controls how the evolution is executed.
        // Needs fitness function and Chromosomes, that represent the problem encoding
        // additional parameters can be configured via method chaining.
        Engine<BitGene, Integer> builder = Engine.builder(Binary::fitness, gtf)
                .optimize(Optimize.MAXIMUM)
                .maximalPhenotypeAge(15)
                .populationSize(50)
                .alterers(
                        new Mutator<>(0.55),
                        new SinglePointCrossover<>(0.06)
                )
                .selector(
                        new RouletteWheelSelector<>()
                )
                .build();

        // stream() creates an EvolutionStream, which controls the evolution process. Termination Criteria is defined here
        // with limit()
        Genotype<BitGene> result = builder.stream().limit(500).collect(EvolutionResult.toBestGenotype());
        System.out.println(result);
    }
}

