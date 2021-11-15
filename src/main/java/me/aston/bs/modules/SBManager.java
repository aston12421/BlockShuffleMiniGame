package me.aston.bs.modules;

import me.aston.bs.BS;
import me.aston.bs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class SBManager {

    private final BS plugin = BS.getInstance();

    public void create(BSPlayer bsPlayer) {
        int roundTimer = plugin.getBsManager().roundTimer;
        int mins = (roundTimer % 3600) / 60;
        int secs = roundTimer % 60;
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BlockShuffle", "dummy");

        obj.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "BLOCKSHUFFLE");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team block = board.registerNewTeam("block");
        block.addEntry(ChatColor.RED.toString());
        block.setPrefix("Block: ");
        block.setSuffix(ChatColor.BLUE + bsPlayer.getMaterial().name().replace("_", " "));
        Team time = board.registerNewTeam("round_timer");
        time.addEntry(ChatColor.GREEN.toString());
        time.setPrefix("Time: ");
        time.setSuffix(ChatColor.GREEN + Utils.twoDigitString(mins) + ":" + Utils.twoDigitString(secs));
        obj.getScore(ChatColor.GREEN.toString()).setScore(1);
        obj.getScore(ChatColor.RED.toString()).setScore(2);

        Bukkit.getPlayer(bsPlayer.getUuid()).setScoreboard(board);
    }

}
