package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.plugin.java.JavaPlugin;

public class MainShit extends JavaPlugin {
    @Override
    public void onEnable(){
        ThisPlugin.setInstance(this);
        this.getCommand("sumo").setExecutor(new PlayerQueue());
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

        ConfigInfo.init();
    }

}
