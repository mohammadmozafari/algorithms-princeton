import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;


public class PointSET
{
    private TreeSet<Point2D> points;

    public PointSET()
    {
        points = new TreeSet<>();
    }
    public boolean isEmpty()
    {
        return points.isEmpty();
    }
    public int size()
    {
        return points.size();
    }
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();

        points.add(p);
    }
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();

        return points.contains(p);
    }
    public void draw()
    {
        for (Point2D p : points)
        {
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException();

        ArrayList<Point2D> inRange = new ArrayList<>();
        for (Point2D p : points)
        {
            if (rect.contains(p))
                inRange.add(p);
        }
        return inRange;
    }
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();
        Point2D champ = null;

        for (Point2D point : points)
        {
            if (champ == null || point.distanceTo(p) < champ.distanceTo(p))
                champ = point;
        }
        return champ;
    }

    public static void main(String[] args)
    {
//        Random rand = new Random();
//        Point2D newPoint;
//        PointSET set = new PointSET();
//        RectHV rectHV;
//        ArrayList<Point2D> inRange;
//        double x, y, x2, y2;
//
//
//        for (int i = 0 ; i < 4000 ; i++)
//        {
//            x = rand.nextDouble();
//            y = rand.nextDouble();
//
//            if (x < 0 || y < 0 || x > 1 || y > 1)
//                break;
//
//            newPoint = new Point2D(x, y);
//            set.insert(newPoint);
//        }
//
//        set.draw();
//
//        StdDraw.setPenColor(Color.RED);
//        x = rand.nextDouble();
//        y = rand.nextDouble();
//        x2 = rand.nextDouble();
//        y2 = rand.nextDouble();
//        rectHV = new RectHV(x < x2 ? x : x2, y < y2 ? y : y2, x < x2 ? x2 : x, y < y2 ? y2 : y);
//        rectHV.draw();
//
//        StdDraw.setPenColor(Color.RED);
//        long startTime = System.nanoTime();
//        inRange = (ArrayList<Point2D>)set.range(rectHV);
//        long estimated = System.nanoTime() - startTime;
//        System.out.println(estimated);
//        for (Point2D p : inRange)
//            p.draw();
    }
}
