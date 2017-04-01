package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.hqpots.core.Core;

public class AutorestartCommand implements CommandExecutor, Listener
{

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.BLUE + "You cannot do this in console");
			return true;
		}
		final Player p = (Player) sender;
		final String perm = ChatColor.translateAlternateColorCodes('&', "&cYou cannot execute this command");
		if (!cmd.getName().equalsIgnoreCase("autorestart")) { return false; }
		if (!p.hasPermission("command.restart"))
		{
			p.sendMessage(perm);
			return true;
		}
		Bukkit.broadcastMessage(ChatColor.GOLD + "");
		Bukkit.broadcastMessage(ChatColor.GOLD + "");
		Bukkit.broadcastMessage(ChatColor.GOLD + "§6§lHQPots §ehas been scheduled a automatic restart to happen");
		Bukkit.broadcastMessage(ChatColor.GOLD + "§ePlease reconnect one commenced the automatic restart in §7§l1 Minute");
		Bukkit.broadcastMessage(ChatColor.GOLD + "");
		Bukkit.broadcastMessage(ChatColor.GOLD + "");
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, (Runnable) new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§6§lHQPots §ehas been scheduled a automatic restart to happen");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§ePlease reconnect one commenced the automatic restart in §7§l40 seconds");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
			}
		}, 500L);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, (Runnable) new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§6§lHQPots §ehas been scheduled a automatic restart to happen");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§ePlease reconnect one commenced the automatic restart in §7§l30 seconds");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
			}
		}, 700L);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, (Runnable) new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§6§lHQPots §ehas been scheduled a automatic restart to happen");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§ePlease reconnect one commenced the automatic restart in §7§l20 seconds");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
			}
		}, 900L);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, (Runnable) new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§6§lHQPots §ehas been scheduled a automatic restart to happen");
				Bukkit.broadcastMessage(ChatColor.GOLD + "§ePlease reconnect one commenced the automatic restart in §7§l10 seconds");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
				Bukkit.broadcastMessage(ChatColor.GOLD + "");
			}
		}, 1000L);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, (Runnable) new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.savePlayers();
				Bukkit.dispatchCommand(sender, "hub");
				Bukkit.dispatchCommand(sender, "stop");
			}
		}, 1200L);
		return true;
	}
}
