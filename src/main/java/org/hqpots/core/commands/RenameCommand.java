package org.hqpots.core.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hqpots.core.utils.StringUtil;

public class RenameCommand implements Listener, CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player) sender;
		if (args.length != 1)
		{
			p.sendMessage(StringUtil.colorize("&cWrong number of args &7(&cUse '_' instead of space&7)"));
		}
		else if (!p.hasPermission("command.rename"))
		{
			p.sendMessage(StringUtil.colorize("&cYou do not have &epermission &cto do this."));
		}
		else if ((p.getItemInHand() == null) || (p.getItemInHand() == new ItemStack(Material.AIR)))
		{
			p.sendMessage(StringUtil.colorize("&cYou're not holding an item!"));
		}
		else
		{
			ItemMeta meta = p.getItemInHand().getItemMeta();
			meta.setDisplayName(StringUtil.colorize(args[0].replace("_", " ")));
			p.getItemInHand().setItemMeta(meta);
			p.sendMessage(StringUtil.colorize("&7Item &asuccessfully &7renamed!"));
		}
		return false;
	}
}
