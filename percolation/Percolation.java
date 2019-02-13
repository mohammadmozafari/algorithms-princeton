import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;

public class Percolation
{
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private boolean[] openSites;
    private boolean[] fullSites;
    private int openSitesNumber = 0;
    private int numberOfRows;

    public Percolation(int n)
    {
        if (n <= 0)
            throw new IllegalArgumentException();

        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        openSites = new boolean[n * n + 2];
        fullSites = new boolean[n * n];
        this.numberOfRows = n;
        for (int i = 0 ; i < n * n ; i++)
            openSites[i] = false;
        for (int i = 0 ; i < n * n ; i++)
            fullSites[i] = false;
        openSites[n * n] = true;
        openSites[n * n + 1] = true;
    }

    public void open(int row, int column)
    {
        if ((row < 1) || (row > numberOfRows) || (column < 1) || (column > numberOfRows))
            throw new IllegalArgumentException();

        int thisPlace = findPlace(row, column);
        if (openSites[thisPlace]) return;

        openSites[thisPlace] = true;
        openSitesNumber++;

        if (row > 1)
        {
            if (openSites[thisPlace - numberOfRows]) weightedQuickUnionUF.union(thisPlace, thisPlace - numberOfRows);
            if (fullSites[thisPlace - numberOfRows]) fullify(row - 1, column);
        }
        else
        {
            weightedQuickUnionUF.union(thisPlace, numberOfRows * numberOfRows);
            fullify(row, column);
        }
        if (row < numberOfRows)
        {
            if (openSites[thisPlace + numberOfRows]) weightedQuickUnionUF.union(thisPlace, thisPlace + numberOfRows);
            if (fullSites[thisPlace + numberOfRows]) fullify(row + 1, column);
        }
        else weightedQuickUnionUF.union(thisPlace, numberOfRows * numberOfRows + 1);
        if (column > 1)
        {
            if (openSites[thisPlace - 1]) weightedQuickUnionUF.union(thisPlace, thisPlace - 1);
            if (fullSites[thisPlace - 1]) fullify(row, column - 1);
        }
        if (column < numberOfRows)
        {
            if (openSites[thisPlace + 1]) weightedQuickUnionUF.union(thisPlace, thisPlace + 1);
            if (fullSites[thisPlace + 1]) fullify(row, column + 1);
        }
    }
    public boolean isOpen(int row, int column)
    {
        if ((row < 1) || (row > numberOfRows) || (column < 1) || (column > numberOfRows))
            throw new IllegalArgumentException();

        return openSites[(row - 1) * numberOfRows + column - 1];
    }
    public boolean isFull(int row, int column)
    {
        if ((row < 1) || (row > numberOfRows) || (column < 1) || (column > numberOfRows))
            throw new IllegalArgumentException();

        return fullSites[(row - 1) * numberOfRows + column - 1];
    }
    public int numberOfOpenSites()
    {
        return openSitesNumber;
    }

    public boolean percolates()
    {
        return weightedQuickUnionUF.connected(numberOfRows * numberOfRows, numberOfRows * numberOfRows + 1);
    }

    private int findPlace(int row, int column)
    {
        return (row - 1) * numberOfRows + column - 1;
    }
    private void fullify(int row, int column)
    {
        fullSites[findPlace(row, column)] = true;
        if (row > 1 && openSites[findPlace(row - 1, column)] &&
                !fullSites[findPlace(row - 1, column)]) fullify(row - 1, column);
        if (row < numberOfRows && openSites[findPlace(row + 1, column)] &&
                !fullSites[findPlace(row + 1, column)]) fullify(row + 1, column);
        if (column > 1 && openSites[findPlace(row, column - 1)] &&
                !fullSites[findPlace(row, column - 1)]) fullify(row, column - 1);
        if (column < numberOfRows && openSites[findPlace(row, column + 1)] &&
                !fullSites[findPlace(row, column + 1)]) fullify(row, column + 1);
    }

    public static void main(String[] args)
    {
        int n, row, column;

        n = StdIn.readInt();
        Percolation percolation = new Percolation(n);

        while (true)
        {
            row = StdIn.readInt();
            column = StdIn.readInt();
            percolation.open(row, column);
            if (percolation.percolates())
                break;
        }
        System.out.println("percolates now");
    }
}
