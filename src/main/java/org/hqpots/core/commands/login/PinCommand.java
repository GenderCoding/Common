package org.hqpots.core.commands.login;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class PinCommand implements Listener, CommandExecutor
{
	private File pinFile;

	public PinCommand()
	{
		this.pinFile = new File(Core.getInstance().getDataFolder(), "pin.yml");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player p = (Player) sender;
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "pin.yml"));
		if (sender.hasPermission("command.pin"))
		{
			if (args.length != 1)
			{
				p.sendMessage(StringUtil.colorize("&cUsage&7: /setpin [pin]"));
			}
			else if ((args.length == 1) && (!data.contains(String.valueOf(p.getUniqueId()))))
			{
				try
				{
					int i = 0;
					i = Integer.parseInt(args[0]);
					if ((i >= 0) && (i <= 10000))
					{
						p.sendMessage(StringUtil.colorize("&aPin successfully set."));
						data.set(String.valueOf(p.getUniqueId()), Integer.valueOf(args[0]));
						data.save(this.pinFile);
					}
					else
					{
						p.sendMessage(StringUtil.colorize("&cNumber must be between 0-10000"));
					}
				}
				catch (NumberFormatException | IOException ex2)
				{
					p.sendMessage(StringUtil.colorize("&cThat is not a number."));
				}
			}
			else if ((args.length == 1) && (data.contains(String.valueOf(p.getUniqueId()))))
			{
				p.sendMessage(StringUtil.colorize("&cIf you wish to change your pin, please speak to the owner"));
			}
		}
		return false;
	}

	@EventHandler
	public void onNoPinMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "pin.yml"));
		if ((!data.contains(String.valueOf(p.getUniqueId()))) && (p.hasPermission("command.pin")))
		{
			event.setTo(event.getFrom());
			p.sendMessage(StringUtil.colorize("&eYou must set a &6&lPIN&e. &7&o(/setpin <pin>)"));
		}
	}

	@EventHandler
	public void onCommandBeforeLoggedIn(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "pin.yml"));
		if ((!e.getMessage().startsWith("/setpin")) && (!data.contains(String.valueOf(p.getUniqueId()))) && (p.hasPermission("Core.command.pin")))
		{
			e.setCancelled(true);
			p.sendMessage(StringUtil.colorize("&eYou must set a &6&lPIN&e. &7&o(/setpin <pin>)"));
		}
	}
}
