import java.util.Arrays;

import java.util.Comparator;

import java.util.Scanner;

import java.util.Stack;

 

class Point2D implements Comparable<Point2D>

{

    public static final Comparator<Point2D> X_ORDER = new XOrder();

    public static final Comparator<Point2D> Y_ORDER = new YOrder();

    public static final Comparator<Point2D> R_ORDER = new ROrder();

    public final Comparator<Point2D> POLAR_ORDER = new PolarOrder();

    public final Comparator<Point2D> ATAN2_ORDER = new Atan2Order();

    public final Comparator<Point2D> DISTANCE_TO_ORDER = new DistanceToOrder();

 

    private final double x; // x coordinate

    private final double y; // y coordinate

 

    public Point2D(double x, double y)

    {

        if (Double.isInfinite(x) || Double.isInfinite(y))

            throw new IllegalArgumentException("Coordinates must be finite");

        if (Double.isNaN(x) || Double.isNaN(y))

            throw new IllegalArgumentException("Coordinates cannot be NaN");

        if (x == 0.0)

            x = 0.0; // convert -0.0 to +0.0

        if (y == 0.0)

            y = 0.0; // convert -0.0 to +0.0

        this.x = x;

        this.y = y;

    }

 

    public double x()

    {

        return x;

    }

 

    public double y()

    {

        return y;

    }

 

    public double r()

    {

        return Math.sqrt(x * x + y * y);

    }

 

    public double theta()

    {

        return Math.atan2(y, x);

    }

 

    private double angleTo(Point2D that)

    {

        double dx = that.x - this.x;

        double dy = that.y - this.y;

        return Math.atan2(dy, dx);

    }

 

    public static int ccw(Point2D a, Point2D b, Point2D c)

    {

        double area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);

        if (area2 < 0)

            return -1;

        else if (area2 > 0)

            return +1;

        else

            return 0;

    }

 

    public static double area2(Point2D a, Point2D b, Point2D c)

    {

        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);

    }

 

    public double distanceTo(Point2D that)

    {

        double dx = this.x - that.x;

        double dy = this.y - that.y;

        return Math.sqrt(dx * dx + dy * dy);

    }

 

    public double distanceSquaredTo(Point2D that)

    {

        double dx = this.x - that.x;

        double dy = this.y - that.y;

        return dx * dx + dy * dy;

    }

 

    public int compareTo(Point2D that)

    {

        if (this.y < that.y)

            return -1;

        if (this.y > that.y)

            return +1;

        if (this.x < that.x)

            return -1;

        if (this.x > that.x)

            return +1;

        return 0;

    }

 

    private static class XOrder implements Comparator<Point2D>

    {

        public int compare(Point2D p, Point2D q)

        {

            if (p.x < q.x)

                return -1;

            if (p.x > q.x)

                return +1;

            return 0;

        }

    }

 

    private static class YOrder implements Comparator<Point2D>

    {

        public int compare(Point2D p, Point2D q)

        {

            if (p.y < q.y)

                return -1;

            if (p.y > q.y)

                return +1;

            return 0;

        }

    }

 

    private static class ROrder implements Comparator<Point2D>

    {

        public int compare(Point2D p, Point2D q)

        {

            double delta = (p.x * p.x + p.y * p.y) - (q.x * q.x + q.y * q.y);

            if (delta < 0)

                return -1;

            if (delta > 0)

                return +1;

            return 0;

        }

    }

 

    private class Atan2Order implements Comparator<Point2D>

    {

        public int compare(Point2D q1, Point2D q2)

        {

            double angle1 = angleTo(q1);

            double angle2 = angleTo(q2);

            if (angle1 < angle2)

                return -1;

            else if (angle1 > angle2)

                return +1;

            else

                return 0;

        }

    }

 

    private class PolarOrder implements Comparator<Point2D>

    {

        public int compare(Point2D q1, Point2D q2)

        {

            double dx1 = q1.x - x;

            double dy1 = q1.y - y;

            double dx2 = q2.x - x;

            double dy2 = q2.y - y;

 

            if (dy1 >= 0 && dy2 < 0)

                return -1; // q1 above; q2 below

            else if (dy2 >= 0 && dy1 < 0)

                return +1; // q1 below; q2 above

            else if (dy1 == 0 && dy2 == 0)

            { // 3-collinear and horizontal

                if (dx1 >= 0 && dx2 < 0)

                    return -1;

                else if (dx2 >= 0 && dx1 < 0)

                    return +1;

                else

                    return 0;

            } else

                return -ccw(Point2D.this, q1, q2); // both above or below

        }

    }

 

    private class DistanceToOrder implements Comparator<Point2D>

    {

        public int compare(Point2D p, Point2D q)

        {

            double dist1 = distanceSquaredTo(p);

            double dist2 = distanceSquaredTo(q);

            if (dist1 < dist2)

                return -1;

            else if (dist1 > dist2)

                return +1;

            else

                return 0;

        }

    }

 

    public boolean equals(Object other)

    {

        if (other == this)

            return true;

        if (other == null)

            return false;

        if (other.getClass() != this.getClass())

            return false;

        Point2D that = (Point2D) other;

        return this.x == that.x && this.y == that.y;

    }

 

    public String toString()

    {

        return "(" + x + ", " + y + ")";

    }

 

    public int hashCode()

    {

        int hashX = ((Double) x).hashCode();

        int hashY = ((Double) y).hashCode();

        return 31 * hashX + hashY;

    }

 

}

 

