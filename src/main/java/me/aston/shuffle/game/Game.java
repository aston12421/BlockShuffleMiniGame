package me.aston.shuffle.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Game {

    private final Map<UUID, GamePlayer> players = new HashMap<>();
    private GameState gameState = GameState.INACTIVE;
    private final int roundTime = 10;
    private int roundTimer = roundTime;
    private int mins = (roundTimer % 3600) / 60;
    private int secs = roundTimer % 60;



    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getRoundTime() {
        return roundTime;
    }


    public int getRoundTimer() {
        return roundTimer;
    }

    public void setRoundTimer(int roundTimer) {
        this.roundTimer = roundTimer;
        this.mins = (roundTimer % 3600) / 60;
        this.secs = roundTimer % 60;
    }

    public void resetRoundTimer() {
        this.roundTimer = this.roundTime;
    }

    public int getMins() {
        return mins;
    }

    public int getSecs() {
        return secs;
    }

    public Map<UUID, GamePlayer> getPlayers() {
        return players;
    }



}
