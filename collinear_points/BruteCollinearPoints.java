import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BruteCollinearPoints
{
    private LineSegment[] segments;
    private Point[] points;

    public BruteCollinearPoints(Point[] pts)
    {
        Point[] fourPoints = new Point[4];
        ArrayList<LineSegment> segs = new ArrayList<>();

        if (pts == null) throw new IllegalArgumentException();
        points = pts.clone();
        checkValidity();

        //finding collinear points using brute force
        for (int i = 0 ; i < points.length ; i++)
        {
            for (int j = i + 1 ; j < points.length ; j++)
            {
                for (int k = j + 1 ; k < points.length ; k++)
                {
                    for (int l = k + 1; l < points.length ; l++)
                    {
                        if (points[i].slopeTo(points[j]) == points[j].slopeTo(points[k]) &&
                            points[j].slopeTo(points[k]) == points[k].slopeTo(points[l]))
                        {
                            fourPoints[0] = points[i];
                            fourPoints[1] = points[j];
                            fourPoints[2] = points[k];
                            fourPoints[3] = points[l];
                            segs.add(new LineSegment(fourPoints[0], fourPoints[3]));
                        }

                    }
                }
            }
        }

        //converting array list to array
        segments = new LineSegment[segs.size()];
        for (int i = 0 ; i < segs.size() ; i++)
        {
            segments[i] = segs.get(i);
        }
    }

    public int numberOfSegments()
    {
        if (segments == null) return 0;
        return segments.length;
    }

    public LineSegment[] segments()
    {
        if (segments == null) return new LineSegment[0];
        return segments.clone();
    }

    private void checkValidity()
    {
        for (Point point : points)
            if (point == null)
                throw new IllegalArgumentException();

        Arrays.sort(points);
        for (int i = 0 ; i < points.length - 1 ; i++)
        {
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
    }


    public static void main(String[] args)
    {
        int x, y, n;
        Point[] points;
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        points = new Point[n];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.005);
        for (int i = 0 ; i < n ; i++)
        {
            x = in.nextInt();
            y = in.nextInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }

        FastCollinearPoints fast = new FastCollinearPoints(points);
        LineSegment[] segments = fast.segments();
        segments = fast.segments();
        for (int i = 0 ; i < segments.length ; i++)
        {
            segments[i].draw();
        }
    }
}