public class Gift_Wrapping_Algorithm

{

    private Stack<Point2D> hull = new Stack<Point2D>();

 

    public Gift_Wrapping_Algorithm(Point2D[] pts)

    {

 

        // defensive copy

        int N = pts.length;

        Point2D[] points = new Point2D[N];

        for (int i = 0; i < N; i++)

            points[i] = pts[i];

        Arrays.sort(points);

 

        Arrays.sort(points, 1, N, points[0].POLAR_ORDER);

 

        hull.push(points[0]); // p[0] is first extreme point

        int k1;

        for (k1 = 1; k1 < N; k1++)

            if (!points[0].equals(points[k1]))

                break;

        if (k1 == N)

            return; // all points equal

 

        int k2;

        for (k2 = k1 + 1; k2 < N; k2++)

            if (Point2D.ccw(points[0], points[k1], points[k2]) != 0)

                break;

        hull.push(points[k2 - 1]); // points[k2-1] is second extreme point

 

        for (int i = k2; i < N; i++)

        {

            Point2D top = hull.pop();

            while (Point2D.ccw(hull.peek(), top, points[i]) <= 0)

            {

                top = hull.pop();

            }

            hull.push(top);

            hull.push(points[i]);

        }

 

        assert isConvex();

    }

 

    public Iterable<Point2D> hull()

    {

        Stack<Point2D> s = new Stack<Point2D>();

        for (Point2D p : hull)

            s.push(p);

        return s;

    }

 

    private boolean isConvex()

    {

        int N = hull.size();

        if (N <= 2)

            return true;

 

        Point2D[] points = new Point2D[N];

        int n = 0;

        for (Point2D p : hull())

        {

            points[n++] = p;

        }

 

        for (int i = 0; i < N; i++)

        {

            if (Point2D

                    .ccw(points[i], points[(i + 1) % N], points[(i + 2) % N]) <= 0)

            {

                return false;

            }

        }

        return true;

    }

 

    // test client

    public static void main(String[] args)

    {

        System.out.println("Gift Wrapping Algorithm");

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of points");

        int N = sc.nextInt();

 

        Point2D[] points = new Point2D[N];

        System.out.println("Enter the coordinates of each points: <x> <y>");

        for (int i = 0; i < N; i++)

        {

            int x = sc.nextInt();

            int y = sc.nextInt();

            points[i] = new Point2D(x, y);

        }

        Gift_Wrapping_Algorithm graham = new Gift_Wrapping_Algorithm(points);

        System.out.println("The Wrapper covers following points: ");

        for (Point2D p : graham.hull())

            System.out.println(p);

 

        sc.close();

    }

 

}