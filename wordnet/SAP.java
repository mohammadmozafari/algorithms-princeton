import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP
{
    private Digraph graph;
    private int[] marked, secondaryMarked;
    private Queue<Integer> bfsQueue;
    private int length, ancestor;

    public SAP(Digraph G)
    {
        if (G == null)
            throw new IllegalArgumentException();
        graph = new Digraph(G);
    }

    public int length(int v, int w)
    {
        ArrayList<Integer> vs = new ArrayList<>();
        ArrayList<Integer> ws = new ArrayList<>();

        vs.add(v);
        ws.add(w);

        length(vs, ws);
        return length;
    }
    public int ancestor(int v, int w)
    {
        length(v, w);
        return ancestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        ArrayList<Integer> ws = new ArrayList<>();
        bfsQueue = new Queue<>();

        reset();
        for (Integer ver : v)
        {
            if (ver == null || ver < 0 || ver >= graph.V())
                throw new IllegalArgumentException();
            bfsQueue.enqueue(ver);
            marked[ver] = 0;
        }
        for (Integer ver : w)
        {
            if (ver == null || ver < 0 || ver >= graph.V())
                throw new IllegalArgumentException();
            ws.add(ver);
        }

        doubleBfs(ws);
        return length;
    }
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        length(v, w);
        return ancestor;
    }

    private void doubleBfs(Iterable<Integer> secondary)
    {
        int ver;
        while (!bfsQueue.isEmpty())
        {
            ver = bfsQueue.dequeue();
            for (int v : graph.adj(ver))
            {
                if (marked[v] == -1)
                {
                    marked[v] = marked[ver] + 1;
                    bfsQueue.enqueue(v);
                }
            }
        }
        for (int v : secondary)
        {
            bfsQueue.enqueue(v);
            secondaryMarked[v] = 0;
            if (marked[v] != -1)
            {
                if (length == -1 || marked[v] < length)
                {
                    length = marked[v];
                    ancestor = v;
                }

            }
        }
        while (!bfsQueue.isEmpty())
        {
            ver = bfsQueue.dequeue();
            for (int v : graph.adj(ver))
            {
                if (secondaryMarked[v] == -1)
                {
                    if (marked[v] == -1)
                    {
                        secondaryMarked[v] = secondaryMarked[ver] + 1;
                        bfsQueue.enqueue(v);
                    }
                    else
                    {
                        if (length == -1 || secondaryMarked[ver] + marked[v] + 1 < length)
                        {
                            length = secondaryMarked[ver] + marked[v] + 1;
                            ancestor = v;
                        }
                        secondaryMarked[v] = secondaryMarked[ver] + 1;
                        bfsQueue.enqueue(v);
                    }
                }
            }
        }
    }

    private void reset()
    {
        marked = new int[graph.V()];
        secondaryMarked = new int[graph.V()];
        length = -1;
        ancestor = -1;

        for (int i = 0 ; i < graph.V() ; i++)
        {
            secondaryMarked[i] = -1;
            marked[i] = -1;
        }
    }

    public static void main(String[] args)
    {
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
