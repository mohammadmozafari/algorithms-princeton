import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private double x[];
    private double mean, stddev, cLow, cHigh;

    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        Percolation percolation;
        int counter = 0;
        int randRow, randColumn;
        x = new double[trials];

        for (int i = 0 ; i < trials ; i++)
        {
            percolation = new Percolation(n);
            while (true)
            {
                randRow = StdRandom.uniform(n) + 1;
                randColumn = StdRandom.uniform(n) + 1;
                if (percolation.isOpen(randRow, randColumn))
                    continue;
                percolation.open(randRow, randColumn);
                counter++;
                if (percolation.percolates())
                    break;
            }
            x[i] = (double)counter / (n * n);
            counter  = 0;
        }

        mean = StdStats.mean(x);
        stddev = StdStats.stddev(x);
        cLow = mean() - 1.96 * stddev() / Math.sqrt(trials);
        cHigh = mean() + 1.96 * stddev() / Math.sqrt(trials);
    }
    public double mean() { return mean; }
    public double stddev()
    {
        return stddev;
    }
    public double confidenceLo()
    {
        return cLow;
    }
    public double confidenceHi()
    {
        return cHigh;
    }

    public static void main(String[] args)
    {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " +
                stats.confidenceHi() + "]");
    }
}
