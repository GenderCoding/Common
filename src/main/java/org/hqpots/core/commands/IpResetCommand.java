package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class IpResetCommand implements CommandExecutor
{
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!sender.hasPermission("command.ipmatcher.admin"))
		{
			sender.sendMessage(StringUtil.colorize("&cYou do not have permission to use this command."));
			return true;
		}
		if (args.length != 1)
		{
			sender.sendMessage(StringUtil.colorize("&cUsage: /ipreset [player]"));
			return true;
		}
		if (args[0] == null)
		{
			sender.sendMessage(StringUtil.colorize("&cUsage: /ipreset [player]"));
			return true;
		}
		Player p = Bukkit.getPlayer(args[0]);
		OfflinePlayer ofp = Bukkit.getOfflinePlayer(args[0]);
		if ((p == null) && (ofp == null))
		{
			sender.sendMessage(StringUtil.colorize("&cUsage: /ipreset [player]"));
			return true;
		}
		if (p != null)
		{
			Core.getInstance().getDB().addToResetQueue(p);
			sender.sendMessage(StringUtil.colorize("&cYou have added " + p.getName() + " to the reset queue."));
		}
		else
		{
			Core.getInstance().getDB().addToResetQueue(ofp);
			sender.sendMessage(StringUtil.colorize("&cYou have added " + ofp.getName() + " to the reset queue."));
		}
		return true;
	}
}
