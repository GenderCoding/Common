package org.hqpots.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.Core;
import org.hqpots.core.api.PermissionsAPI;
import org.hqpots.core.utils.StringUtil;

public class HelpOPCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		if (!(sender instanceof Player))
		{
			sender.sendMessage(PermissionsAPI.CONSOLE);
			return false;
		}

		if (args.length < 1)
		{
			sender.sendMessage(StringUtil.colorize("&cUsage: /helpop [message]"));
			return false;
		}

		String message = StringUtil.colorize("&c[HelpOP] &e" + sender.getName() + "&7: &f");
		message += StringUtil.join(" ", args);
		Core.getHqJedis().send("hq_staffchat", message);

		return false;
	}

}
