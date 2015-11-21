package me.porterk.pods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.Overridden;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Kalob on 11/18/2015.
 */
public class PodsMain extends JavaPlugin{

    PodsCommand PodsCommand = new PodsCommand(this);
    private static PodsMain plugin;
    public Inventory worldSelect;
    BukkitTask high;

    public void onEnable() {

        worldSelect = getServer().createInventory(null, 54, ChatColor.DARK_RED + "Teleportation Dock");

        FileConfiguration config = getConfig();
        config.options().header("You may now remove the 'coords' area, Jukebox's are kept by ID's now!");
        saveConfig();

        getServer().getPluginManager().registerEvents(new PodsListener(this), this);

        getLogger().log(Level.INFO, "Pods enabled!");

        getCommand("pods").setExecutor(PodsCommand);

    }

    public void onDisable(){

        getLogger().log(Level.INFO, "Pods disabled!");

    }

    public void addItem(Material m, Inventory i, String name)
    {
        ItemStack a = new ItemStack(m, 1);
        ItemMeta b = a.getItemMeta();
        b.setDisplayName(ChatColor.GOLD + name);
        a.setItemMeta(b);
        i.addItem(a);
    }

    public void launch(Player p){

            Vector v = p.getVelocity();
            v.setY(1000);
            p.setVelocity(v);
            v.setY(0);
            p.sendMessage(ChatColor.GREEN + "Launched boy!");

    }

    public void openWorldSelect(Player p){

        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY() + 100;
        int z = l.getBlockZ();
        int floorRadius = 2;
        for(int xCoord = x - floorRadius; xCoord < x + floorRadius; xCoord++)
            for(int zCoord = z - floorRadius; zCoord < z + floorRadius; zCoord++) {
                p.sendBlockChange(new Location(l.getWorld(), xCoord, y, zCoord), Material.BARRIER, (byte) 0);
            }

        FileConfiguration config = getConfig();
        for(String key : config.getConfigurationSection("ID").getKeys(false)){
            addItem(Material.STONE, worldSelect, key);
        }

        p.teleport(new Location(p.getWorld(), x, y + 2, z));
        p.openInventory(worldSelect);
        Bukkit.getScheduler().cancelAllTasks();

    }

    public void removeFloat(Player p){

        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY() - 1;
        int z = l.getBlockZ();
        int floorRadius = 2;
        for(int xCoord = x - floorRadius; xCoord < x + floorRadius; xCoord++)
            for(int zCoord = z - floorRadius; zCoord < z + floorRadius; zCoord++) {
                p.sendBlockChange(new Location(l.getWorld(), xCoord, y, zCoord), Material.AIR, (byte) 0);
            }

    }
    public boolean locationIsInCuboid(Location playerLocation, Location min, Location max) {
        boolean trueOrNot = false;
        if (playerLocation.getWorld() == min.getWorld() && playerLocation.getWorld() == max.getWorld()) {
            if (playerLocation.getX() >= min.getX() && playerLocation.getX() <= max.getX()) {
                if (playerLocation.getY() >= min.getY() && playerLocation.getY() <= max.getY()) {
                    if (playerLocation.getZ() >= min.getZ()
                            && playerLocation.getZ() <= max.getZ()) {
                        trueOrNot = true;
                    }
                }
            }
            if (playerLocation.getX() <= min.getX() && playerLocation.getX() >= max.getX()) {
                if (playerLocation.getY() <= min.getY() && playerLocation.getY() >= max.getY()) {
                    if (playerLocation.getZ() <= min.getZ()
                            && playerLocation.getZ() >= max.getZ()) {
                        trueOrNot = true;
                    }
                }
            }
        }
        return trueOrNot;
    }
}
