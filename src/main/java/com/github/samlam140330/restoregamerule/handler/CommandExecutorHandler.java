package com.github.samlam140330.restoregamerule.handler;

import com.github.samlam140330.restoregamerule.manager.GameruleManager;
import com.github.samlam140330.restoregamerule.utils.GameruleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandExecutorHandler implements CommandExecutor {
    private final GameruleManager gameruleManager;

    public CommandExecutorHandler(GameruleManager gameruleManager) {
        this.gameruleManager = gameruleManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String _label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("set-gamerule")) {
            if (!sender.hasPermission("restoregamerule.setgamerule")) {
                sender.sendMessage("You do not have permission to use this command!");
                return true;
            }
            if (args.length != 3) {
                return false;
            }

            String worldName = args[0];
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage("Invalid world: " + worldName);
                return true;
            }

            String gameruleName = args[1];
            GameRule<?> gamerule = GameruleUtil.getTypedGamerule(gameruleName);
            if (gamerule == null) {
                sender.sendMessage("Invalid gamerule: " + gameruleName);
                return true;
            }

            String gameruleValue = args[2];
            Boolean result = GameruleUtil.setTypedGamerule(world, gamerule, gameruleValue);
            if (!result) {
                sender.sendMessage("Failed to set the gamerule: " + gameruleName + " to " + gameruleValue + " for world: " + worldName);
                return true;
            }

            result = gameruleManager.saveGamerule(worldName, gameruleName, gamerule, gameruleValue);
            if (!result) {
                sender.sendMessage("Failed to save the gamerule to file");
                return true;
            }

            sender.sendMessage("Successfully set and save the gamerule: " + gameruleName + " to " + gameruleValue + " for world: " + worldName);
            return true;
        }
        return false;
    }
}
