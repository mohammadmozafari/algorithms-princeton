import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints
{
    private LineSegment[] segments;
    private Point[] points;

    public FastCollinearPoints(Point[] pts)
    {
        Comparator<Point> slopeComparator;
        ArrayList<Point> exceptOrigin;
        ArrayList<Point[]> distinct = new ArrayList<>();
        ArrayList<Point[]> pairPoint = new ArrayList<>();

        Point[] inLine;
        int cursor;

        //checking the validity of argument
        if (pts == null) throw new IllegalArgumentException();
        points = pts.clone();
        checkValidity();

        //finding collinear points
        if (points.length < 4) return;
        for (Point point : points)
        {
            exceptOrigin = new ArrayList<>();
            for (Point p : points)
            {
                if (p.compareTo(point) != 0)
                    exceptOrigin.add(p);
            }
            slopeComparator = point.slopeOrder();
            exceptOrigin.sort(slopeComparator);
            cursor = 0;
            for (int i = 1 ; i <= exceptOrigin.size() ; i++)
            {
                if (i == exceptOrigin.size() || exceptOrigin.get(i).slopeTo(point) != exceptOrigin.get(cursor).slopeTo(point))
                {
                    if (i - cursor >= 3)
                    {
                        inLine = new Point[i - cursor + 1];
                        inLine[0] = point;
                        for (int k = cursor ; k < i ; k++)
                            inLine[k - cursor + 1] = exceptOrigin.get(k);
                        Arrays.sort(inLine);
                        Point[] points = new Point[2];
                        points[0] = inLine[0];
                        points[1] = inLine[inLine.length - 1];
                        pairPoint.add(points);
                    }
                    cursor = i;
                }
            }
        }

        //converting array list to array
        Comparator<Point[]> pairCompare = (a, b) ->
        {
            if (a[0].compareTo(b[0]) != 0)
                return a[0].compareTo(b[0]);
            return a[1].compareTo(b[1]);
        };
        pairPoint.sort(pairCompare);
        if (pairPoint.size() == 0) return;
        distinct.add(pairPoint.get(0));
        for (int i = 1; i < pairPoint.size() ; i++)
        {
            if (pairCompare.compare(distinct.get(distinct.size() - 1), pairPoint.get(i)) != 0)
                distinct.add(pairPoint.get(i));
        }
        segments = new LineSegment[distinct.size()];
        for (int i = 0 ; i < distinct.size() ; i++)
            segments[i] = new LineSegment(distinct.get(i)[0], distinct.get(i)[1]);
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
        if (points == null) throw new IllegalArgumentException();

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
}
