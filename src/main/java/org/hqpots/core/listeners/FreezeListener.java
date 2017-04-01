package org.hqpots.core.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.hqpots.core.Core;
import org.hqpots.core.utils.ColorUtils;

public class FreezeListener implements Listener
{
	private final Core utilities = Core.getInstance();

	private final Set<UUID> frozen = new HashSet<>();

	private AlertTask alertTask;

	public boolean isFrozen(Player player)
	{
		return frozen.contains(player.getUniqueId());
	}

	public void setFreeze(Player player, Player target)
	{
		if (target.hasPermission("command.player.staff") || target.isOp())
		{
			player.sendMessage(new ColorUtils().translateFromString("&cYou can not freeze staff members."));
		}
		else
		{
			frozen.add(target.getUniqueId());
			alertTask = new AlertTask(target);
			alertTask.runTaskTimerAsynchronously(utilities, 0L, 5 * 20);

			for (Player staff : Bukkit.getServer().getOnlinePlayers())
			{
				if (staff.hasPermission("command.player.staff"))
				{
					if (staff.equals(player))
					{
						player.sendMessage(new ColorUtils().translateFromString("&c" + target.getName() + " &eis now frozen."));
					}
					else
					{
						staff.sendMessage(new ColorUtils().translateFromString("&c" + target.getName() + " &eis now frozen, frozen by &a" + player.getName() + "&e."));
					}
				}
			}
		}
	}

	public void removeFreeze(Player player, Player target)
	{
		alertTask.cancel();
		frozen.remove(target.getUniqueId());
		target.sendMessage(new ColorUtils().translateFromString("&aYou are not longer frozen."));

		for (Player staff : Bukkit.getServer().getOnlinePlayers())
		{
			if (staff.hasPermission("command.player.staff") || staff.isOp())
			{
				if (staff.equals(player))
				{
					player.sendMessage(new ColorUtils().translateFromString("&c" + target.getName() + " &eis no longer frozen."));
				}
				else
				{
					staff.sendMessage(new ColorUtils().translateFromString("&c" + target.getName() + " &eis no longer frozen, removed by &a" + player.getName() + "&e."));
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		Location from = event.getFrom();
		Location to = event.getTo();
		if (isFrozen(player))
		{
			if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())
			{
				event.setTo(from);
			}
		}
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event)
	{
		Player player = (Player) event.getEntity().getShooter();

		if (isFrozen(player))
		{
			event.setCancelled(true);
			player.sendMessage(new ColorUtils().translateFromString("&cYou can not interact with that item while you are frozen."));
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		if (isFrozen(player))
		{
			event.setCancelled(true);
			player.sendMessage(new ColorUtils().translateFromString("&cYou can not use commands while you are frozen."));
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		if (event.getBlock() != null)
		{
			if (isFrozen(player))
			{
				event.setCancelled(true);
				player.sendMessage(new ColorUtils().translateFromString("&cYou can not place blocks while you are frozen."));
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();

		if (event.getBlock() != null)
		{
			if (isFrozen(player))
			{
				event.setCancelled(true);
				player.sendMessage(new ColorUtils().translateFromString("&cYou can not break blocks while you are frozen."));
			}
		}
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player player = event.getPlayer();

		if (isFrozen(player))
		{
			alertTask.cancel();
			frozen.remove(player.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity().getPlayer();

		if (isFrozen(player))
		{
			alertTask.cancel();
			frozen.remove(player.getUniqueId());
		}
	}

	public Player getPlayer(Entity entity)
	{
		if (entity instanceof Player) { return (Player) entity; }
		if (entity instanceof Projectile)
		{
			Projectile projectile = (Projectile) entity;
			if (projectile.getShooter() != null && projectile.getShooter() instanceof Player) { return (Player) projectile.getShooter(); }
		}
		return null;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		Player damager = getPlayer(event.getDamager());
		Player damaged = getPlayer(event.getEntity());
		if (damager != null && damaged != null && damaged != damager)
		{
			if (utilities.getFreezeListener().isFrozen(damager))
			{
				damager.sendMessage(new ColorUtils().translateFromString("&cYou can not attack players while frozen."));
				event.setCancelled(true);
			}

			if (utilities.getFreezeListener().isFrozen(damaged))
			{
				damager.sendMessage(new ColorUtils().translateFromString("&cYou can not attack " + damaged.getName() + " because he is currently frozen."));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		if (isFrozen(player))
		{
			for (Player staff : Bukkit.getServer().getOnlinePlayers())
			{
				if (staff.hasPermission("command.player.staff") || staff.isOp())
				{}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		if (isFrozen(player))
		{
			alertTask = new AlertTask(player);
			alertTask.runTaskTimerAsynchronously(utilities, 0L, 5 * 20);

			for (Player staff : Bukkit.getServer().getOnlinePlayers())
			{
				if (staff.hasPermission("command.player.staff"))
				{}
			}
		}
	}

	private class AlertTask extends BukkitRunnable
	{
		private final Player player;

		public AlertTask(Player player)
		{
			this.player = player;
		}

		@Override
		public void run()
		{
			player.setHealth(((CraftPlayer) player).getMaxHealth());
			player.setFireTicks(0);
			player.setFoodLevel(20);
			player.setSaturation(3.0F);
			for (PotionEffect potionEffect : player.getActivePotionEffects())
			{
				player.removePotionEffect(potionEffect.getType());
			}

			player.sendMessage(new ColorUtils().translateFromString(" "));
			player.sendMessage(new ColorUtils().translateFromString("&f��������&c��&f��������"));
			player.sendMessage(new ColorUtils().translateFromString("&f������&c��&6��&c��&f������"));
			player.sendMessage(new ColorUtils().translateFromString("&f����&c��&6��&0��&6��&c��&f���� &4&lDo NOT log out!"));
			player.sendMessage(new ColorUtils().translateFromString("&f����&c��&6��&0��&6��&c��&f���� &eIf you log out, you will be banned!"));
			player.sendMessage(new ColorUtils().translateFromString("&f��&c��&6����&0��&6����&c��&f�� &fPlease download Teamspeak and connect to:"));
			player.sendMessage(new ColorUtils().translateFromString("&f��&c��&6����������&c��&f�� &fts.saifed.us"));
			player.sendMessage(new ColorUtils().translateFromString("&c��&6������&0��&6������&c��"));
			player.sendMessage(new ColorUtils().translateFromString("&c������������������"));
			player.sendMessage(new ColorUtils().translateFromString(" "));
		}
	}
}