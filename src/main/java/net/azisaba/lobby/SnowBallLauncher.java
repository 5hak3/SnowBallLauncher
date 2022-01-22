package net.azisaba.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

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
        Player pl = event.getPlayer();
        if (item == null) return;
        if (item.getType() == Material.SNOW_BALL) {
            event.setCancelled(true);
            if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            pl.launchProjectile(Snowball.class);
            pl.getWorld().playSound(pl.getLocation(), Sound.BLOCK_GRASS_PLACE, 0.5F, 0.5F);
        }
    }

    @EventHandler
    public void onHit (ProjectileHitEvent event) {
        Entity prj = event.getEntity();
        prj.getWorld().spawnParticle(Particle.BLOCK_CRACK, prj.getLocation(), 10, 0, 0, 0, 1.0F, new MaterialData(Material.ICE));
        if (event.getHitBlock() != null)
            prj.getWorld().playSound(prj.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5F, 5.0F);
    }

    /**
     * めっちゃ投げられるやつを渡す
     * @param event PLinE
     */
    @EventHandler
    public void onLogin (PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskLater(
                this, () -> event.getPlayer().getInventory().setItem(1, sball), 20);
    }
}
