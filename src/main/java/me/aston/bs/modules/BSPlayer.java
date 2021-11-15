package me.aston.bs.modules;

import org.bukkit.Material;

import java.util.UUID;

public class BSPlayer {

    private final UUID uuid;
    private Material material;
    private boolean foundBlock;

    public BSPlayer(UUID uuid, Material material) {
        this.uuid = uuid;
        this.material = material;
        this.foundBlock = false;
    }


    public UUID getUuid() {
        return uuid;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setFound(boolean found) {
        this.foundBlock = found;
    }

    public boolean hasFoundBlock() {
        return this.foundBlock;
    }
}
