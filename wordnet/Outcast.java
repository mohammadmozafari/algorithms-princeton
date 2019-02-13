import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast
{
    private WordNet wordNet;
    private int[] distances;
    private int maxIndex = 0;

    public Outcast(WordNet wordNet)
    {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns)
    {
        distances = new int[nouns.length];
        for (int i = 0 ; i < nouns.length ; i++) distances[i] = 0;

        for (int i = 0 ; i < nouns.length ; i++)
        {
            for (int j = 0 ; j < nouns.length ; j++)
            {
                distances[i] += wordNet.distance(nouns[i], nouns[j]);
            }
        }

        for (int i = 0 ; i < nouns.length ; i++)
        {
            if (distances[i] > distances[maxIndex])
            {
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = {"book", "paper", "school", "newspaper",};
        System.out.println(outcast.outcast(nouns));
    }
}
