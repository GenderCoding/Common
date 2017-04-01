package org.hqpots.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.hqpots.core.utils.StringUtil;

public class NightVisionCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Only players can use this command.");
			return false;
		}

		Player player = (Player) sender;
		if (player.getActivePotionEffects().contains(PotionEffectType.NIGHT_VISION))
		{
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.sendMessage(StringUtil.colorize("&cNightvision disabled."));
			return false;
		}

		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000000, 1));
		player.sendMessage(StringUtil.colorize("&aNightvision enabled."));

		return false;
	}

}
