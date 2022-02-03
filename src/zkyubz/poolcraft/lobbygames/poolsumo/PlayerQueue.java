package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static zkyubz.poolcraft.lobbygames.poolsumo.ColorMgr.color;

public class PlayerQueue implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0){
            if (!ListPlayers.onQueue.contains(player.getName())){ //Si no está en la cola
                if (player.getWorld().getName().equals(ConfigStorage.lobbyWorldName)){ //Si esta en el hub
                    ListPlayers.onQueue.add(player.getName());
                    player.sendMessage(color(ConfigStorage.enterQueue)); //Mensaje entrando a la cola
                    if (ListPlayers.onQueue.size() > 1 && ListPlayers.onMatch[0].equals("") && ListPlayers.onMatch[1].equals("")){
                        SumoListener.init();
                    }
                }
                else { //Si no está en el lobby
                    System.out.println(ConfigStorage.wrongWorld);
                    player.sendMessage(color(ConfigStorage.wrongWorld));  //"Tienes que estar en el lobby para entrar a sumo"
                }
            }
            else{ //Si ya esta en la cola del sumo
                player.sendMessage(color(ConfigStorage.leaveQueue)); //"Te has salido de la cola del sumo"
                ListPlayers.onQueue.remove(player.getName());
            }
            /*else {
                player.sendMessage(color("&c[!] ¡Ya estás en la cola!"));
            }*/

        }

        else if (args[0].equals("help")){
            player.sendMessage(color("&e[?] ¡Por el momento solo hay \"/sumo\" ^^! Creado por Nuannn"));
        }

        else if (args[0].equals("ayuda")){
            player.sendMessage(color("&e[?] ¡Por el momento solo hay \"/sumo\" ^^! Creado por Nuannn"));
        }

        else{
            player.sendMessage(color(ConfigStorage.unknownSubCommand)); //"Subcomando desconocido"
        }
        return true;
    }
}