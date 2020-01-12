package com.mygdx.game.controller.civilization;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Graph {

    protected Integer adjMatrix[][];
    protected Integer numVertices;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        adjMatrix = new Integer[numVertices][numVertices];
    }

    public void setRelation(int i, int j, Integer value) {
        adjMatrix[i][j] = value;
        adjMatrix[j][i] = value;
    }

    public Integer getRelation(int i, int j) {
        return adjMatrix[i][j];
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            s.append(i + ": ");
            for (Integer j : adjMatrix[i]) {
                s.append(j == null ? "x" :j + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String args[])
    {
        Graph g = new Graph(4);

        g.setRelation(0, 1, 1);
        g.setRelation(0, 2, 1);
        g.setRelation(1, 2, 1);
        g.setRelation(2, 0, 1);
        g.setRelation(2, 3, 1);

        System.out.print(g.toString());
        /* Outputs
           0: 0 1 1 0
           1: 1 0 1 0
           2: 1 1 0 1
           3: 0 0 1 0
          */
    }
}