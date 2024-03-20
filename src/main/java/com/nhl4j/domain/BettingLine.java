package com.nhl4j.domain;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;

@Data
public class BettingLine {

    private Float spread;
    private Float totalPoints;

    public static BettingLine parseLinesJson(ArrayNode linesListNode) {
        if (linesListNode == null || linesListNode.isEmpty()) {
            return null;
        }

        // There can be multiple providers, we will just take the first for now
        final var lineNode = linesListNode.get(0);
        final var bettingLine = new BettingLine();
        bettingLine.setSpread(lineNode.get("spread").floatValue());
        bettingLine.setTotalPoints(lineNode.get("overUnder").floatValue());

        return bettingLine;
    }
}
