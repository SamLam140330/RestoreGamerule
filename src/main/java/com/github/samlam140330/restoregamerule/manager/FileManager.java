package com.github.samlam140330.restoregamerule.manager;

import com.github.samlam140330.restoregamerule.Restoregamerule;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileManager {
    private final Restoregamerule plugin;

    public FileManager(Restoregamerule plugin) {
        this.plugin = plugin;
    }

    public void createDefaultYMLFile() {
        plugin.saveDefaultConfig();
    }

    public void createYMLFile(@NotNull String fileName) {
        File configFile = getFile(fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public @NotNull File getFile(@NotNull String fileName) {
        if (!fileName.endsWith(".yml")) {
            fileName += ".yml";
        }
        return new File(plugin.getDataFolder(), fileName);
    }

    public YamlConfiguration getYamlConfig(@NotNull File configFile) {
        try {
            return YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load the configuration file: " + configFile.getName());
            plugin.getLogger().severe("Reason: " + e.getMessage());
            return null;
        }
    }
}
