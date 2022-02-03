package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static zkyubz.poolcraft.lobbygames.poolsumo.ColorMgr.color;

public class PlayerEventListener implements Listener {

    @EventHandler()
    public void onPlayerTeleport(PlayerTeleportEvent event){
        //Si un jugador se teletransporta
        Player player = event.getPlayer();
        //if counter >= 5
        //if (SumoListener.counter != -1 && player.getWorld().getName().equals(ConfigStorage.lobbyWorldName)){
        if (SumoListener.counter != -1){
            if (player.getName().equals(ListPlayers.onMatch[0]) && !isTeleportValid(event.getTo(), 0)){
                playerDSQ(player, 0);
                //System.out.println("0 Result: " + event.getTo().getX() + " " + event.getTo().getY() + " " + event.getTo().getZ());
                //System.out.println("0 Expected: " + ConfigStorage.spawn1X + " " + ConfigStorage.spawn1Y + " " + ConfigStorage.spawn1Z);
            }
            else if (player.getName().equals(ListPlayers.onMatch[1]) && !isTeleportValid(event.getTo(), 1)){
                playerDSQ(player, 1);
                //System.out.println("1 Result: " + event.getTo().getX() + " " + event.getTo().getY() + " " + event.getTo().getZ());
                //System.out.println("1 Expected: " + ConfigStorage.spawn2X + " " + ConfigStorage.spawn2Y + " " + ConfigStorage.spawn2Z);
            }
        }

        //}
        //Espera 2 ticks para verificar si al final se teletransporto a otro mapa
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ListPlayers.onQueue.contains(player.getName()) && !player.getWorld().getName().equals(ConfigStorage.lobbyWorldName)){
                    player.sendMessage(color(ConfigStorage.leaveQueue)); //"Te has salido de la cola del sumo"
                    ListPlayers.onQueue.remove(player.getName());
                }
            }
        }.runTaskLater(ThisPlugin.getInstance(), 2L);
    }

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event){
        //Remover al jugador de la cola y de la lista de jugadores en partida del sumo
        Player player = (Player) event.getPlayer();
        if (player.getName().equals(ListPlayers.onMatch[0])){
            SumoListener.playerOneDisconnect = true;
        }
        else if (player.getName().equals(ListPlayers.onMatch[1])){
            SumoListener.playerTwoDisconnect = true;
        }
        ListPlayers.onQueue.remove(player.getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if ((p.getName().equals(ListPlayers.onMatch[0]) || p.getName().equals(ListPlayers.onMatch[1])) && e.getMessage().contains("/")) {
            e.getPlayer().sendMessage(color(ConfigStorage.executeCommand)); //"No puedes ejecutar comandos en partida!"
            e.setCancelled(true);
        }
    }

    @EventHandler()
    public void onPlayerMove(PlayerMoveEvent event){
        //Evitar que el jugador se mueva antes de la partida
        Player player = event.getPlayer();
        if (SumoListener.counter < 5 && SumoListener.counter > -1){
            if (player.getName().equals(ListPlayers.onMatch[0])){
                if (player.getLocation().getBlockX() != ConfigStorage.spawn1X || player.getLocation().getBlockZ() != ConfigStorage.spawn1Z){
                    PlayerTeleport.teleport(player, ConfigStorage.lobbyWorldName, ConfigStorage.spawn1X, ConfigStorage.spawn1Y, ConfigStorage.spawn1Z, ConfigStorage.spawn1Yaw, ConfigStorage.spawn1Pitch); //Teletransporta al jugador al punto 1... ("Hub", 0.5, 58, 33.5, 0, 0)
                }
            }
            else if (player.getName().equals(ListPlayers.onMatch[1])){
                if (player.getLocation().getBlockX() != ConfigStorage.spawn2X || player.getLocation().getBlockZ() != ConfigStorage.spawn2Z){
                    PlayerTeleport.teleport(player, ConfigStorage.lobbyWorldName, ConfigStorage.spawn2X, ConfigStorage.spawn2Y, ConfigStorage.spawn2Z, ConfigStorage.spawn2Yaw, ConfigStorage.spawn2Pitch);  //Teletransporta al jugador al punto 2... ("Hub", 0.5, 58, 41.5, -180, 0)
                }
            }
        }
    }

    @EventHandler()
    public void onDamage(EntityDamageByEntityEvent event) {
        //Reduce el daño a 0
        Entity entity = (Entity) event.getDamager();
        if (entity instanceof Player) {
            if (entity.getName().equals(ListPlayers.onMatch[0]) || entity.getName().equals(ListPlayers.onMatch[1])){
                event.setDamage(0);
            }
        }
    }

    public void playerCheck(Player PlayerInvolved){
        if (!PlayerInvolved.getWorld().getName().equals(ConfigStorage.lobbyWorldName)){
            ListPlayers.onQueue.remove(PlayerInvolved.getName());
            PlayerInvolved.sendMessage(color(ConfigStorage.leaveQueue)); //"Te has salido de la cola del sumo"
        }
    }

    public void playerDSQ(Player PlayerInvolved, int playerNum){
        PlayerInvolved.sendMessage(color(ConfigStorage.exitMatchBeforeEnding)); //"Te has salido de la partida del sumo"
        if (playerNum == 0){
            SumoListener.playerOneDisconnect = true;
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), ConfigStorage.matchEndCommand.replace("%player%", ListPlayers.onMatch[0]));
            //ListPlayers.onMatch[0] = "";
        }
        else{
            SumoListener.playerTwoDisconnect = true;
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), ConfigStorage.matchEndCommand.replace("%player%", ListPlayers.onMatch[1]));
            //ListPlayers.onMatch[1] = "";
        }
        //PlayerTeleport.teleport(PlayerInvolved, ConfigStorage.lobbyWorldName, ConfigStorage.lobbyX, ConfigStorage.lobbyY, ConfigStorage.lobbyZ, ConfigStorage.lobbyYaw, ConfigStorage.lobbyPitch); //Teletransporta al jugador al spawn ("Hub", 0.5, 60, 14.5, 0, 0)
    }

    public boolean isTeleportValid(Location loc, int playerNum) {
        if (playerNum == 0){
            //System.out.println("0: " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
            return loc.getWorld().getName().equals(ConfigStorage.lobbyWorldName) && loc.getX() == ConfigStorage.spawn1X && loc.getY() == ConfigStorage.spawn1Y && loc.getZ() == ConfigStorage.spawn1Z;
        }
        else {
            //System.out.println("1: " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
            return loc.getWorld().getName().equals(ConfigStorage.lobbyWorldName) && loc.getX() == ConfigStorage.spawn2X && loc.getY() == ConfigStorage.spawn2Y && loc.getZ() == ConfigStorage.spawn2Z;
        }
    }
}
