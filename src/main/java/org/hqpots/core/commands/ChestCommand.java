package org.hqpots.core.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.utils.StringUtil;

public class ChestCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		Player p = (Player) sender;
		if (!sender.hasPermission("command.staffchest"))
		{
			sender.sendMessage(StringUtil.colorize("&cYou do not have permission to do this."));
			return true;
		}
		p.openInventory(p.getEnderChest());
		p.playSound(p.getLocation(), Sound.CHEST_OPEN, 5.0F, 1.0F);
		p.sendMessage(StringUtil.colorize("&6Chest &ehas been successfully opened."));
		return false;
	}
}