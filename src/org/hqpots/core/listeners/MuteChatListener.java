package org.hqpots.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.hqpots.core.commands.MuteChatCommand;

import net.md_5.bungee.api.ChatColor;

public class MuteChatListener implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if(MuteChatCommand.muteToggle) {
			if(!player.hasPermission("command.mutechat.chat")) {
				e.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChat is muted!"));
			} else if (player.hasPermission("command.mutechat.chat")) {
				e.setCancelled(false);
			}
		}
	}
}
