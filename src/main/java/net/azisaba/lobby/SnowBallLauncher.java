package net.azisaba.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
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

    /**
     * 当たった人NMS使ってダメージを無理矢理入れるやつ
     * @param event PrjHitEv
     */
    @EventHandler
    public void onHit (ProjectileHitEvent event) {
        Entity prj = event.getEntity();
        if (!(prj instanceof Snowball)) return;

        prj.getWorld().spawnParticle(Particle.BLOCK_CRACK, prj.getLocation(), 10, 0, 0, 0, 1.0F, new MaterialData(Material.ICE));
        if (event.getHitBlock() != null)
            prj.getWorld().playSound(prj.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5F, 5.0F);

        if (!(event.getHitEntity() instanceof Player)) return;
        Player hitted = (Player) (event.getHitEntity());

        if(hitted.getInventory().getItemInMainHand().getType() != Material.SNOW_BALL) return;

        double damage = 1.0;
        double health = hitted.getHealth();

        // この辺からNMSでいい感じにやる (Walkureのパクリ)
        Object player = null;
        try {
            player = NMS.method_CraftPlayer_getHandle.invoke(
                    hitted
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Object snowball = null;
        try {
            snowball = NMS.method_CraftSnowball_getHandle.invoke(
                    prj
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Object damageSource = null;
        try {
            damageSource = NMS.method_DamageSource_projectile.invoke(
                    null,
                    snowball,
                    NMS.method_EntitySnowball_getShooter.invoke(
                            snowball
                    )
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (health <= damage) {
            try {
                NMS.method_EntityPlayer_killEntity.invoke(
                        player
                );
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }

        hitted.setHealth(health - damage);
        try {
            NMS.method_CombatTracker_trackDamage.invoke(
                    NMS.method_EntityPlayer_getCombatTracker.invoke(player),
                    damageSource,
                    (float)health,
                    (float)damage
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            NMS.method_EntityPlayer_setAbsorptionHearts.invoke(
                    player,
                    (float)(NMS.method_EntityPlayer_getAbsorptionHearts.invoke(player)) - (float)damage
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            NMS.method_World_broadcastEntityEffect.invoke(
                    NMS.field_EntityPlayer_world.get(player),
                    player,
                    (byte)2
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        hitted.setNoDamageTicks(0);
    }

    /**
     * ないとダメージ判定がおかしくなるみたいなので
     * @param event EntDmgByEntEv
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof  Player && event.getDamager() instanceof Snowball)) return;
        event.setCancelled(true);
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
