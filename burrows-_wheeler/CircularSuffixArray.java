public class CircularSuffixArray
{
    private String originalString;
    private int[] sortedSuffixes;

    public CircularSuffixArray(String s)
    {
        if (s == null)
            throw new IllegalArgumentException();

        originalString = s;
        sortedSuffixes = new int[s.length()];
        for (int i = 0; i < s.length() ; i++)
            sortedSuffixes[i] = i;

        sort(0, s.length() - 1, 0);
    }

    public int length()
    {
        return originalString.length();
    }

    public int index(int i)
    {
        if (i < 0 || i >= sortedSuffixes.length)
            throw new IllegalArgumentException();
        return sortedSuffixes[i];
    }

    public static void main(String[] args)
    {
        CircularSuffixArray csa = new CircularSuffixArray("AAAAAA");

        for (int i = 0 ; i < csa.length() ; i++)
        {
            System.out.println(csa.sortedSuffixes[i]);
        }
    }

    private void sort(int low, int high, int step)
    {
        if (high <= low) return;
        int lt = low, gt = high, pivot = low;

        int i = low + 1, ch;
        char pivotChar = originalString.charAt((sortedSuffixes[pivot] + step) % sortedSuffixes.length);
        while (i <= gt)
        {
            ch = originalString.charAt((sortedSuffixes[i] + step) % sortedSuffixes.length);
            if (ch < pivotChar) swap(lt++, i++);
            else if (ch > pivotChar) swap(i, gt--);
            else i++;
        }

        sort(low, lt - 1, step);
        if (step + 1 < sortedSuffixes.length) sort(lt, gt, step + 1);
        sort(gt + 1, high, step);
    }

    private void swap(int i, int j)
    {
        int temp = sortedSuffixes[i];
        sortedSuffixes[i] = sortedSuffixes[j];
        sortedSuffixes[j] = temp;
    }
}
