package Examples;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;

public class BinaryLambda {

    public static void main(String[] args) {

        // Same as in Binary.java, but shorter. :)
        Engine<BitGene, Integer> builder = Engine
                .builder(
                        gt -> gt.chromosome().as(BitChromosome.class).bitCount(),
                        BitChromosome.of(16, 0.15)
                )
                .optimize(Optimize.MAXIMUM)
                .maximalPhenotypeAge(15)
                .populationSize(50)
                .alterers(
                        new Mutator<>(0.5),
                        new SinglePointCrossover<>(0.5)
                )
                .selector(
                        new TournamentSelector<>()
                )
                .build();

        Phenotype<BitGene, Integer> result = builder
                .stream()
                .limit(500)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(result);

    }
}