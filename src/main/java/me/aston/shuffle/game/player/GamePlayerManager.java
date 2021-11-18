package me.aston.shuffle.game.player;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GamePlayerManager {

    private final Map<UUID, GamePlayer> players = new HashMap<>();

    public GamePlayerManager() {
    }

    public Optional<GamePlayer> find(@NotNull final UUID uuid) {
        return Optional.ofNullable(players.get(uuid));
    }

    public GamePlayer register(@NotNull final UUID uuid) {
        return players.put(uuid, new GamePlayer(uuid));
    }

    public void remove(@NotNull final UUID uuid) {
        players.remove(uuid);
    }

    public boolean exists(@NotNull final UUID uuid) {
        return players.containsKey(uuid);
    }

    public void broadcast(@NotNull final String message) {
        Bukkit.getOnlinePlayers().forEach(online -> {
            Optional<GamePlayer> optionalGamePlayer = find(online.getUniqueId());
            optionalGamePlayer.ifPresent(gamePlayer -> gamePlayer.sendMessage(message));
        });
    }

    public GamePlayer getLast() {
        return players.values().stream().findFirst().orElse(null);
    }

    public int size() {
        return players.size();
    }

    public void clear() {
        players.clear();
    }

}
