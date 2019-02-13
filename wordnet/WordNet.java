import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.TreeMap;

public class WordNet
{
    private int[] marked;
    private int distance, sca;
    private Digraph wordNetGraph;
    private TreeMap<String, ArrayList<Integer>> nouns;
    private ArrayList<String> allSynsets;
    private SAP sap;

    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        int id, numberOfSynsets = 0;
        In synsInput = new In(synsets);
        In hypesInput = new In(hypernyms);
        String line;
        String[] slices, words;
        ArrayList<Integer> ids, temp;
        allSynsets = new ArrayList<>();
        nouns = new TreeMap<>();


        while (synsInput.hasNextLine())
        {
            line = synsInput.readLine();
            if (line != null) numberOfSynsets++;
            slices = line.split(",");
            allSynsets.add(slices[1]);
            words = slices[1].split(" ");

            for (String word : words)
            {
                id = Integer.parseInt(slices[0]);
                ids = nouns.get(word);
                if (ids == null)
                {
                    temp = new ArrayList<>();
                    temp.add(id);
                    nouns.put(word, temp);
                }
                else
                {
                    ids.add(id);
                }
            }
        }

        wordNetGraph = new Digraph(numberOfSynsets);
        marked = new int[numberOfSynsets];
        while (hypesInput.hasNextLine())
        {
            line = hypesInput.readLine();
            slices = line.split(",");
            id = Integer.parseInt(slices[0]);
            for (int i = 1 ; i < slices.length ; i++)
                wordNetGraph.addEdge(id, Integer.parseInt(slices[i]));
        }

        if (!isRooted() || !isDag())
            throw new IllegalArgumentException();

        sap = new SAP(wordNetGraph);
    }
    public Iterable<String> nouns()
    {
        return nouns.keySet();
    }
    public boolean isNoun(String word)
    {
        if (word == null)
            throw new IllegalArgumentException();

        return nouns.containsKey(word);
    }
    public int distance(String nounA, String nounB)
    {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();

        ArrayList<Integer> s1;
        ArrayList<Integer> s2;

        s1 = nouns.get(nounA);
        s2 = nouns.get(nounB);

        if (s1 == null || s2 == null)
            throw new IllegalArgumentException();

        return sap.length(s1, s2);
    }
    public String sap(String nounA, String nounB)
    {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();

        ArrayList<Integer> s1;
        ArrayList<Integer> s2;
        int key;

        s1 = nouns.get(nounA);
        s2 = nouns.get(nounB);

        if (s1 == null || s2 == null)
            throw new IllegalArgumentException();

        key = sap.ancestor(s1, s2);
        return allSynsets.get(key);
    }

    private boolean isDag()
    {
        for (int i = 0 ; i < wordNetGraph.V() ; i++)
        {
            if (marked[i] == 0)
                if (!dfs(i)) return false;
        }
        return true;
    }

    private boolean dfs(int v)
    {
        marked[v] = -1;
        for (int nei : wordNetGraph.adj(v))
        {
            if (marked[nei] == -1)
                return false;
            else if (marked[nei] == 0 && !dfs(nei))
                return false;
        }
        marked[v] = 1;
        return true;
    }

    private boolean isRooted()
    {
        int counter = 0;
        for (int i = 0 ; i < wordNetGraph.V() ; i++)
        {
            if (wordNetGraph.outdegree(i) == 0)
                counter++;
        }
        if (counter != 1)
            return false;
        return true;
    }

    public static void main(String[] args)
    {
        WordNet wordNet = new WordNet("test.txt", "test2.txt");
        wordNet.nouns();
    }
}
