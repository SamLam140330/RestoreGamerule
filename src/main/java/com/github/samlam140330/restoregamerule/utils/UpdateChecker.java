package com.github.samlam140330.restoregamerule.utils;

import com.github.samlam140330.restoregamerule.Restoregamerule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private final Restoregamerule plugin;
    private final int resourceId;

    public UpdateChecker(@NotNull Restoregamerule plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Error on checking the plugin update");
            }
        });
    }
}
