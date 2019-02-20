import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver
{
    private int[][] pixels;
    private float[] distTo;
    private int[] parent;

    public SeamCarver(Picture picture)
    {
        if (picture == null)
            throw new IllegalArgumentException("A picture must be provided.");

        pixels = new int[picture.width()][picture.height()];
        for (int i = 0 ; i < picture.width() ; i++)
        {
            for (int j = 0 ; j < picture.height() ; j++)
            {
                pixels[i][j] = picture.get(i, j).getRGB();
            }
        }
        preSet();
    }

    public Picture picture()
    {
        Picture pic = new Picture(width(), height());
        for (int i = 0 ; i < width() ; i++)
        {
            for (int j = 0 ; j < height() ; j++)
            {
                pic.set(i, j, new Color(pixels[i][j]));
            }
        }
        return pic;
    }

    public int width()
    {
        return pixels.length;
    }

    public int height()
    {
        return pixels[0].length;
    }

    public double energy(int x, int y)
    {
        float energy = 0;
        int left, right, top, bottom;
        int leftR, leftG, leftB, rightR, rightG, rightB, topR, topG, topB, bottomR, bottomG, bottomB;

        if (x >= width() || x < 0 || y >= height() || y < 0)
            throw new IllegalArgumentException("This coordinates don't exist in the picture.");

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000;

        left = pixels[x - 1][y];
        right = pixels[x + 1][y];
        top = pixels[x][y - 1];
        bottom = pixels[x][y + 1];

        leftR = (left >> 16) & 0xFF;
        leftG = (left >> 8) & 0xFF;
        leftB = (left >> 0) & 0xFF;
        rightR = (right >> 16) & 0xFF;
        rightG = (right >> 8) & 0xFF;
        rightB = (right >> 0) & 0xFF;
        topR = (top >> 16) & 0xFF;
        topG = (top >> 8) & 0xFF;
        topB = (top >> 0) & 0xFF;
        bottomR = (bottom >> 16) & 0xFF;
        bottomG = (bottom >> 8) & 0xFF;
        bottomB = (bottom >> 0) & 0xFF;

        energy += (leftR - rightR) * (leftR - rightR);
        energy += (leftG - rightG) * (leftG - rightG);
        energy += (leftB - rightB) * (leftB - rightB);

        energy += (topR - bottomR) * (topR - bottomR);
        energy += (topG - bottomG) * (topG - bottomG);
        energy += (topB - bottomB) * (topB - bottomB);

        return Math.sqrt(energy);
    }

    public int[] findHorizontalSeam()
    {
        preSet();
        return findSeam(true);
    }

    public int[] findVerticalSeam()
    {
        preSet();
        return findSeam(false);
    }

    public void removeHorizontalSeam(int[] seam)
    {
        if (seam == null || seam.length != width())
            throw new IllegalArgumentException();

        int[][] newPixels = new int[width()][height() - 1];
        createNewPhoto(seam, width(), height(), true, newPixels);
        pixels = newPixels;
    }

    public void removeVerticalSeam(int[] seam)
    {
        if (seam == null || seam.length != height())
            throw new IllegalArgumentException();

        int[][] newPixels = new int[width() - 1][height()];
        createNewPhoto(seam, height(), width(), false, newPixels);
        pixels = newPixels;
    }

    private void createNewPhoto(int[] seam, int length1, int length2, boolean horizontal, int[][] newPixels)
    {
        int k;
        for (int i = 0 ; i < length1 ; i++)
        {
            k = 0;

            if (seam[i] < 0 || seam[i] >= length2)
                throw new IllegalArgumentException();

            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();

            for (int j = 0 ; j < length2 ; j++)
            {
                if (j != seam[i])
                {
                    if (horizontal) newPixels[i][k++] = pixels[i][j];
                    else newPixels[k++][i] = pixels[j][i];
                }
            }
        }
    }

    private int[] findSeam(boolean horizontal)
    {
        int currentVer, length;
        int[] seam;
        length = horizontal ? width() : height();
        seam = new int[length];

        for (int v : findNeighbors(width() * height(), horizontal))
            relaxEdge(width() * height(), v, horizontal);

        for (int i = 0 ; i < width() * height() ; i++)
        {
            for (int v : findNeighbors(i, horizontal))
                relaxEdge(i, v, horizontal);
        }

        currentVer = width() * height() + 1;
        for (int i = length - 1 ; i >= 0 ; i--)
        {
            currentVer = parent[currentVer];
            if (horizontal) seam[i] = currentVer % height();
            else seam[i] = currentVer % width();
        }

        return seam;
    }

    private Iterable<Integer> findNeighbors(int vertex, boolean horizontal)
    {
        int x, y;
        ArrayList<Integer> nei = new ArrayList<>();

        if (vertex == width() * height())
        {
            int length = horizontal ? height() : width();
            for (int i = 0 ; i < length ; i++)
                nei.add(i);
            return nei;
        }
        else if (horizontal)
        {
            if (vertex > (width() - 1) * height())
            {
                nei.add(width() * height() + 1);
                return nei;
            }
            else
            {
                x = vertex / height();
                y = vertex % height();

                if (x < width() - 1)
                {
                    nei.add((x + 1) * height() + y);
                    if (y > 0)
                        nei.add((x + 1) * height() + y - 1);
                    if (y < height() - 1)
                        nei.add((x + 1) * height() + y + 1);
                }
            }
        }
        else
        {
            if (vertex > (height() - 1) * width())
            {
                nei.add(width() * height() + 1);
                return nei;
            }
            else
            {
                x = vertex % width();
                y = vertex / width();

                if (y < height() - 1)
                {
                    nei.add((y + 1) * width() + x);
                    if (x > 0)
                        nei.add((y + 1) * width() + x - 1);
                    if (x < width() - 1)
                        nei.add((y + 1) * width() + x + 1);
                }
            }
        }

        return nei;
    }

    private void relaxEdge(int from, int to, boolean horizontal)
    {
        int sX, sY;
        float edgeWeight;

        if (from == width() * height())
            edgeWeight = 0;
        else
        {
            if (horizontal)
            {
                sX = from / height();
                sY = from % height();
            }
            else
            {
                sX = from % width();
                sY = from / width();
            }
            edgeWeight = (float)energy(sX, sY);
        }

        if (distTo[to] > distTo[from] + edgeWeight)
        {
            distTo[to] = distTo[from] + edgeWeight;
            parent[to] = from;
        }
    }

    private void preSet()
    {
        distTo = new float[width() * height() + 2];
        parent = new int[width() * height() + 2];

        for (int i = 0 ; i < width() * height() ; i++)
            distTo[i] = Float.POSITIVE_INFINITY;

        distTo[width() * height()] = 0;
        distTo[width() * height() + 1] = Float.POSITIVE_INFINITY;
    }

    public static void main(String[] args)
    {
        SeamCarver sc = new SeamCarver(new Picture("src/1-A.png"));

        for (int i = 0 ; i < 468 ; i++)
        {
            sc.removeVerticalSeam(sc.findVerticalSeam());
        }
        sc.picture().save("src/1-B.png");
    }
}
