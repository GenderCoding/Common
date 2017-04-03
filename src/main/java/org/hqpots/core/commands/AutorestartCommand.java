package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

public class AutorestartCommand implements CommandExecutor, Listener
{

	@Getter boolean rebooting = false;
	@Getter int taskId;
	@Getter @Setter int time;

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.BLUE + "You cannot do this in console");
			return true;
		}
		final Player p = (Player) sender;
		final String perm = StringUtil.colorize("&cYou cannot execute this command");
		if (!p.hasPermission("command.restart"))
		{
			p.sendMessage(perm);
			return true;
		}

		if (!isRebooting())
		{
			time = 60;
			reboot();
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(StringUtil.colorize("&c&l(!) &cWorld reboot has been queued by the network"));
			Bukkit.broadcastMessage(StringUtil.colorize("   The world will automagically reboot in 60 seconds"));
			Bukkit.broadcastMessage("");
		}

		return true;
	}

	public void reboot()
	{
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), () -> {
			switch (getTime())
			{
				case 45:
				case 30:
				case 20:
				case 10:
				case 5:
				case 4:
				case 3:
				case 2:
				case 1:
				case 0:
					Bukkit.broadcastMessage(StringUtil.colorize("Rebooting " + (getTime() == 0 ? "now" : "in " + getTime() + " seconds") + "."));
					if (getTime() == 0)
					{
						Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
							Bukkit.getOnlinePlayers().forEach(player -> {
								player.chat("/hub");
							});
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
							Bukkit.getScheduler().cancelTask(getTaskId());
						});
					}
					setTime(getTime() - 1);
			}
		} , 0, 20);
	}

}
