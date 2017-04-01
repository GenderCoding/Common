package org.hqpots.core.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hqpots.core.utils.StringUtil;

public class ReportCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		if (args.length < 2)
		{
			sender.sendMessage(StringUtil.colorize("&cUsage: /report [player] [reason]"));
			return false;
		}

		String target = args[0];
		if (Bukkit.getPlayer(target) == null)
		{
			sender.sendMessage(StringUtil.colorize("&c" + target + " is not online."));
			return false;
		}

		String reason = StringUtil.join(" ", Arrays.copyOfRange(args, 1, args.length));
		Bukkit.broadcast(StringUtil.colorize("&c[Report] &e" + sender.getName() + " &7report &e" + Bukkit.getPlayer(target).getName() + " &7for &b") + reason, "command.staffchat");

		return false;
	}

}
