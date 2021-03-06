package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class StaffChatCommand implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		
		if(!sender.hasPermission("command.staffchat")){
			sender.sendMessage(StringUtil.colorize("&cYou do not have permission to use this command."));
			return false;
		}

		if(args.length < 1){
			sender.sendMessage(StringUtil.colorize("&cUsage: /sc [message]"));
			return false;
		}
		
		String message = StringUtil.colorize("&9(" + Bukkit.getServerName() + ") &b" + sender.getName() + "&7:&f ");
		message += StringUtil.join(" ", args);
		Core.getHqJedis().send("hq_staffchat", message);
		
		return false;
	}
	
}
