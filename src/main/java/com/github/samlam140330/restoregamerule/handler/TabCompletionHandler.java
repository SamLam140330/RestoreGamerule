package com.github.samlam140330.restoregamerule.handler;

import com.github.samlam140330.restoregamerule.utils.GameruleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompletionHandler implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender _sender, @NotNull Command command, @NotNull String _alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("set-gamerule")) {
            if (args.length == 1) {
                for (World world : Bukkit.getWorlds()) {
                    completions.add(world.getName());
                }
                return completions;
            }

            if (args.length == 2) {
                String worldName = args[0];
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    return Collections.emptyList();
                }

                String[] gamerules = world.getGameRules();
                completions.addAll(Arrays.asList(gamerules));
                return completions;
            }

            if (args.length == 3) {
                String gameruleName = args[1];
                if (gameruleName.isEmpty()) {
                    return Collections.emptyList();
                }

                GameRule<?> gamerule = GameruleUtil.getTypedGamerule(gameruleName);
                if (gamerule == null) {
                    return Collections.emptyList();
                }

                List<String> possibleValues = getPossibleValuesForGameRule(gamerule);
                completions.addAll(possibleValues);
                return completions;
            }
        }
        return Collections.emptyList();
    }

    private @NotNull List<String> getPossibleValuesForGameRule(@NotNull GameRule<?> gameRule) {
        List<String> possibleValues = new ArrayList<>();
        if (GameruleUtil.getGameruleClass(gameRule) == Boolean.class) {
            possibleValues.add("true");
            possibleValues.add("false");
        } else {
            possibleValues.add("<value>");
        }
        return possibleValues;
    }
}
