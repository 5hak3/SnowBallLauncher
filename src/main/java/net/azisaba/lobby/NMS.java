package net.azisaba.lobby;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class NMS {
    public static final Class<?> class_nms_EntityPlayer;
    public static final Class<?> class_nms_EntitySnowball;
    public static final Class<?> class_nms_DamageSource;
    public static final Class<?> class_obc_CraftPlayer;
    public static final Class<?> class_obc_CraftSnowball;
    public static final Class<?> class_nms_EntityLiving;
    public static final Class<?> class_nms_World;
    public static final Class<?> class_nms_CombatTracker;

    public static final Method method_CraftPlayer_getHandle;
    public static final Method method_CraftSnowball_getHandle;
    public static final Method method_EntitySnowball_getShooter;
    public static final Method method_DamageSource_projectile;
    public static final Method method_EntityPlayer_killEntity;
    public static final Method method_EntityPlayer_getCombatTracker;
    public static final Method method_CombatTracker_trackDamage;
    public static final Method method_EntityPlayer_getAbsorptionHearts;
    public static final Method method_EntityPlayer_setAbsorptionHearts;
    public static final Method method_World_broadcastEntityEffect;

    public static final Field field_EntityPlayer_world;


    static {
        try {
            class_nms_EntityPlayer = Class.forName("net.minecraft.server.v1_12_R1.EntityPlayer");
            class_nms_EntitySnowball = Class.forName("net.minecraft.server.v1_12_R1.EntitySnowball");
            class_nms_DamageSource = Class.forName("net.minecraft.server.v1_12_R1.DamageSource");
            class_obc_CraftPlayer = Class.forName("org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer");
            class_obc_CraftSnowball = Class.forName("org.bukkit.craftbukkit.v1_12_R1.entity.CraftSnowBall");
            class_nms_EntityLiving = Class.forName("net.minecraft.server.v1_12_R1.EntityLiving");
            class_nms_World = Class.forName("net.minecraft.server.v1_12_R1.World");
            class_nms_CombatTracker = Class.forName("net.minecraft.server.v1_12_R1.CombatTracker");

            method_CraftPlayer_getHandle = class_obc_CraftPlayer.getMethod("getHandle");
            method_CraftSnowball_getHandle = class_obc_CraftSnowball.getMethod("getHandle");
            method_EntitySnowball_getShooter = class_nms_EntitySnowball.getMethod("getShooter");
            method_DamageSource_projectile = class_nms_DamageSource.getMethod("projectile", class_nms_EntitySnowball, class_nms_EntityLiving);
            method_EntityPlayer_killEntity = class_nms_EntityPlayer.getMethod("killEntity");
            method_EntityPlayer_getCombatTracker = class_nms_EntityPlayer.getMethod("getCombatTracker");
            method_CombatTracker_trackDamage = class_nms_CombatTracker.getMethod("trackDamage", class_nms_DamageSource, Float.class, Float.class);
            method_EntityPlayer_getAbsorptionHearts = class_nms_EntityPlayer.getMethod("getAbsorptionHearts");
            method_EntityPlayer_setAbsorptionHearts = class_nms_EntityPlayer.getMethod("setAbsorptionHearts", Float.class);
            method_World_broadcastEntityEffect = class_nms_World.getMethod("broadcastEntityEffect", class_nms_World, Byte.class);

            field_EntityPlayer_world = class_nms_EntityPlayer.getField("world");

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}