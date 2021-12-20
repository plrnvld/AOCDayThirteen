import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.nio.file.*;
import java.awt.Point;

class Main {
    public static void main(String[] args) {

        String foldText = "fold along ";

        List<Point> points = new LinkedList<Point>();
        List<Fold> folds = new LinkedList<Fold>();

        try (Stream<String> stream = Files.lines(Paths.get("Input.txt"))) {
            stream
            .filter(line -> !line.equals(""))
            .forEach(line -> {             
                if (line.startsWith(foldText)) {
                    String relevant = line.substring(foldText.length());
                    String[] parts = relevant.split("=");                    
                    Fold f = new Fold(parts[0].equals("x"), Integer.parseInt(parts[1]));
                    folds.add(f);
                } else {
                    String[] parts = line.split(",");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    Point p = new Point(x, y);
                    points.add(p);
                }
            });
        }
        catch (IOException ex) {
            System.out.println("Exception: " + ex);
        }

        Folder folder = new Folder();

        List<Point> pointsToFold = new LinkedList<Point>(points);
        
        // folder.printPoints(pointsToFold);

        /*
        for (Fold fold : folds) {
            pointsToFold = folder.Apply(fold, pointsToFold);
        }
        */

        pointsToFold = folder.Apply(folds.get(0), pointsToFold);

        // folder.printPoints(pointsToFold);
          
        System.out.println("Finished, " + pointsToFold.size() + " points and " + folds.size() + " folds read.");
    }    
}

class Folder {
    void printPoints(List<Point> points) {
        int maxX = points.stream().map(p -> p.x).max(Integer::compare).get();
        int maxY = points.stream().map(p -> p.y).max(Integer::compare).get();

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                final int streamX = x;
                final int streamY = y;
                boolean containsPoint = points.stream().anyMatch(p -> p.x == streamX && p.y == streamY);
                String item = containsPoint ? "#" : ".";
                System.out.print(item);
            }

            System.out.println();
        }
    }


    List<Point> Apply(Fold fold, List<Point> points) {
        System.out.println("Folding: " + fold);
        System.out.println();

        List<Point> foldedPoints = new LinkedList<Point>();

        for (Point point: points) {
            if (fold.foldX) {
                int borderX = fold.value;

                if (point.x < borderX) {
                    foldedPoints.add(point);
                } else if (point.x > borderX) {
                    foldedPoints.add(new Point(2 * borderX - point.x, point.y));
                }
                

            } else {
                int borderY = fold.value;

                if (point.y < borderY) {
                    foldedPoints.add(point);
                } else if (point.y > borderY) {
                    foldedPoints.add(new Point(point.x, 2 * borderY - point.y));
                }
            }
        }

        return foldedPoints
            .stream()
            .distinct()
            .collect(Collectors.toList());
    }
}

class Fold {
    boolean foldX;
    int value;

    Fold(boolean foldX, int value) {
        this.foldX = foldX;
        this.value = value;
    }

    @Override
    public String toString() {
        return "fold " + (foldX ? "x" : "y") + "="+ value;
    }
}