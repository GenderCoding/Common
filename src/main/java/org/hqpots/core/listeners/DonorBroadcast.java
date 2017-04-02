package org.hqpots.core.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DonorBroadcast implements Listener
{

	public static void enable()
	{

		new BukkitRunnable()
		{

			@Override
			public void run()
			{
				ArrayList<String> playernames = new ArrayList<String>();
				for (Player player : Bukkit.getOnlinePlayers())
				{
					PermissionUser u = PermissionsEx.getUser(player);
					if (u.inGroup("platinum"))
					{
						playernames.add(player.getName());
					}
				}
				Bukkit.broadcastMessage(StringUtil.colorize("&eOnline &3✵&bPlatinum &eUsers:&b ") + playernames.toString());
			}
		}.runTaskTimer(Core.getInstance(), 3600L, 3600L);
	}
}