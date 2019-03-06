import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class BoggleSolver
{
    private BoggleBoard board;
    private boolean[][] marked;
    private Node root = null;
    private String[] dictionary, forShuffle;
    private StringBuilder word;
    private TreeSet<String> validWords;

    public BoggleSolver(String[] dictionary)
    {
        if (dictionary == null)
            throw new IllegalArgumentException();

        this.dictionary = dictionary;

        for (int i = 0 ; i < dictionary.length ; i++)
        {
            root = put(root, dictionary[i], dictionary[i].length(), 0);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        if (board == null)
            throw new IllegalArgumentException();

        this.board = board;
        marked = new boolean[board.rows()][board.cols()];
        validWords = new TreeSet<>();
        word = new StringBuilder();
        Node pointer;

        for (int i = 0 ; i < board.rows() ; i++)
        {
            for (int j = 0 ; j < board.cols(); j++)
            {
                pointer = root;
                wordsStartingWith(i, j, pointer);
                decrementWord(i, j);
            }
        }
        return validWords;
    }

    private void decrementWord(int i, int j)
    {
        if (marked[i][j])
        {
            word.deleteCharAt(word.length() - 1);
            if (word.length() > 0)
            {
                if (word.charAt(word.length() - 1) == 'Q')
                    word.deleteCharAt(word.length() - 1);
            }
            marked[i][j] = false;
        }
    }

    private class Node
    {
        private char c;
        private Node left, right, mid;
        private int val = -1;
    }

    private Node put(Node x, String key, int val, int d)
    {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    private void wordsStartingWith(int a, int b, Node pointer)
    {
        boolean found = false;
        char c = board.getLetter(a, b);
        int newA, newB;
        int[] neiRows = {-1, 0, 1}, neiCols = {-1, 0, 1};

        while (pointer != null)
        {
            if (pointer.c == c)
            {
                word.append(pointer.c);
                if (c != 'Q' && pointer.val == word.length() && word.length() >= 3)
                    validWords.add(word.toString());
                pointer = pointer.mid;

                if (c != 'Q')
                {
                    found = true;
                    break;
                }
                else
                    c = 'U';
            }
            else if (pointer.c > c) pointer = pointer.left;
            else pointer = pointer.right;
        }

        if (!found && word.length() > 0 && word.charAt(word.length() - 1) == 'Q')
            word.deleteCharAt(word.length() - 1);

        if (found)
        {
            marked[a][b] = true;
            for (int i = 0 ; i < neiRows.length ; i++)
            {
                for (int j = 0 ; j < neiCols.length ; j++)
                {
                    newA = a + neiRows[i];
                    newB = b + neiCols[j];

                    if (newA < 0 || newA >= board.rows() || newB < 0 || newB >= board.cols()) continue;
                    if (j == 1 && i == 1) continue;

                    if (!marked[newA][newB])
                    {
                        wordsStartingWith(newA, newB, pointer);
                        decrementWord(newA, newB);
                    }
                }
            }
        }
    }

    public int scoreOf(String word)
    {
        int result = Arrays.binarySearch(dictionary, word);
        if (result < 0) return 0;

        int length = word.length();
        if (length < 3) return 0;
        else if (length < 5) return 1;
        else if (length == 5) return 2;
        else if (length == 6) return 3;
        else if (length == 7) return 5;
        else return 11;
    }

    public static void main(String[] args) {

        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("test-board.txt");
        solver.getAllValidWords(board);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
