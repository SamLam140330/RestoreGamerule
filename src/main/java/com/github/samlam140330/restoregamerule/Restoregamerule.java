package com.github.samlam140330.restoregamerule;

import com.github.samlam140330.restoregamerule.handler.CommandExecutorHandler;
import com.github.samlam140330.restoregamerule.handler.TabCompletionHandler;
import com.github.samlam140330.restoregamerule.manager.FileManager;
import com.github.samlam140330.restoregamerule.manager.GameruleManager;
import com.github.samlam140330.restoregamerule.manager.UpdateManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Restoregamerule extends JavaPlugin {
    @Override
    public void onEnable() {
        FileManager fileManager = new FileManager(this);
        GameruleManager gameruleManager = new GameruleManager(this, fileManager, "gamerules.yml");
        gameruleManager.applyGamerules();
        registerCommands(gameruleManager);
        UpdateManager updateManager = new UpdateManager(this, fileManager);
        updateManager.checkUpdate();
    }

    private void registerCommands(GameruleManager gameruleManager) {
        CommandExecutorHandler commandExecutorHandler = new CommandExecutorHandler(gameruleManager);
        TabCompletionHandler tabCompletionHandler = new TabCompletionHandler();

        Objects.requireNonNull(getCommand("set-gamerule")).setExecutor(commandExecutorHandler);
        Objects.requireNonNull(getCommand("set-gamerule")).setTabCompleter(tabCompletionHandler);
    }
}
