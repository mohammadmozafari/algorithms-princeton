import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class Solver
{
    private Board[] solutionBoards;
    private int minimumMoves;

    private class Step implements Comparable<Step>
    {
        Board current;
        Step previous;
        int moves;
        int priority;

        Step(Board current, Step previous)
        {
            this.current = current;
            this.previous = previous;
            if (previous == null)
                moves = 0;
            else
                moves = previous.moves + 1;
            priority = moves + current.manhattan();
        }

        @Override
        public int compareTo(Step o)
        {
            return Integer.compare(this.priority, o.priority);
        }
    }

    private class SolutionList implements Iterator<Board>
    {
        int index = 0;
        @Override
        public boolean hasNext()
        {
            return index < solutionBoards.length;
        }

        @Override
        public Board next()
        {
            return solutionBoards[index++];
        }
    }

    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<Step> mainPuzzle = new MinPQ<>();
        MinPQ<Step> twinPuzzle = new MinPQ<>();
        Step finalMove;

        mainPuzzle.insert(new Step(initial, null));
        twinPuzzle.insert(new Step(initial.twin(), null));

        do
        {
            finalMove = expandTree(mainPuzzle);
        } while (finalMove == null && expandTree(twinPuzzle) == null);

        if (finalMove == null)
        {
            solutionBoards = null;
            minimumMoves = -1;
        }
        else
        {
            minimumMoves = finalMove.moves;
            solutionBoards = new Board[minimumMoves + 1];
            Step pointer = finalMove;

            for (int i = minimumMoves; i >= 0 ; i--)
            {
                solutionBoards[i] = pointer.current;
                pointer = pointer.previous;
            }
        }
    }

    public boolean isSolvable()
    {
        return minimumMoves != -1;
    }

    public int moves()
    {
        return minimumMoves;
    }

    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        return SolutionList::new;
    }

    private Step expandTree(MinPQ<Step> heap)
    {
        if (heap.isEmpty()) return null;
        Step bestBoard = heap.delMin();
        if (bestBoard.current.isGoal()) return bestBoard;

        for (Board b : bestBoard.current.neighbors())
            if (bestBoard.previous == null || !b.equals(bestBoard.previous.current))
                heap.insert(new Step(b, bestBoard));
        return null;
    }

    public static void main(String[] args)
    {
        int n;
        int[][] blocks;
        Scanner scanner = new Scanner(System.in);

        n = scanner.nextInt();
        blocks = new int[n][n];
        for (int i = 0 ; i < n ; i++)
        {
            for (int j = 0 ; j < n ; j++)
            {
                blocks[i][j] = scanner.nextInt();
            }
        }

        Board initial = new Board(blocks);
        Solver solve = new Solver(initial);

        for (Board bs : solve.solution())
            System.out.println(bs + "\n");
    }
}
