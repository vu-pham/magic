package astar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarEx {
    public static final int V_H_COST = 10;

    //Blocked cells are just null Cell values in grid
    static Cell[][] grid = new Cell[5][5];
    static PriorityQueue<Cell> open;
    static boolean closed[][];

    public static void sb(int i, int j) {
        grid[i][j] = null;
    }

    static void cu(Cell current, Cell t, int cost) {
        if (t == null || closed[t.i][t.j]) return;
        int t_final_cost = t.heuristicCost + cost;

        boolean inOpen = open.contains(t);
        if (!inOpen || t_final_cost < t.finalCost) {
            t.finalCost = t_final_cost;
            t.parent = current;
            if (!inOpen) open.add(t);
        }
    }

    public static void AStar(Graph graph) {

        //add the start location to open list.
        Point source = graph.getSource();
        open.add(grid[source.getX()][source.getY()]);

        Cell current;

        while (true) {
            current = open.poll();
            if (current == null) break;
            closed[current.i][current.j] = true;

            if (foundAllPaths(graph)) {
                return;
            }

            Cell t;
            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                if (graph.getClearGround().contains(new Point(current.i -1, current.j)) ||
                        graph.getDestinations().contains(new Point(current.i -1, current.j))) {
                    cu(current, t, current.finalCost + 1);
                } else {
                    cu(current, t, computeGCost(current, graph.getSource(), t));
                }

            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                if (graph.getClearGround().contains(new Point(current.i, current.j-1))||
                        graph.getDestinations().contains(new Point(current.i, current.j-1))) {
                    cu(current, t, current.finalCost + 1);
                } else {
                    cu(current, t, computeGCost(current, graph.getSource(), t));
                }
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                if (graph.getClearGround().contains(new Point(current.i, current.j+1))||
                        graph.getDestinations().contains(new Point(current.i, current.j+1))) {
                    cu(current, t, current.finalCost + 1);
                } else {
                    cu(current, t, computeGCost(current, graph.getSource(), t));
                }
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                if (graph.getClearGround().contains(new Point(current.i +1, current.j))||
                        graph.getDestinations().contains(new Point(current.i +1, current.j))) {
                    cu(current, t, current.finalCost + 1);
                } else {
                    cu(current, t, computeGCost(current, graph.getSource(), t));
                }

            }
        }
    }

    private static int computeGCost(Cell current, Point source, Cell t) {
        if (t == null) {
            return 0;
        }
        return current.finalCost + V_H_COST;
    }


    private static boolean foundAllPaths(Graph graph) {
        for(Point des : graph.getDestinations()) {
            if (!closed[des.getX()][des.getY()]) {
                return false;
            }
        }
        return true;
    }

    /*
    Params :
    tCase = test case No.
    x, y = Board's dimensions
    startX, startY = start location's x and y coordinates
    endX, endY = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
    */
    public static void test(Graph graph) {
        //Reset
        grid = new Cell[graph.getSizeX()][graph.getSizeY()];
        closed = new boolean[graph.getSizeX()][graph.getSizeY()];

        open = new PriorityQueue<>(64, (Cell c1, Cell c2) -> {
            if (graph.getClearGround().contains(new Point(c1.i, c1.j))) {
                return -1;
            }

            return c1.finalCost < c2.finalCost ? -1 :
                    c1.finalCost > c2.finalCost ? 1 : 0;
        });

        for (int i = 0; i < graph.getSizeX(); ++i) {
            for (int j = 0; j < graph.getSizeY(); ++j) {
                if (graph.getBlocked().contains(new Point(i,j))) {
                    sb(i, j);
                } else {
                    grid[i][j] = new Cell(i, j);
                    grid[i][j].heuristicCost = chc(i, j, graph.getSource(), graph.getDestinations(), graph.getClearGround());
                }
            }
        }
        grid[graph.getSource().getX()][graph.getSource().getY()].finalCost = 0;
           
        //Display initial map
        for (int i = 0; i <graph.getSizeX(); ++i) {
            for (int j = 0; j < graph.getSizeY(); ++j) {
                if (i == graph.getSource().getX() && j == graph.getSource().getY()) System.out.print("SO  "); //Source
                else if (graph.getDestinations().contains(new Point(i,j))) System.out.print("DE  ");  //Destination
                else if (grid[i][j] != null) System.out.printf("%-3d ", 0); // Block
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();

        AStar(graph);
        System.out.println("\nScores for cells: ");
        for (int i = 0; i < graph.getSizeX(); ++i) {
            for (int j = 0; j < graph.getSizeY(); ++j) {
                if (grid[i][j] != null) System.out.printf("%-3d ", grid[i][j].finalCost);
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();

        if (foundAllPaths(graph)) {
            //Trace back the path
            Set<Point> tbCleanedCell = new HashSet<Point>();
            for(Point des : graph.getDestinations()) {
                System.out.println("Path: ");
                Cell current = grid[des.getX()][des.getY()];
                System.out.print(current);

                while (current.parent != null) {
                    Point curPoint = new Point(current.i, current.j);
                    if (!graph.getDestinations().contains(curPoint) && !graph.getSource().equals(curPoint) && !graph.getClearGround().contains(curPoint)) {
                        tbCleanedCell.add(curPoint);
                    }
                    System.out.print(" -> " + current.parent);
                    current = current.parent;
                }
                System.out.println();
            }

            System.out.println("Total efforts: " + tbCleanedCell.size());
            graph.setCost(tbCleanedCell.size());

        } else {
            System.out.println("No possible path");
            graph.setCost(-1);
        }
    }

    private static int chc(int i, int j, Point source, List<Point> destinations, List<Point> clearedList) {
        int h = 0;
        if (!clearedList.contains(new Point(i,j)) && !source.equals(new Point(i,j)) && !destinations.contains(new Point(i,j))) {
            for (Point des : destinations) {
                h += Math.abs(i - des.getX()) + Math.abs(j - des.getY());
            }
        }
        return h;
    }

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        StringBuilder builder  = new StringBuilder();
        int count = 1;
        for(Graph graph : readInput(args[0])) {
            int bestCost = Integer.MAX_VALUE;
            System.out.println();
            System.out.println();
            System.out.println("################################################################################");
            System.out.println("Case " + count);
            for(Point source : graph.getHouses()) {
                graph.setSource(source);
                graph.setDestinations(graph.getHouses().stream().filter(p -> !p.equals(source)).collect(Collectors.toList()));
                test(graph);
                if (graph.getCost() < bestCost) {
                    bestCost = graph.getCost();
                }
            }
            if (bestCost == -1) {
                builder.append("Cannot find the cleared paths");
            } else {
                builder.append(bestCost);
            }
            builder.append("\r\n");
            count ++;
        }
        Files.write(Paths.get("shovelling-1.ans"), builder.toString().getBytes());

        System.out.println("---Duration time in milis: " + (System.currentTimeMillis() - startTime));
    }

    private static List<Graph> readInput(String fileName) {
        ArrayList<Graph> graphs = new ArrayList<Graph>();

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            Graph graph = new Graph();
            int currentIndexX = 0;
            for (String line : stream.collect(Collectors.toList())) {
                String nomalizedStr = line.trim();
                if (nomalizedStr.equals("0 0")) {
                    break;
                } else if (line.isEmpty()) {
                    graph = new Graph();
                } else if (isGraphSizeLine(nomalizedStr)){
                    graphs.add(graph);
                    int[] graphSize = extractGraphSizeLine(nomalizedStr);
                    graph.setSizeX(graphSize[0]);
                    graph.setSizeY(graphSize[1]);

                    // reset currentIndexX
                    currentIndexX = 0;
                } else {
                    // get graph information
                    for(int j = 0 ; j < line.length(); j ++) {
                        switch (line.charAt(j)) {
                            case 'o':
                                // do nothing, this cell will be computed HeuristicCost later
                                break;
                            case '#':
                                graph.getBlocked().add(new Point(currentIndexX, j));
                                break;
                            case '.':
                                graph.getClearGround().add(new Point(currentIndexX, j));
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                                graph.getHouses().add(new Point(currentIndexX, j));
                                break;
                        }
                    }
                    currentIndexX++;
                }

            }

        } catch (IOException e) {
            // cannot load input source
        }
        return graphs;
    }

    private static boolean isGraphSizeLine(String nomalizedStr) {
        String[] split = nomalizedStr.split(" ");
        if (split.length == 2) {
            Integer x = Integer.valueOf(split[0]);
            Integer y = Integer.valueOf(split[1]);
            if (x >= 1 && x <= 20 && x >=1 && y <= 20) {
                return true;
            }
        }
        return false;
    }

    private static int[] extractGraphSizeLine(String nomalizedStr) {
        String[] split = nomalizedStr.split(" ");
        Integer x = Integer.valueOf(split[0]);
        Integer y = Integer.valueOf(split[1]);
        int[] result = {x.intValue(), y.intValue()};
        return result;
    }

    static class Cell implements Comparable{
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent;

        Cell(int i, int j){
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }

        @Override
        public int compareTo(Object o) {
            Cell c2 = (Cell)o;
            return this.finalCost < c2.finalCost ? -1 :
                    this.finalCost > c2.finalCost ? 1 : 0;
        }
    }

    static class Graph {
        private int sizeX;
        private int sizeY;
        private Point source;
        private List<Point> houses = new ArrayList<>();
        private List<Point> destinations = new ArrayList<>();
        private List<Point> blocked = new ArrayList<>();
        private List<Point> clearGround = new ArrayList<>();
        private int cost; //result

        public Graph(){
        }

        public int getSizeX() {
            return sizeX;
        }

        public void setSizeX(int sizeX) {
            this.sizeX = sizeX;
        }

        public int getSizeY() {
            return sizeY;
        }

        public void setSizeY(int sizeY) {
            this.sizeY = sizeY;
        }

        public Point getSource() {
            return source;
        }

        public void setSource(Point source) {
            this.source = source;
        }

        public List<Point> getDestinations() {
            return destinations;
        }

        public void setDestinations(List<Point> destinations) {
            this.destinations = destinations;
        }

        public List<Point> getBlocked() {
            return blocked;
        }

        public void setBlocked(List<Point> blocked) {
            this.blocked = blocked;
        }

        public List<Point> getClearGround() {
            return clearGround;
        }

        public void setClearGround(List<Point> clearGround) {
            this.clearGround = clearGround;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public List<Point> getHouses() {
            return houses;
        }

        public void setHouses(List<Point> houses) {
            this.houses = houses;
        }
    }

    static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Point that = (Point)obj;
            return this.x == that.x && this.y == that.y;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new int[]{x,y});
        }
    }



}