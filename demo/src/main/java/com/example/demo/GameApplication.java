package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
@CrossOrigin
@RestController
public class GameApplication {
    private static final List<Player> lobby = new ArrayList<>();
    private static final Map<String, List<Integer>> playerPositions = new HashMap<>();
    private static boolean gameStartedForAnyPlayer = false;

    private static int MAX_ROWS = 10;
    private static int MAX_COLUMNS = 10;
    private static int centerCityRow;
    private static int centerCityCol;

    public static void main(String[] args) {
        SpringApplication.run(GameApplication.class, args);
    }

    @PostMapping("/api/game/start")
    public Map<String, Object> startGame() {
        if (!lobby.isEmpty() && lobby.size() >= 2 && !gameStartedForAnyPlayer) {
            System.out.println("Game Started!");
            List<Player> players = new ArrayList<>(lobby);
            gameStartedForAnyPlayer = true;

            int maxRows = 10;
            int maxColumns = 10;

            MAX_ROWS = maxRows;
            MAX_COLUMNS = maxColumns;

            playerPositions.clear();

            Player firstPlayer = players.get(0);
            firstPlayer.setRow(1);
            firstPlayer.setCol(1);
            playerPositions.put(firstPlayer.getUsername(), List.of(firstPlayer.getRow(), firstPlayer.getCol()));

            Player secondPlayer = players.get(1);
            secondPlayer.setRow(8);
            secondPlayer.setCol(8);
            playerPositions.put(secondPlayer.getUsername(), List.of(secondPlayer.getRow(), secondPlayer.getCol()));

            lobby.clear();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("playerPositions", playerPositions);
            responseData.put("maxRows", maxRows);
            responseData.put("maxColumns", maxColumns);

            return responseData;
        } else if (gameStartedForAnyPlayer) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("playerPositions", playerPositions);
            responseData.put("maxRows", MAX_ROWS);
            responseData.put("maxColumns", MAX_COLUMNS);

            return responseData;
        } else {
            System.out.println("Not enough players in the lobby to start the game.");
            return Collections.emptyMap();
        }
    }

    @PostMapping("/api/lobby")
    public Map<String, Object> joinLobby(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        boolean userExists = lobby.stream().anyMatch(p -> p.getUsername().equals(username));

        if (!userExists) {
            Player player = new Player(username);
            if (lobby.size() < 4) {
                lobby.add(player);
            }
        }

        System.out.println("Current players in the lobby: " + lobby);

        List<String> usernames = new ArrayList<>();
        for (Player p : lobby) {
            usernames.add(p.getUsername());
        }

        return Map.of(
                "lobby", usernames,
                "userExists", userExists
        );
    }

    @PostMapping("/api/lobby/clear")
    public void clearLobby() {
        lobby.clear();
        playerPositions.clear();
        gameStartedForAnyPlayer = false;
    }

    @PostMapping("/api/game/reset")
    public void resetGameState() {
        lobby.clear();
        playerPositions.clear();
        MAX_ROWS = 10;
        MAX_COLUMNS = 10;
        gameStartedForAnyPlayer = false;
    }

    @PostMapping("/api/game/join")
    public Map<String, Object> joinGame(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        boolean userExists = lobby.stream().anyMatch(p -> p.getUsername().equals(username));

        if (!userExists && !gameStartedForAnyPlayer) {
            Player player = new Player(username);
            if (lobby.size() < 4) {
                lobby.add(player);
                Player.assignRandomPosition(player, MAX_ROWS, MAX_COLUMNS, lobby);
                playerPositions.put(player.getUsername(), List.of(player.getRow(), player.getCol()));
            }
        } else if (!userExists && gameStartedForAnyPlayer) {
            Player player = new Player(username);
            if (lobby.size() < 4) {
                lobby.add(player);
                Player.assignRandomPosition(player, MAX_ROWS, MAX_COLUMNS, lobby);
                playerPositions.put(player.getUsername(), List.of(player.getRow(), player.getCol()));
            }
        }

        return Map.of(
                "gameStarted", gameStartedForAnyPlayer,
                "playerPositions", playerPositions,
                "maxRows", MAX_ROWS,
                "maxColumns", MAX_COLUMNS
        );
    }
}
