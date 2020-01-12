package com.mygdx.game.controller.civilization;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class CivilizationGraph extends Graph {

    private List<String> civilizations;

    public CivilizationGraph(List<String> civilizationCodes) {
        super(civilizationCodes.size());
        civilizations = civilizationCodes;
    }

    public void setRelation(String civCode1, String civCode2, int value) {
        adjMatrix[civilizations.indexOf(civCode1)][civilizations.indexOf(civCode2)] = value;
        adjMatrix[civilizations.indexOf(civCode2)][civilizations.indexOf(civCode1)] = value;
    }

    public void updateRelation(String civCode1, String civCode2, int value) {
        adjMatrix[civilizations.indexOf(civCode1)][civilizations.indexOf(civCode2)] += value;
        adjMatrix[civilizations.indexOf(civCode2)][civilizations.indexOf(civCode1)] += value;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            s.append(String.format("%15s", civilizations.get(i)) + ": ");
            for (Integer j : adjMatrix[i]) {
                s.append((j == null ? "x" :j) + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String args[]) {
        CivilizationGraph g = new CivilizationGraph(Arrays.asList("Inds", "Franks", "Englishmen", "Russians"));

        g.setRelation("Inds", "Franks", 1);
        g.setRelation("Inds", "Russians", 1);
        g.setRelation("Inds", "Englishmen", 1);
        g.setRelation("Russians", "Franks", 1);
        g.setRelation("Russians", "Englishmen", 1);

        System.out.print(g.toString());
    }
}