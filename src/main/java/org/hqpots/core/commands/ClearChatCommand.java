package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.utils.StringUtil;

public class ClearChatCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
	{
		if (sender.hasPermission("command.clearchat"))
		{
			if (arguments.length == 0)
			{
				sender.sendMessage(StringUtil.colorize("&cUsage: /" + label + " <reason>"));
			}
			else
			{
				for (Player online : Bukkit.getServer().getOnlinePlayers())
				{
					online.sendMessage(new String[101]);
					online.sendMessage(StringUtil.colorize("&a" + sender.getName() + " &ehas cleared the global chat for &7" + StringUtil.join(" ", arguments) + "&e."));
				}
			}
		}
		else
		{
			sender.sendMessage(StringUtil.colorize("&cYou do not have permissions to execute this command."));
		}
		return true;
	}
}
