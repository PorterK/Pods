package me.porterk.pods;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kalob on 11/18/2015.
 */
public class PodsListener implements Listener {
    BukkitTask launch;
    int launchCount = 6;
    Boolean taskDidStart = false;
    Boolean taskIsRunning = false;
    private PodsMain plugin;

    public PodsListener(PodsMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        Player p = e.getPlayer();

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getClickedBlock().equals(Material.GOLD_BLOCK)){

                Vector v = p.getVelocity();

                v.setY(300);

                p.setVelocity(v);

            }


        }

    }

    @EventHandler
    public void onPlayerInventory(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        if(e.getInventory() == plugin.worldSelect){

            e.setCancelled(true);

            Block b = (Block) e.getClickedInventory();

            if(b != null){

                plugin.removeFloat(p);

               String w = b.getMetadata("DisplayName").get(0).toString();
                World world = Bukkit.getWorld(w);

                Random random = new Random();

                int x = random.nextInt(35) + 1;
                int z = random.nextInt(42) + 1;

                Location l = new Location(world, x, world.getHighestBlockYAt(x, z), z);

                p.teleport(l);

            }

        }


    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Boolean isOnBlock = false;

        Player p = e.getPlayer();
        FileConfiguration config = plugin.getConfig();
        for(String key : config.getConfigurationSection("ID").getKeys(false)){
            int x = config.getInt("ID." + key + ".x");
            int y = config.getInt("ID." + key + ".y");
            int z = config.getInt("ID." + key + ".z");
            String world = plugin.getConfig().getString("ID." + key + ".world");
            Block block = plugin.getServer().getWorld(world).getBlockAt(x, y, z);

            Location a = new Location(Bukkit.getWorld(world), x, y, z);
            a.setX(a.getX() + 1);
            a.setZ(a.getZ() + 1);

            Location b = new Location(Bukkit.getWorld(world), x, y, z);
            b.setX(b.getX() - 1);
            b.setZ(b.getZ() - 1);

            if(plugin.locationIsInCuboid(p.getLocation(), a, b )){

                isOnBlock = true;


            }else{
                isOnBlock = false;
            }

        }
        if(isOnBlock){

            taskDidStart = true;
            runLaunch(p, launchCount);

        }else{

            if(taskDidStart){
                taskDidStart = false;
                launch.cancel();
            }

        }
    }

       public void runLaunch(Player p, final int timesToRun){

           if(!taskIsRunning) {

               launch = plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                   @Override
                   public void run() {

                       while (timesToRun > 0) {
                           runLaunch(p, timesToRun - 1);

                           switch (timesToRun) {

                               case 5:
                                   p.sendMessage(ChatColor.DARK_RED + "3...");
                                   break;
                               case 4:
                                   p.sendMessage(ChatColor.DARK_RED + "2...");
                                   break;
                               case 3:
                                   p.sendMessage(ChatColor.DARK_RED + "1...");
                                   break;
                               case 2:
                                   p.sendMessage(ChatColor.DARK_RED + "Blast off!");
                                   plugin.launch(p);
                                   if (p.getLocation().getY() > 100) {

                                       plugin.openWorldSelect(p);


                                   }
                                   break;
                               case 0:
                                   plugin.openWorldSelect(p);
                                   taskDidStart = false;
                                   launch.cancel();
                                   taskIsRunning = false;
                                   return;

                           }
                       }


                   }
               }, 20L);

           }

       }

}
