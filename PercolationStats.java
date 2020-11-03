import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private final int T;
    private double[] experiments; //array of thresholds

    // Perform T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        //Special Cases: when N <= 0 or T <= 0
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N or T must be greater than 0");

        this.T = T;
        this.experiments = new double[T]; //T experiments

        //Loop for T experiments
        for (int i = 0; i < T; i++){
            Percolation per = new Percolation(N);
            while(!per.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                per.open(row, col);  //open random site
            }
            //add each threshold to the array
            experiments[i] = (double) per.numberOfOpenSites()/(N*N);
        }
    }

    // Sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(experiments);
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(experiments);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - (1.96 * stddev()) / Math.sqrt(T);
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + (1.96 * stddev()) / Math.sqrt(T);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
