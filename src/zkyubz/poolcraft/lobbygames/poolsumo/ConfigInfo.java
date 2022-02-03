package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static org.bukkit.Bukkit.getServer;

public class ConfigInfo {

    public static void init(){
        File configYAML = new File( "plugins/PoolSumo", "config.yml");
        File messagesYAML = new File( "plugins/PoolSumo", "messages.yml");

        FileConfiguration configConf = new YamlConfiguration();
        FileConfiguration messagesConf = new YamlConfiguration();

        if (!messagesYAML.exists()) {
            messagesYAML.getParentFile().mkdirs();
            saveResource(messagesConf, messagesYAML, 0);
        }

        if (!configYAML.exists()) {
            messagesYAML.getParentFile().mkdirs();
            saveResource(configConf, configYAML, 1);
        }

        assignValues(configConf, messagesConf, configYAML, messagesYAML);
    }

    private static void saveResource(FileConfiguration config, File file, int fileNum) {
        switch(fileNum) {
            case 0:
                config.set("command.queue.enter", "&a[O] ¡Haz sido añadido a la cola del sumo!");
                config.set("command.queue.leave", "&c[!] ¡Te has salido de la cola del sumo!");
                config.set("command.error.wrong-world", "&c[!] ¡Tienes que estar en el lobby para entrar a sumo!");
                config.set("command.error.unknown-subcommand", "&e[¡] Subcomando desconocido");
                config.set("command.error.already-on-queue", "&c[!] ¡Ya estás en la cola!");
                config.set("command.error.execute-command", "&c[!] ¡No puedes ejecutar comandos en partida!");
                config.set("command.error.teleport-while-on-match", "&c[!] ¡Te has salido de la partida del sumo!");
                config.set("command.error.player-disqualified", "&cParece que %opponent% se fue de la partida!");

                config.set("countdown-chat.count", "&fLa partida comenzará en: &a%count% segundos.");
                config.set("countdown-chat.count-last", "&fLa partida comenzará en: &a%count% segundo.");

                config.set("countdown-title.top", "&f%count%");
                config.set("countdown-title.top-last", "&fListo?");
                config.set("countdown-title.top-start", "&fGo!");
                config.set("countdown-title.bottom", "&aPoolSumo");
                config.set("countdown-title.bottom-last", "&aPoolSumo");
                config.set("countdown-title.bottom-start", "&aPoolSumo");


                config.set("match.start.message", "&aPeleando contra %opponent%!");
                config.set("match.start.command", "pvpwl add %player%");
                config.set("match.end.broadcast-message", "&f%winner% &aganó una partida de PoolSumo!");
                config.set("match.end.command", "pvpwl remove %player%");

                break;

            case 1:
                config.set("lobby.world-name", "Hub");
                config.set("lobby.coordinates.x", 0.00);
                config.set("lobby.coordinates.y", 0.00);
                config.set("lobby.coordinates.z", 0.00);
                config.set("lobby.coordinates.yaw", 0.00);
                config.set("lobby.coordinates.pitch", 0.00);

                config.set("spawn.1.x", 0.00);
                config.set("spawn.1.y", 0.00);
                config.set("spawn.1.z", 0.00);
                config.set("spawn.1.yaw", 0.00);
                config.set("spawn.1.pitch", 0.00);

                config.set("spawn.2.x", 0.00);
                config.set("spawn.2.y", 0.00);
                config.set("spawn.2.z", 0.00);
                config.set("spawn.2.yaw", 0.00);
                config.set("spawn.2.pitch", 0.00);

                config.set("parameters.y-death", 0.00);

                break;
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void assignValues(FileConfiguration config2, FileConfiguration config1, File f2, File f1){
        try {
            config1.load(f1);
            config2.load(f2);

            ConfigStorage.enterQueue = config1.getString("command.queue.enter");
            ConfigStorage.leaveQueue = config1.getString("command.queue.leave");
            ConfigStorage.wrongWorld = config1.getString("command.error.wrong-world");
            ConfigStorage.unknownSubCommand = config1.getString("command.error.unknown-subcommand");
            ConfigStorage.alreadyOnQueue = config1.getString("command.error.already-on-queue");
            ConfigStorage.executeCommand = config1.getString("command.error.execute-command");
            ConfigStorage.exitMatchBeforeEnding = config1.getString("command.error.teleport-while-on-match");
            ConfigStorage.playerDisqualified = config1.getString("command.error.player-disqualified");

            ConfigStorage.countdownChatCount = config1.getString("countdown-chat.count");
            ConfigStorage.countdownChatCountLast = config1.getString("countdown-chat.count-last");

            ConfigStorage.countdownTitleTop = config1.getString("countdown-title.top");
            ConfigStorage.countdownTitleTopLast = config1.getString("countdown-title.top-last");
            ConfigStorage.countdownTitleTopStart = config1.getString("countdown-title.top-start");
            ConfigStorage.countdownTitleBottom = config1.getString("countdown-title.bottom");
            ConfigStorage.countdownTitleBottomLast = config1.getString("countdown-title.bottom-last");
            ConfigStorage.countdownTitleBottomStart = config1.getString("countdown-title.bottom-start");

            ConfigStorage.matchStartMessage = config1.getString("match.start.message");
            ConfigStorage.matchStartCommand = config1.getString("match.start.command");
            ConfigStorage.matchEndBroadcastMessage = config1.getString("match.end.broadcast-message");
            ConfigStorage.matchEndCommand = config1.getString("match.end.command");


            ConfigStorage.lobbyWorldName = config2.getString("lobby.world-name");
            ConfigStorage.lobbyX = config2.getDouble("lobby.coordinates.x");
            ConfigStorage.lobbyY = config2.getDouble("lobby.coordinates.y");
            ConfigStorage.lobbyZ = config2.getDouble("lobby.coordinates.z");
            ConfigStorage.lobbyYaw = (float) config2.getDouble("lobby.coordinates.yaw");
            ConfigStorage.lobbyPitch = (float) config2.getDouble("lobby.coordinates.pitch");

            ConfigStorage.spawn1X = config2.getDouble("spawn.1.x");
            ConfigStorage.spawn1Y = config2.getDouble("spawn.1.y");
            ConfigStorage.spawn1Z = config2.getDouble("spawn.1.z");
            ConfigStorage.spawn1Yaw = (float) config2.getDouble("spawn.1.yaw");
            ConfigStorage.spawn1Pitch = (float) config2.getDouble("spawn.1.pitch");

            ConfigStorage.spawn2X = config2.getDouble("spawn.2.x");
            ConfigStorage.spawn2Y = config2.getDouble("spawn.2.y");
            ConfigStorage.spawn2Z = config2.getDouble("spawn.2.z");
            ConfigStorage.spawn2Yaw = (float) config2.getDouble("spawn.2.yaw");
            ConfigStorage.spawn2Pitch = (float) config2.getDouble("spawn.2.pitch");

            ConfigStorage.yDeath = config2.getDouble("parameters.y-death");
        }
        catch (IOException | InvalidConfigurationException e){
            getServer().getConsoleSender().sendMessage(String.valueOf(e));
            getServer().getConsoleSender().sendMessage("");
            getServer().getConsoleSender().sendMessage("Hubo un error al leer el archivo de configuracion!!! :( Envia el codigo de error de arriba a Nuannn!");
            return;
        }
    }

}
