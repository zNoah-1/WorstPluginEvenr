package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerTeleport {
    public static void teleport(CommandSender sender, String world, double X, double Y, double Z){
        Player player = (Player) sender;
        Location loc1 = new Location(Bukkit.getWorld(world), X, Y, Z, 2, 2);
        player.teleport(loc1);
    }

    public static void teleport(Player player, String world, double X, double Y, double Z, float Yaw, float Pitch){
        Location loc1 = new Location(Bukkit.getWorld(world), X, Y, Z, Yaw, Pitch);
        player.teleport(loc1);
    }

    public static void teleport(Player player, String world, double X, double Y, double Z){
        Location loc1 = new Location(Bukkit.getWorld(world), X, Y, Z);
        player.teleport(loc1);
    }
}

