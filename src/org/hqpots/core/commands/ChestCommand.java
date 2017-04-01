package org.hqpots.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		Player p = (Player) sender;
		if (!sender.hasPermission("command.staffchest"))
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do this."));
			return true;
		}
		p.openInventory(p.getEnderChest());
		p.playSound(p.getLocation(), Sound.CHEST_OPEN, 5.0F, 1.0F);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Chest &ehas been successfully opened."));
		return false;
	}
}