package me.porterk.pods;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kalob on 11/18/2015.
 */
public class PodsCommand implements CommandExecutor {

    private PodsMain plugin;

    public PodsCommand(PodsMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String commandLabel, String[] args) {

        if(cmd.getName().equalsIgnoreCase("pods")){
            Player p = (Player) s;

            if(args[0].equalsIgnoreCase("set")){
                    if ( !( s instanceof Player ) )
                    {
                        s.sendMessage( " Only players can use that command!" );
                        return true;
                    }

                    if ( !p.hasPermission( "pod.set" ) )
                    {
                        p.sendMessage( ChatColor.DARK_RED + "You don't have permission to do that!" );
                        return true;
                    }
                    if(args.length < 2){
                        s.sendMessage(ChatColor.DARK_RED + "Syntax Error: Please enter a Pod ID");
                        s.sendMessage(ChatColor.YELLOW + "Ex: /pods set <id name>");
                    }
                    if(args.length > 1) {
                        if (args[1] != null) {

                            HashSet<Material> transparent = new HashSet<Material>();
                            transparent.add(Material.AIR);

                            int x = p.getTargetBlock(transparent, 30).getX();
                            int y = p.getTargetBlock(transparent, 30 ).getY();
                            int z = p.getTargetBlock(transparent, 30).getZ();
                            String world = p.getTargetBlock(transparent, 30).getWorld().getName();
                            String id = args[1];

                            FileConfiguration config = plugin.getConfig();
                            config.addDefault("ID." + id + ".x", x);
                            config.addDefault("ID." + id + ".y", y);
                            config.addDefault("ID." + id + ".z", z);
                            config.addDefault("ID." + id + ".world", world);

                            config.set("ID." + id + ".x", x);
                            config.set("ID." + id + ".y", y);
                            config.set("ID." + id + ".z", z);
                            config.set("ID." + id + ".world", world);

                            plugin.saveConfig();

                            s.sendMessage(ChatColor.DARK_RED + "You've added a Pod by the name of " + ChatColor.GOLD + args[1] + ChatColor.DARK_RED + " at " + ChatColor.GOLD + x + ChatColor.DARK_RED + "," + ChatColor.GOLD + y + ChatColor.DARK_RED + "," + ChatColor.GOLD + z);

                        }
                    }
                    return true;
            }
            if(args[0].equalsIgnoreCase("test")){
                plugin.launch(p);
            }

            p.sendMessage(ChatColor.RED + "Pods!");
            return true;
        }

        return false;
    }
}
