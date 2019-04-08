import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler
{
    public static void transform()
    {
        char[] chars;
        int first = 0, index;
        String message;

        message = BinaryStdIn.readString();

        BinaryStdIn.close();
        chars = message.toCharArray();
        CircularSuffixArray csa = new CircularSuffixArray(message);

        for (int i = 0; i < csa.length() ; i++)
        {
            int charNumber;
            index = csa.index(i);
            if (index == 0)
            {
                charNumber = csa.length() - 1;
                first = i;
            }
            else
            {
                charNumber = index - 1;
            }
            chars[i] = message.charAt(charNumber);
        }

        BinaryStdOut.write(first);

        for (int i = 0 ; i < message.length() ; i++)
            BinaryStdOut.write(chars[i]);
        BinaryStdOut.close();
    }
    public static void inverseTransform()
    {
        boolean[] marked;
        char[] chars;
        int first = 0;
        int[] next;
        String message;

        first = BinaryStdIn.readInt();
        message = BinaryStdIn.readString();
        BinaryStdIn.close();

        chars = message.toCharArray();
        Node[] nodes = new Node[chars.length];
        for (int i = 0 ; i < chars.length ; i++)
            nodes[i] = new Node(chars[i], i);

        sort(nodes);
        sort(chars);

        next = new int[chars.length];
        for (int i = 0 ; i < chars.length ; i++)
        {
            next[i] = nodes[i].val;
        }
        for (int i = 0 ; i < message.length() ; i++)
        {
            BinaryStdOut.write(chars[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }
    public static void main(String[] args)
    {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException();
    }

    private static class Node
    {
        char key;
        int val;
        Node (char k, int v)
        {
            key = k;
            val = v;
        }
    }

    private static void sort(Node[] arr)
    {
        int n = arr.length;
        Node output[] = new Node[n];
        int count[] = new int[256];
        for (int i = 0; i< 256; ++i)
            count[i] = 0;
        for (int i=0; i<n; ++i)
            ++count[arr[i].key];
        for (int i=1; i<=255; ++i)
            count[i] += count[i-1];
        for (int i = n-1; i>=0; i--)
        {
            output[count[arr[i].key]-1] = arr[i];
            --count[arr[i].key];
        }
        for (int i = 0; i<n; ++i)
            arr[i] = output[i];
    }

    private static void sort(char arr[])
    {
        int n = arr.length;
        char output[] = new char[n];
        int count[] = new int[256];
        for (int i=0; i<256; ++i)
            count[i] = 0;
        for (int i=0; i<n; ++i)
            ++count[arr[i]];
        for (int i=1; i<=255; ++i)
            count[i] += count[i-1];
        for (int i = n-1; i>=0; i--)
        {
            output[count[arr[i]]-1] = arr[i];
            --count[arr[i]];
        }
        for (int i = 0; i<n; ++i)
            arr[i] = output[i];
    }
}