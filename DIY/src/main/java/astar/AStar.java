package astar;

import java.util.*;

public class AStar {
    /*public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;

    //Blocked cells are just null Cell values in grid
    static Cell[][] grid = new Cell[5][5];

    static PriorityQueue<Cell> open;

    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;

    public static void sb(int i, int j) {
        grid[i][j] = null;
    }

    public static void setStartCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    public static void setEndCell(int i, int j) {
        endI = i;
        endJ = j;
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

    public static void AStar() {

        //add the start location to open list.
        open.add(grid[startI][startJ]);

        Cell current;

        while (true) {
            current = open.poll();
            if (current == null) break;
            closed[current.i][current.j] = true;

            if (current.equals(grid[endI][endJ])) {
                return;
            }

            Cell t;
            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                cu(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    cu(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    cu(current, t, current.finalCost + DIAGONAL_COST);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                cu(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                cu(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                cu(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    cu(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i + 1][current.j + 1];
                    cu(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
        }
    }

    *//*
    Params :
    tCase = test case No.
    x, y = Board's dimensions
    startX, startY = start location's x and y coordinates
    endX, endY = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
    *//*
    public static void test(int tCase, int x, int y, int startX, int startY, int endX, int endY, int[][] blocked) {
        System.out.println("\n\nTest Case #" + tCase);
        //Reset
        grid = new Cell[x][y];
        closed = new boolean[x][y];

        open = new PriorityQueue(16, new Comparator() {
            public int compare(Object o1, Object o2) {
                Cell c1 = (Cell) o1;
                Cell c2 = (Cell) o2;

                return c1.finalCost < c2.finalCost ? -1 :
                        c1.finalCost > c2.finalCost ? 1 : 0;
            }

        });
        //Set start position
        setStartCell(startX, startY);  //Setting to 0,0 by default. Will be useful for the UI part

        //Set End Location
        setEndCell(endX, endY);

        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
                System.out.print(grid[i][j].heuristicCost + " ");
            }
            System.out.println();
        }
        grid[startX][startY].finalCost = 0;
           
           *//*
             Set blocked cells. Simply set the cell values to null
             for blocked cells.
           *//*
        for (int i = 0; i < blocked.length; ++i) {
            sb(blocked[i][0], blocked[i][1]);
        }

        //Display initial map
        System.out.println("Grid: ");
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                if (i == startX && j == startY) System.out.print("SO  "); //Source
                else if (i == endX && j == endY) System.out.print("DE  ");  //Destination
                else if (grid[i][j] != null) System.out.printf("%-3d ", 0); // Block
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();

        AStar();
        System.out.println("\nScores for cells: ");
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < x; ++j) {
                if (grid[i][j] != null) System.out.printf("%-3d ", grid[i][j].finalCost);
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();

        if (closed[endI][endJ]) {
            //Trace back the path
            System.out.println("Path: ");
            Cell current = grid[endI][endJ];
            System.out.print(current);
            while (current.parent != null) {
                System.out.print(" -> " + current.parent);
                current = current.parent;
            }
            System.out.println();
        } else System.out.println("No possible path");
    }

    public static void main(String[] args) throws Exception {
        test(1, 5, 5, 0, 0, 3, 2, new int[][]{{0, 4}, {2, 2}, {3, 1}, {3, 3}});
        test(2, 5, 5, 0, 0, 4, 4, new int[][]{{0, 4}, {2, 2}, {3, 1}, {3, 3}});
        test(3, 7, 7, 2, 1, 5, 4, new int[][]{{4, 1}, {4, 3}, {5, 3}, {2, 3}});

        test(1, 5, 5, 0, 0, 4, 4, new int[][]{{3, 4}, {3, 3}, {4, 3}});
    }*/
}