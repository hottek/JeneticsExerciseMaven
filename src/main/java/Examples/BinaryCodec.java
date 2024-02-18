package Examples;

import io.jenetics.*;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

import java.util.function.Function;

public class BinaryCodec {

    // Codecs narrow the gap between the fitness function and the Genotype representation
    private static final Codec<Integer, BitGene> onesCountingCodec = new Codec<>() {
        // encoding returns Genotype Factory
        @Override
        public Factory<Genotype<BitGene>> encoding() {
            return () -> Genotype.of(BitChromosome.of(16, 0.15));
        }

        // decoder return Function, which transforms the Genotype into the argument type of the fitness function
        @Override
        public Function<Genotype<BitGene>, Integer> decoder() {
            return chromosomes -> chromosomes.chromosome().as(BitChromosome.class).bitCount();
        }
    };

    // Because we use a Codec, we don't need to decode the Genotype in the fitness function and use the already decoded
    // object of the problem domain directly.
    // Here: decoded value (the bitCount of the BitChromosome) already is the fitness value. Easy example.
    private static int fitness(int x) {
        return x;
    }

    public static void main(String[] args) {

        // Same as in Binary.java. Only difference is, that we build the engine using the Codec and the new fitness function.
        Engine<BitGene, Integer> builder = Engine
                .builder(BinaryCodec::fitness, onesCountingCodec)
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