package com.github.samlam140330.restoregamerule.manager;

import com.github.samlam140330.restoregamerule.Restoregamerule;
import com.github.samlam140330.restoregamerule.utils.UpdateChecker;
import org.jetbrains.annotations.NotNull;

public class UpdateManager {
    private final Restoregamerule plugin;

    public UpdateManager(@NotNull Restoregamerule plugin, @NotNull FileManager fileManager) {
        this.plugin = plugin;
        fileManager.createDefaultYMLFile();
    }

    public void checkUpdate() {
        final boolean shouldCheckUpdate = plugin.getConfig().getBoolean("check-update");
        if (!shouldCheckUpdate) {
            plugin.getLogger().info("Update checking is disabled.");
            return;
        }

        final int resourceId = 120772;
        new UpdateChecker(plugin, resourceId).getVersion(version -> {
            if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                plugin.getLogger().info("You are running the latest version: " + plugin.getDescription().getVersion());
            } else {
                plugin.getLogger().warning("There is a new version available in SpigotMC: " + version);
            }
        });
    }
}
