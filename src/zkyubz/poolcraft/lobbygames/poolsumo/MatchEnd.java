package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static zkyubz.poolcraft.lobbygames.poolsumo.ColorMgr.color;

public class MatchEnd {
    public static void init (String Winner){
        SumoListener.counter = -1;
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), ConfigStorage.matchEndCommand.replace("%player%", ListPlayers.onMatch[0]));
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), ConfigStorage.matchEndCommand.replace("%player%", ListPlayers.onMatch[1]));

        if (SumoListener.playerTwoDisconnect){
            Bukkit.getPlayer(ListPlayers.onMatch[0]).sendMessage(color(ConfigStorage.playerDisqualified.replace("%opponent%", ListPlayers.onMatch[1])));
        }
        else if (SumoListener.playerOneDisconnect){
            Bukkit.getPlayer(ListPlayers.onMatch[1]).sendMessage(color(ConfigStorage.playerDisqualified.replace("%opponent%", ListPlayers.onMatch[0])));
        }

        Bukkit.getWorld(ConfigStorage.lobbyWorldName).getPlayers().forEach(player -> player.sendMessage(color(ConfigStorage.matchEndBroadcastMessage.replace("%winner%", Winner))));

        new BukkitRunnable() {
            @Override
            public void run() {
                teleportPlayersToSpawn(ListPlayers.onMatch);
                ListPlayers.onMatch[0] = "";
                ListPlayers.onMatch[1] = "";

                if (ListPlayers.onQueue.size() > 1){
                    SumoListener.init();
                }
            }
        }.runTaskLater(ThisPlugin.getInstance(), 100L);

    }

    public static void teleportPlayersToSpawn(String[] onMatch){
        Player p1, p2;
        if (!SumoListener.playerOneDisconnect){
            p1 = Bukkit.getPlayer(onMatch[0]);
            PlayerTeleport.teleport(p1, ConfigStorage.lobbyWorldName, ConfigStorage.lobbyX, ConfigStorage.lobbyY, ConfigStorage.lobbyZ, ConfigStorage.lobbyYaw, ConfigStorage.lobbyPitch);
            InvManager.load(p1, 0);
        }
        if (!SumoListener.playerTwoDisconnect){
            p2 = Bukkit.getPlayer(onMatch[1]);
            PlayerTeleport.teleport(p2, ConfigStorage.lobbyWorldName, ConfigStorage.lobbyX, ConfigStorage.lobbyY, ConfigStorage.lobbyZ, ConfigStorage.lobbyYaw, ConfigStorage.lobbyPitch);
            InvManager.load(p2, 1);
        }
    }
}
