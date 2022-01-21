package net.azisaba.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public final class SnowBallLauncher extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
    }

    /**
     * めっちゃ投げられるやつの定義
     */
    final private static ItemStack sball = new ItemStack(Material.SNOW_BALL);
    static {
        ItemMeta meta = sball.getItemMeta();
        meta.setDisplayName("めっちゃ投げられるやつ");
        meta.setLore(Arrays.asList("めっちゃ投げよう！"));
        sball.setItemMeta(meta);
    }

    /**
     * めっちゃ投げられるやつを投げたとき返してあげるやつ
     * @param event PIntE
     */
    @EventHandler
    public void onThrow (PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (item.getType() == Material.SNOW_BALL) {
            player.launchProjectile(Snowball.class);
            event.setCancelled(true);
        }
    }

    /**
     * めっちゃ投げられるやつを渡す
     * @param event PLinE
     */
    @EventHandler
    public void onLogin (PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskLater(
                this, () -> {event.getPlayer().getInventory().addItem(sball);}, 20 * 1);
    }
}
