package com.github.samlam140330.restoregamerule.utils;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GameruleUtil {
    public static GameRule<?> getTypedGamerule(@NotNull String gamerule) {
        return GameRule.getByName(gamerule);
    }

    @Contract(pure = true)
    public static @NotNull Object getGameruleClass(@NotNull GameRule<?> gamerule) {
        return gamerule.getType();
    }

    public static <T> @NotNull Boolean setTypedGamerule(@NotNull World world, @NotNull GameRule<T> gamerule, @NotNull String value) {
        try {
            T typedValue = getTypedGameruleValue(gamerule, value);
            return world.setGameRule(gamerule, typedValue);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Contract("_, _ -> param2")
    public static <T> T getTypedGameruleValue(@NotNull GameRule<T> gamerule, @NotNull String value) {
        try {
            if (gamerule.getType().equals(Boolean.class)) {
                if (!value.equals("true") && !value.equals("false")) {
                    throw new IllegalArgumentException("Invalid value for boolean gamerule: " + value);
                }
                return gamerule.getType().cast(Boolean.parseBoolean(value));
            } else {
                return gamerule.getType().cast(Integer.parseInt(value));
            }
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for gamerule: " + value);
        }
    }
}
