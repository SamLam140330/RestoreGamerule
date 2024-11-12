package com.github.samlam140330.restoregamerule.manager;

import com.github.samlam140330.restoregamerule.Restoregamerule;
import com.github.samlam140330.restoregamerule.utils.GameruleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class GameruleManager {
    private final Restoregamerule plugin;
    private final File gameruleFile;
    private final YamlConfiguration gameruleConfig;

    public GameruleManager(@NotNull Restoregamerule plugin, @NotNull FileManager fileManager, @NotNull String fileName) {
        this.plugin = plugin;
        fileManager.createYMLFile(fileName);
        this.gameruleFile = fileManager.getFile(fileName);
        this.gameruleConfig = fileManager.getYamlConfig(gameruleFile);
    }

    public void applyGamerules() {
        for (String worldName : gameruleConfig.getKeys(false)) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("Invalid world: " + worldName + " in gamerules.yml. Skipping...");
                continue;
            }

            ConfigurationSection configurationSection = gameruleConfig.getConfigurationSection(worldName);
            if (configurationSection == null) {
                plugin.getLogger().warning("Incorrect format for world: " + worldName + " in gamerules.yml. Skipping...");
                continue;
            }

            Set<String> gamerules = configurationSection.getKeys(false);
            if (gamerules.isEmpty()) {
                plugin.getLogger().warning("No gamerules found for world: " + worldName + " in gamerules.yml. Skipping...");
                continue;
            }

            for (String gamerule : gamerules) {
                GameRule<?> gameRule = GameruleUtil.getTypedGamerule(gamerule);
                if (gameRule == null) {
                    plugin.getLogger().warning("Invalid gamerule: " + gamerule + " for world: " + worldName + " in gamerules.yml. Skipping...");
                    continue;
                }

                String value = gameruleConfig.getString(worldName + "." + gamerule);
                if (value == null) {
                    plugin.getLogger().warning("Invalid value for gamerule: " + gamerule + " for world: " + worldName + " in gamerules.yml. Skipping...");
                    continue;
                }

                Boolean result = GameruleUtil.setTypedGamerule(world, gameRule, value);
                if (!result) {
                    plugin.getLogger().warning("Failed to set the gamerule: " + gamerule + " to " + value + " for world: " + worldName);
                    continue;
                }
                plugin.getLogger().info("Successfully set gamerule: " + gamerule + " to " + value + " for world: " + worldName);
            }
        }
    }

    public <T> @NotNull Boolean saveGamerule(@NotNull String worldName, @NotNull String gameruleName, @NotNull GameRule<T> gamerule, @NotNull String gameruleValue) {
        try {
            T typedValue = GameruleUtil.getTypedGameruleValue(gamerule, gameruleValue);
            gameruleConfig.set(worldName + "." + gameruleName, typedValue);
            gameruleConfig.save(gameruleFile);
            return true;
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            plugin.getLogger().severe("Unable to save gamerule: " + gamerule + " to file");
            plugin.getLogger().severe("Reason: " + e.getMessage());
            return false;
        }
    }
}
