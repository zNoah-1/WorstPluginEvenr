package zkyubz.poolcraft.lobbygames.poolsumo;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvManager {
    private static ItemStack[] p1inv;
    private static ItemStack[] p2inv;

    public static void save (Player p1, Player p2){
        p1inv = p1.getInventory().getContents();
        p2inv = p2.getInventory().getContents();

        clr(p1, p2);
    }

    public static void clr(Player p1, Player p2){
        p1.getInventory().clear();
        p2.getInventory().clear();
    }

    public static void load (Player p, int player){
        if (player == 0){
            p.getInventory().setContents(p1inv);
        }
        else {
            p.getInventory().setContents(p2inv);
        }
    }
}
