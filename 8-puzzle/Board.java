import java.util.Iterator;

public class Board
{
    private final int[][] blocks;
    private final int hammingNumber;
    private final int manhattanNumber;
    private int blankRow, blankColumn;

    public Board(int[][] blocks)
    {
        this.blocks = blocks;
        int goalColumn, goalRow, key;
        int rowDistance, columnDistance;
        int misplacedBlocks = 0, manhattanSum = 0;

        for (int i = 0 ; i < dimension() ; i++)
        {
            for (int j = 0 ; j < dimension() ; j++)
            {
                if (blocks[i][j] == 0)
                {
                    blankRow = i;
                    blankColumn = j;
                    continue;
                }
                if (blocks[i][j] != i * dimension() + j + 1)
                    misplacedBlocks++;

            }
        }
        for (int i = 0 ; i < dimension() ; i++)
        {
            for (int j = 0 ; j < dimension() ; j++)
            {
                key = blocks[i][j];
                if (key == 0) continue;
                goalRow = (key - 1) / dimension();
                goalColumn = (key - 1) % dimension();

                rowDistance = (goalRow > i) ? goalRow - i : i - goalRow;
                columnDistance = (goalColumn > j) ? goalColumn - j : j - goalColumn;

                manhattanSum += rowDistance + columnDistance;
            }
        }

        hammingNumber = misplacedBlocks;
        manhattanNumber = manhattanSum;
    }

    public int dimension()
    {
        return blocks.length;
    }

    public int hamming()
    {
        return hammingNumber;
    }

    public int manhattan()
    {
        return manhattanNumber;
    }

    public boolean isGoal()
    {
        return (hammingNumber == 0);
    }

    public Board twin()
    {
        int j1 = 0, j2 = 0;
        int swapTemp;

        int[][] newBlocks = new int[dimension()][dimension()];
        for (int i = 0 ; i < dimension() ; i++)
        {
            System.arraycopy(blocks[i], 0, newBlocks[i], 0, dimension());
        }

        if (newBlocks[0][0] == 0) j1 = 1;
        else if (newBlocks[1][0] == 0) j2 = 1;

        swapTemp = newBlocks[0][j1];
        newBlocks[0][j1] = newBlocks[1][j2];
        newBlocks[1][j2] = swapTemp;

        return new Board(newBlocks);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Board)) return false;
        Board newBoard = (Board)obj;
        if (dimension() != newBoard.dimension()) return false;

        for (int i = 0 ; i < dimension() ; i++)
            for (int j = 0 ; j < dimension() ; j++)
                if (blocks[i][j] != newBoard.blocks[i][j]) return false;
        return true;
    }

    public Iterable<Board> neighbors()
    {
        return () -> new NeighborList(this);
    }

    @Override
    public String toString()
    {
        StringBuilder stringRep = new StringBuilder(dimension() + "\n");
        for (int i = 0 ; i < dimension() ; i++)
        {
            for (int j = 0 ; j < dimension() ; j++)
            {
                stringRep.append(blocks[i][j]);
                if (j != dimension() - 1)
                    stringRep.append(" ");
            }
            if (i != dimension() - 1)
                stringRep.append("\n");
        }
        return stringRep.toString();
    }

    private class NeighborList implements Iterator<Board>
    {
        int[] rows, cols;
        int cursor = 0;
        Board board;

        NeighborList(Board board)
        {
            this.board = board;
            rows = new int[4];
            cols = new int[4];

            rows[0] = board.blankRow - 1;
            rows[1] = board.blankRow;
            rows[2] = board.blankRow;
            rows[3] = board.blankRow + 1;

            cols[0] = board.blankColumn;
            cols[1] = board.blankColumn - 1;
            cols[2] = board.blankColumn + 1;
            cols[3] = board.blankColumn;
        }

        @Override
        public boolean hasNext()
        {
            while ((cursor < 4) && !(rows[cursor] >= 0 && rows[cursor] < dimension() && cols[cursor] >= 0 && cols[cursor] < dimension())) cursor++;
            return (cursor < 4);
        }

        @Override
        public Board next()
        {
            int[][] newBlocks = new int[dimension()][dimension()];
            for (int i = 0 ; i < dimension() ; i++)
            {
                System.arraycopy(blocks[i], 0, newBlocks[i], 0, dimension());
            }

            newBlocks[board.blankRow][board.blankColumn] = newBlocks[rows[cursor]][cols[cursor]];
            newBlocks[rows[cursor]][cols[cursor]] = 0;
            cursor++;

            return new Board(newBlocks);
        }
    }

    public static void main(String[] args)
    {
        int[][] blocks = {
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        };
        int[][] second = {
                {0, 1, 3},
                {4, 8, 2},
                {7, 6, 5}
        };
        int[][] newBlocks;

        Board initial = new Board(blocks);
        System.out.println(initial.dimension());
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());
        System.out.println(initial);
        System.out.println(initial.isGoal());
        System.out.println("-------------");
        System.out.println(initial.twin());
        System.out.println(initial.equals(initial.twin().twin()));
        System.out.println("-------------");
//        newBlocks = initial.getBlocks();
//        newBlocks[0][0] = 50;
//        System.out.println(initial);
//        System.out.println("-------------");
        for (Board nei : initial.neighbors())
            System.out.println(nei + "\n");
        System.out.println("-------------");
        Board secondB = new Board(second);
        for (Board nei : secondB.neighbors())
            System.out.println(nei + "\n");
        System.out.println("-------------");

    }
}
