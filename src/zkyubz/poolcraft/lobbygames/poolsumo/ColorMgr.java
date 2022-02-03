package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.ChatColor;

public class ColorMgr {
    public static String color(String string) {
        System.out.println(string);
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

