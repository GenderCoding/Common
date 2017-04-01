package org.hqpots.core.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hqpots.core.api.PermissionsAPI;

import com.doctordark.util.ItemBuilder;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

public class SpawnCommand implements CommandExecutor
{

	public String C(String msg)
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("spawner"))
		{
			if (!player.hasPermission("command.spawner"))
			{
				player.sendMessage(PermissionsAPI.PERMISSION);
				return true;
			}
			if (args.length == 0)
			{
				sender.sendMessage(ChatColor.RED + "Usage: /spawner <MobType>");
				return false;
			}
			String spawner = args[0];
			Player p = (Player) sender;
			Inventory inv = p.getInventory();
			inv.addItem(new ItemStack[] {
					new ItemBuilder(Material.MOB_SPAWNER).displayName(ChatColor.GREEN + "Spawner").loreLine(ChatColor.WHITE + WordUtils.capitalizeFully(spawner)).build()
			});
			p.sendMessage(C("&cYou just got a &e" + spawner + "&c."));
			return false;
		}
		return false;
	}
}
