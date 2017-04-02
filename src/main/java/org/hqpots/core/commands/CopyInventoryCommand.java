package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.utils.StringUtil;

import net.md_5.bungee.api.ChatColor;

public class CopyInventoryCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
	{
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("copyinv"))
		{
			if (!player.hasPermission("command.copyinv"))
			{
				player.sendMessage(ChatColor.RED + "No");
				return true;
			}
			if (arguments.length != 1)
			{
				sender.sendMessage(StringUtil.colorize("&cThe correct usage is: /" + label + " <name>"));
				return true;
			}
			if (arguments.length == 1)
			{
				Player target = Bukkit.getServer().getPlayer(arguments[0]);

				player.getInventory().setContents(target.getInventory().getContents());
				player.getInventory().setArmorContents(target.getInventory().getArmorContents());
				player.sendMessage(StringUtil.colorize("&eYou copied the inventory of &a" + arguments[0] + "&e."));
			}
		}
		return false;
	}
}
