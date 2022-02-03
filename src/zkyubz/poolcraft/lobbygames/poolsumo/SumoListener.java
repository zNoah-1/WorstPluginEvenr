package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static zkyubz.poolcraft.lobbygames.poolsumo.ColorMgr.color;

public class SumoListener {
    static int counter = -1;
    static boolean playerOneDisconnect = false;
    static boolean playerTwoDisconnect = false;

    public static void init(){
        playerOneDisconnect = false;
        playerTwoDisconnect = false;
        ListPlayers.onMatch[0] = (String) ListPlayers.onQueue.get(0);
        ListPlayers.onMatch[1] = (String) ListPlayers.onQueue.get(1);
        ListPlayers.onQueue.remove(1);
        ListPlayers.onQueue.remove(0);

        Player p1 = Bukkit.getPlayer(ListPlayers.onMatch[0]);
        Player p2 = Bukkit.getPlayer(ListPlayers.onMatch[1]);

        PlayerTeleport.teleport(p1, ConfigStorage.lobbyWorldName, ConfigStorage.spawn1X, ConfigStorage.spawn1Y, ConfigStorage.spawn1Z, ConfigStorage.spawn1Yaw, ConfigStorage.spawn1Pitch); //Reemplazar con el spawn1
        PlayerTeleport.teleport(p2, ConfigStorage.lobbyWorldName, ConfigStorage.spawn2X, ConfigStorage.spawn2Y, ConfigStorage.spawn2Z, ConfigStorage.spawn2Yaw, ConfigStorage.spawn2Pitch); //Reemplazar con el spawn2

        InvManager.save(Bukkit.getPlayer(ListPlayers.onMatch[0]), Bukkit.getPlayer(ListPlayers.onMatch[1]));
        p1.setGameMode(GameMode.SURVIVAL);
        p2.setGameMode(GameMode.SURVIVAL);
        p1.setAllowFlight(false);
        p2.setAllowFlight(false);

        counter = 0;

        new BukkitRunnable() {
            private int Countdown = 5;

            @Override
            public void run() {
                if (!playerOneDisconnect && !playerTwoDisconnect){
                    if (Countdown > 1){

                        TitleAPI.sendTitle(p1,4,16,0, ConfigStorage.countdownTitleTop.replace("%count%", String.valueOf(Countdown)), ConfigStorage.countdownTitleBottom);
                        p1.sendMessage(color(ConfigStorage.countdownChatCount.replace("%count%", String.valueOf(Countdown))));
                        p1.playSound(p1.getLocation(), Sound.CLICK, 1f, 1f);

                        TitleAPI.sendTitle(p2,4,16,0, ConfigStorage.countdownTitleTop.replace("%count%", String.valueOf(Countdown)), ConfigStorage.countdownTitleBottom);
                        p2.sendMessage(color(ConfigStorage.countdownChatCount.replace("%count%", String.valueOf(Countdown))));
                        p2.playSound(p2.getLocation(), Sound.CLICK, 1f, 1f);
                        --Countdown;
                    }
                    else if (Countdown == 1){
                        TitleAPI.sendTitle(p1,4,16,0, ConfigStorage.countdownTitleTopLast.replace("%count%", String.valueOf(Countdown)), ConfigStorage.countdownTitleBottomLast);
                        p1.sendMessage(color(ConfigStorage.countdownChatCountLast.replace("%count%", String.valueOf(Countdown))));
                        p1.playSound(p1.getLocation(), Sound.CLICK, 1f, 1f);

                        TitleAPI.sendTitle(p2,4,16,0, ConfigStorage.countdownTitleTopLast.replace("%count%", String.valueOf(Countdown)), ConfigStorage.countdownTitleBottomLast);
                        p2.sendMessage(color(ConfigStorage.countdownChatCountLast.replace("%count%", String.valueOf(Countdown))));
                        p2.playSound(p2.getLocation(), Sound.CLICK, 1f, 1f);
                        --Countdown;
                    }
                    else{
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), ConfigStorage.matchStartCommand.replace("%player%", ListPlayers.onMatch[0]));
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), ConfigStorage.matchStartCommand.replace("%player%", ListPlayers.onMatch[1]));
                        TitleAPI.sendTitle(p1,4,16,0, ConfigStorage.countdownTitleTopStart, ConfigStorage.countdownTitleBottomStart);
                        p1.sendMessage(color(ConfigStorage.matchStartMessage.replace("%opponent%", ListPlayers.onMatch[1])));
                        TitleAPI.sendTitle(p2,4,16,0, ConfigStorage.countdownTitleTopStart, ConfigStorage.countdownTitleBottomStart);
                        p2.sendMessage(color(ConfigStorage.matchStartMessage.replace("%opponent%", ListPlayers.onMatch[0])));
                        this.cancel();
                    }
                }
                /*else if (playerTwoDisconnect){
                    //p1.sendMessage(color("&cParece que " + ListPlayers.onMatch[1] + " se fue de la partida")); //Añadir a messages.yml
                    this.cancel();
                } */
                else {
                    //p2.sendMessage(color("&cParece que " + ListPlayers.onMatch[0] + " se fue de la partida")); //Añadir a messages.yml
                    this.cancel();
                }
            }
        }.runTaskTimer(ThisPlugin.getInstance(), 0L, 20L);


        new BukkitRunnable() {
            boolean second;
            @Override
            public void run() {
                if (p1.getLocation().getBlockY() < ConfigStorage.yDeath || playerOneDisconnect){ //Reemplazar con el Y correspondiente
                    MatchEnd.init(ListPlayers.onMatch[1]);
                    this.cancel();
                }
                else if (p2.getLocation().getBlockY() < ConfigStorage.yDeath || playerTwoDisconnect){ //Reemplazar con el Y correspondiente
                    MatchEnd.init(ListPlayers.onMatch[0]);
                    this.cancel();
                }
                else{
                    if (second){
                        ++counter;
                        second = false;
                    }
                    else{
                        second = true;
                    }
                }
            }
        }.runTaskTimer(ThisPlugin.getInstance(), 0L, 10L);
    }
}
