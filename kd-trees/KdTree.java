import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Comparator;

public class KdTree
{
    private Node root;
    private int treeSize;
    private double xmin, ymin, xmax, ymax;
    private ArrayList<Point2D> inRange;
    private Point2D nearest;

    public KdTree()
    {
        root = null;
        treeSize = 0;
    }

    public boolean isEmpty()
    {
        return treeSize == 0;
    }

    public int size()
    {
        return treeSize;
    }

    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();

        xmin = ymin = 0.0;
        xmax = ymax = 1.0;
        root = insert(root, p, 0);
    }

    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();

        return contains(root, p);
    }

    public void draw()
    {
        draw(root);
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException();

        inRange = new ArrayList<>();
        rangeSearch(root, rect);
        return inRange;
    }

    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();

        nearest = null;
        findNearest(root, p);
        return nearest;
    }

    private class Node
    {
        Point2D key;
        Node right, left;
        RectHV rect;
        int level;

        Node(Point2D key, Node right, Node left, int level, RectHV rect)
        {
            this.key = key;
            this.right = right;
            this.left = left;
            this.level = level;
            this.rect = rect;
        }
    }

    private Node insert(Node tree, Point2D p, int level)
    {
        RectHV rect;
        if (tree == null)
        {
            treeSize++;

            if (level % 2 == 0)
                rect = new RectHV(p.x(), ymin, p.x(), ymax);
            else
                rect = new RectHV(xmin, p.y(), xmax, p.y());

            return new Node(p, null, null, level, rect);
        }

        int result = customCompare(p, tree.key, level % 2 == 0);

        if (result < 0)
        {
            if (level % 2 == 0)
                xmax = tree.key.x();
            else
                ymax = tree.key.y();

            tree.left = insert(tree.left, p, level + 1);
        }
        else if (result > 0)
        {
            if (level % 2 == 0)
                xmin = tree.key.x();
            else
                ymin = tree.key.y();

            tree.right = insert(tree.right, p, level + 1);
        }
        else tree.key = p;

        return tree;
    }

    private boolean contains(Node tree, Point2D p)
    {
        if (tree == null) return false;

        int result = customCompare(p, tree.key, tree.level % 2 == 0);

        if (result < 0) return contains(tree.left, p);
        else if (result > 0) return contains(tree.right, p);
        else return true;
    }

    private int customCompare(Point2D p1, Point2D p2, boolean hor)
    {
        Comparator<Point2D> primary, secondary;
        if (hor)
        {
            primary = Point2D.X_ORDER;
            secondary = Point2D.Y_ORDER;
        }
        else
        {
            primary = Point2D.Y_ORDER;
            secondary = Point2D.X_ORDER;
        }

        if (primary.compare(p1, p2) < 0) return -1;
        else if (primary.compare(p1, p2) > 0) return +1;
        else return secondary.compare(p1, p2);
    }

    private void draw(Node tree)
    {
        if (tree == null) return;

        tree.key.draw();

        draw(tree.left);
        draw(tree.right);
    }

    private void rangeSearch(Node tree, RectHV rect)
    {
        if (tree == null) return;

        if (tree.rect.intersects(rect))
        {
            if (rect.contains(tree.key))
                inRange.add(tree.key);

            rangeSearch(tree.left, rect);
            rangeSearch(tree.right, rect);
        }
        else if (tree.level % 2 == 0)
        {
            if (tree.key.x() < rect.xmin()) rangeSearch(tree.right, rect);
            else rangeSearch(tree.left, rect);
        }
        else
        {
            if (tree.key.y() < rect.ymin()) rangeSearch(tree.right, rect);
            else rangeSearch(tree.left, rect);
        }
    }

    private void findNearest(Node tree, Point2D p)
    {
        if (tree == null) return;

        if (nearest == null || p.distanceTo(nearest) > p.distanceTo(tree.key))
            nearest = tree.key;

        if (tree.level % 2 == 0)
        {
            if (p.x() < tree.rect.xmin())
            {
                findNearest(tree.left, p);
                if (nearest.distanceTo(p) > tree.rect.distanceTo(p)) findNearest(tree.right, p);
            }
            else
            {
                findNearest(tree.right, p);
                if (nearest.distanceTo(p) > tree.rect.distanceTo(p)) findNearest(tree.left, p);
            }
        }
        else
        {
            if (p.y() < tree.rect.ymin())
            {
                findNearest(tree.left, p);
                if (nearest.distanceTo(p) > tree.rect.distanceTo(p)) findNearest(tree.right, p);
            }
            else
            {
                findNearest(tree.right, p);
                if (nearest.distanceTo(p) > tree.rect.distanceTo(p)) findNearest(tree.left, p);
            }
        }
    }

    public static void main(String[] args)
    {
//        KdTree kd = new KdTree();
//        KdTree empty = new KdTree();
//        RectHV rect = new RectHV(0.3, 0.2, 0.61, 0.6);
//        Point2D po = new Point2D(0.4, 0.8);
//
//        kd.insert(new Point2D(0.5, 0.4));
//        kd.insert(new Point2D(0.3, 0.8));
//        kd.insert(new Point2D(0.4, 0.5));
//        kd.insert(new Point2D(0.45, 0.7));
//        kd.insert(new Point2D(0.7, 0.5));
//        kd.insert(new Point2D(0.6, 0.8));
//        kd.insert(new Point2D(0.6, 0.3));
//        kd.insert(new Point2D(0.6, 0.5));
//        kd.insert(new Point2D(0.3, 0.8));
//        kd.insert(new Point2D(0.4, 0.8));
//
//        System.out.println(kd.size());
//
//        System.out.println(kd.isEmpty());
//        System.out.println(empty.isEmpty());
//
//        System.out.println(kd.contains(new Point2D(0.7, 0.5)));
//
//        StdDraw.setPenRadius(0.01);
//        kd.draw();
//        rect.draw();
//
//        StdDraw.setPenColor(Color.red);
//        System.out.println(((ArrayList<Point2D>) kd.range(rect)).size());
//        for (Point2D p : kd.range(rect))
//            p.draw();
//
//        StdDraw.setPenColor(Color.GREEN);
//        Point2D near = kd.nearest(po);
//        near.draw();

        KdTree kd = new KdTree();
        PointSET set = new PointSET();
        Point2D point, nearTo, nearest1, nearest2;
        ArrayList<Point2D> points1, points2;
        RectHV rect;
        boolean flag;
        long start, est1, est2;

        double sum1 = 0, sum2 = 0;

        double x, y;
        double x1, x2, y1, y2;

        for (int i = 0 ; i < 100 ; i++)
        {
            x = y = 0;
            for (int j = 0 ; j < 5000 ; j++)
            {
                x = StdRandom.uniform();
                y = StdRandom.uniform();
                point = new Point2D(x, y);
                kd.insert(point);
                set.insert(point);
            }
            x = StdRandom.uniform();
            y = StdRandom.uniform();
            nearTo = new Point2D(x, y);

            x1 = StdRandom.uniform();
            x2 = StdRandom.uniform();
            y1 = StdRandom.uniform();
            y2 = StdRandom.uniform();

            rect = new RectHV(x1 < x2 ? x1 : x2, y1 < y2 ? y1 : y2, x1 < x2 ? x2 : x1, y1 < y2 ? y2 : y1);

            start = System.nanoTime();
            nearest1 = kd.nearest(nearTo);
            est1 = System.nanoTime() - start;
            start = System.nanoTime();
            nearest2 = set.nearest(nearTo);
            est2 = System.nanoTime() - start;
            sum1 += (double)est2 / (double)est1;

            start = System.nanoTime();
            points1 = (ArrayList<Point2D>)kd.range(rect);
            est1 = System.nanoTime() - start;
            start = System.nanoTime();
            points2 = (ArrayList<Point2D>)set.range(rect);
            est2 = System.nanoTime() - start;

            sum2 += (double)est2 / (double)est1;
        }

        System.out.println(sum1 / 100);
        System.out.println(sum2 / 100);
    }

}
