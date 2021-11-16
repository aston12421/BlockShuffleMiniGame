package me.aston.shuffle.game;

import me.aston.shuffle.BlockShuffle;
import me.aston.shuffle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public final class Gamebaord {

    private final BlockShuffle plugin = BlockShuffle.getInstance();

    public Scoreboard getGameScoreboard() {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BlockShuffle", "dummy");

        obj.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "BLOCKSHUFFLE");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int mins = plugin.getGame().getMins();
        int secs = plugin.getGame().getSecs();

        Team block = board.registerNewTeam("block");
        block.addEntry(ChatColor.RED.toString());
        block.setPrefix("Block: ");
        block.setSuffix(ChatColor.BLUE + "BLOCK");
        Team time = board.registerNewTeam("round_timer");
        time.addEntry(ChatColor.GREEN.toString());
        time.setPrefix("Time: ");
        time.setSuffix(ChatColor.GREEN + Utils.twoDigitString(mins) + ":" + Utils.twoDigitString(secs));
        Team found = board.registerNewTeam("found_block");
        found.addEntry(ChatColor.BLUE.toString());
        found.setPrefix(ChatColor.GRAY + "FOUND");

        obj.getScore(ChatColor.GREEN.toString()).setScore(1);
        obj.getScore(ChatColor.RED.toString()).setScore(2);
        obj.getScore(ChatColor.BLUE.toString()).setScore(3);

        return board;
    }

}
