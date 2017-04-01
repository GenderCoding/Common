package org.hqpots.core.events;

import org.bukkit.event.Listener;

import lombok.Getter;


public class UserEvents implements Listener {
	
	public static UserEvents settings = new UserEvents();
	
	
	public static UserEvents getSettings() {
		return settings;
	}

	public boolean isDebug() {
		return true;
	}
}
