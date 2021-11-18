package me.aston.shuffle.game.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public final class GamePlayer {

    private final UUID uuid;
    private Material material;
    private boolean foundBlock = false;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(@NotNull final Material material) {
        this.material = material;
    }

    public boolean hasFoundBlock() {
        return foundBlock;
    }

    public void setFoundBlock(final boolean foundBlock) {
        this.foundBlock = foundBlock;
    }

    @NotNull
    public Player getPlayer() {
        return Objects.requireNonNull(Bukkit.getPlayer(uuid));
    }

    public void sendMessage(@NotNull final String message) {
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }


}
