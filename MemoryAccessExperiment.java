
import java.util.Arrays;
import java.util.Random;

public class MemoryAccessExperiment {

    private static volatile long blackhole;

    public static void main(String[] args) {

        int[] sizes = {
                16_384,
                262_144,
                1_048_576,
                4_194_304,
                8_388_608
        };

        System.out.println("Memory Access Research");
        System.out.println("============================================");

        for (int size : sizes) {

            int[] data = new int[size];
            int[] sequentialOrder = new int[size];
            int[] randomOrder = new int[size];

            for (int i = 0; i < size; i++) {
                data[i] = i * 31 + 7;
                sequentialOrder[i] = i;
                randomOrder[i] = i;
            }

            Random random = new Random(12345);

            for (int i = size - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);

                int temp = randomOrder[i];
                randomOrder[i] = randomOrder[j];
                randomOrder[j] = temp;
            }

            int repetitions = Math.max(1, 2_000_000 / size);

            // Warm-up
            for (int i = 0; i < 5; i++) {
                measure(data, sequentialOrder, repetitions);
                measure(data, randomOrder, repetitions);
            }

            long[] sequentialTimes = new long[9];
            long[] randomTimes = new long[9];

            // Measurements
            for (int i = 0; i < 9; i++) {

                if (i % 2 == 0) {
                    sequentialTimes[i] =
                            measure(data, sequentialOrder, repetitions);

                    randomTimes[i] =
                            measure(data, randomOrder, repetitions);
                } else {
                    randomTimes[i] =
                            measure(data, randomOrder, repetitions);

                    sequentialTimes[i] =
                            measure(data, sequentialOrder, repetitions);
                }
            }

            double[] sequentialResults =
                    convertToNs(sequentialTimes, size, repetitions);

            double[] randomResults =
                    convertToNs(randomTimes, size, repetitions);

            System.out.println("\nArray size: " + size);
            System.out.println("Array memory: " +
                    String.format("%.2f MB",
                            size * 4.0 / (1024 * 1024)));

            System.out.println("\nSequential measurements:");
            printResults(sequentialResults);

            System.out.println("\nRandom measurements:");
            printResults(randomResults);
        }

        System.out.println("\nExperiment completed.");
    }

    private static long measure(
            int[] data, int[] order, int repetitions) {

        long checksum = 0;

        long start = System.nanoTime();

        for (int r = 0; r < repetitions; r++) {
            for (int i = 0; i < order.length; i++) {
                checksum += data[order[i]];
            }
        }

        long end = System.nanoTime();

        blackhole = checksum;

        return end - start;
    }

    private static double[] convertToNs(
            long[] times, int size, int repetitions) {

        double[] results = new double[times.length];

        long totalAccesses = (long) size * repetitions;

        for (int i = 0; i < times.length; i++) {
            results[i] = times[i] / (double) totalAccesses;
        }

        return results;
    }

    private static void printResults(double[] results) {

        double[] sorted = results.clone();
        Arrays.sort(sorted);

        double median = sorted[sorted.length / 2];

        double sum = 0;

        for (double value : results) {
            sum += value;
        }

        double mean = sum / results.length;

        double squaredDifferences = 0;

        for (double value : results) {
            squaredDifferences += Math.pow(value - mean, 2);
        }

        double standardDeviation =
                Math.sqrt(squaredDifferences / (results.length - 1));

        System.out.println("Individual results:");

        for (int i = 0; i < results.length; i++) {
            System.out.printf("Run %d: %.3f ns/access%n",
                    i + 1, results[i]);
        }

        System.out.printf("Minimum: %.3f ns/access%n", sorted[0]);
        System.out.printf("Maximum: %.3f ns/access%n",
                sorted[sorted.length - 1]);
        System.out.printf("Median: %.3f ns/access%n", median);
        System.out.printf("Mean: %.3f ns/access%n", mean);
        System.out.printf("Std. deviation: %.3f ns/access%n",
                standardDeviation);
    }
}
