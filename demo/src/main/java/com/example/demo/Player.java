package com.example.demo;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
public class Player {
    @Getter
    private final String username;
    @Getter
    @Setter
    private int row;
    @Getter
    @Setter
    private int col;
    private final int budget = 100;

    public Player(String username) {
        this.username = username;
    }

    public static void assignRandomPosition(Player player, int maxRows, int maxColumns, List<Player> players) {
        if (maxRows <= 0 || maxColumns <= 0) {
            throw new IllegalArgumentException("maxRows and maxColumns must be positive integers");
        }

        Random random = new Random();
        int row, col;
        boolean positionTaken;
        do {
            row = random.nextInt(maxRows);
            col = random.nextInt(maxColumns);
            positionTaken = isPositionTaken(row, col, players);
        } while (positionTaken);
        player.setRow(row);
        player.setCol(col);
    }

    private static boolean isPositionTaken(int row, int col, List<Player> players) {
        for (Player player : players) {
            if (player.getRow() == row && player.getCol() == col) {
                return true;
            }
        }
        return false;
    }
}