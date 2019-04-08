import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront
{
    private static final int R = 256;
    public static void encode()
    {
        char c, hold1 = 0, hold2;
        char[] seq = new char[R];
        int index;

        for (int i = 0 ; i < 256 ; i++)
            seq[i] = (char)i;

        while (!BinaryStdIn.isEmpty())
        {
            c = BinaryStdIn.readChar();
            for (index = 0 ; index < R ; index++)
            {
                if (index == 0)
                {
                    hold1 = seq[index];
                }
                if (seq[index] == c)
                {
                    seq[0] = seq[index];
                    if (index != 0)
                        seq[index] = hold1;
                    break;
                }
                if (index != 0)
                {
                    hold2 = seq[index];
                    seq[index] = hold1;
                    hold1 = hold2;
                }
            }
            BinaryStdOut.write((char)index);
        }
        BinaryStdOut.close();
    }
    public static void decode()
    {
        char c, hold;
        char[] seq = new char[R];
        int index;

        for (int i = 0 ; i < 256 ; i++)
            seq[i] = (char)i;

        while (!BinaryStdIn.isEmpty())
        {
            c = BinaryStdIn.readChar();
            index = (int)c;
            hold = seq[index];
            BinaryStdOut.write(hold);
            for (int j = index ; j > 0 ; j--)
                seq[j] = seq[j - 1];
            seq[0] = hold;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args)
    {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}
