package org.hqpots.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hqpots.core.commands.AutorestartCommand;
import org.hqpots.core.commands.BlacklistCommand;
import org.hqpots.core.commands.ChestCommand;
import org.hqpots.core.commands.ClearChatCommand;
import org.hqpots.core.commands.CopyInventoryCommand;
import org.hqpots.core.commands.CreativeCommand;
import org.hqpots.core.commands.FreezeCommand;
import org.hqpots.core.commands.InvseeCommand;
import org.hqpots.core.commands.IpResetCommand;
import org.hqpots.core.commands.ListCommand;
import org.hqpots.core.commands.MuteChatCommand;
import org.hqpots.core.commands.NightVisionCommand;
import org.hqpots.core.commands.RenameCommand;
import org.hqpots.core.commands.SpawnCommand;
import org.hqpots.core.commands.StaffModeCommand;
import org.hqpots.core.commands.login.LoginCommand;
import org.hqpots.core.commands.login.PinCommand;
import org.hqpots.core.commands.login.RemovePinCommand;
import org.hqpots.core.commands.staffmode.StaffChatCommand;
import org.hqpots.core.commands.staffmode.VanishCommand;
import org.hqpots.core.events.DB;
import org.hqpots.core.events.UserEvents;
import org.hqpots.core.listeners.DonorBroadcast;
import org.hqpots.core.listeners.FreezeListener;
import org.hqpots.core.listeners.JoinHandler;
import org.hqpots.core.listeners.JoinListeners;
import org.hqpots.core.listeners.ListListener;
import org.hqpots.core.listeners.MuteChatListener;
import org.hqpots.core.listeners.StaffModeListener;
import org.hqpots.core.utils.ColorUtils;

import lombok.Getter;

public class Core extends JavaPlugin implements Listener
{
	
	/*
	 * TODO: Fix Bugs Mutechat - Not letting people type it - StaffMode doesnt
	 * allow people to enter it - Done Fix Essentials Gamemode -
	 */

	/*
	 * 
	 * TODO: StaffChatCommand PanicCommand ReportCommand
	 * 
	 */

	@Getter public static Core instance;
	private DB db;
	public static ArrayList<String> staff = new ArrayList<>();
	public static ArrayList<String> mod = new ArrayList<>();
	public static ArrayList<String> freeze = new ArrayList<>();
	@Getter private StaffModeListener staffModeListener;
	@Getter private FreezeListener freezeListener;

	public void onEnable()
	{
		instance = this;
		this.db = new DB();
		setupFiles();
		DonorBroadcast.enable();

		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "HQPots - Core - Status: Active");

		if (UserEvents.getSettings().isDebug())
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "HQPots - Core - Status: Beta");
		}

		// COMMANDS
		getCommand("chest").setExecutor(new ChestCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		getCommand("list").setExecutor(new ListCommand());
		getCommand("pin").setExecutor(new LoginCommand());
		getCommand("removepin").setExecutor(new RemovePinCommand());
		getCommand("setpin").setExecutor(new PinCommand());
		getCommand("copyinv").setExecutor(new CopyInventoryCommand());
		getCommand("mutechat").setExecutor(new MuteChatCommand());
		getCommand("spawner").setExecutor(new SpawnCommand());
		getCommand("ipreset").setExecutor(new IpResetCommand());
		getCommand("cc").setExecutor(new ClearChatCommand());
		getCommand("rename").setExecutor(new RenameCommand());
		getCommand("blacklist").setExecutor(new BlacklistCommand());
		getCommand("creative").setExecutor(new CreativeCommand());
		getCommand("nightvision").setExecutor(new NightVisionCommand());
		getCommand("staffchat").setExecutor(new StaffChatCommand());
		Bukkit.getServer().getPluginCommand("staffmode").setExecutor(new StaffModeCommand());
		Bukkit.getServer().getPluginCommand("freeze").setExecutor(new FreezeCommand());
		Bukkit.getServer().getPluginCommand("vanish").setExecutor(new VanishCommand());
		Bukkit.getServer().getPluginCommand("autorestart").setExecutor(new AutorestartCommand());

		// LISTENERS
		PluginManager manager = Bukkit.getServer().getPluginManager();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new LoginCommand(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinHandler(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new DonorBroadcast(), this);
		manager.registerEvents(new ListListener(), this);
		manager.registerEvents(new MuteChatListener(), this);
		freezeListener = new FreezeListener();
		Bukkit.getServer().getPluginManager().registerEvents(freezeListener, this);
		staffModeListener = new StaffModeListener();
		Bukkit.getServer().getPluginManager().registerEvents(staffModeListener, this);
	}

	public void onDisable()
	{

	}

	public DB getDB()
	{
		return this.db;
	}

	public FileConfigurationOptions getFileConfigurationOptions()
	{
		return getConfig().options();
	}

	public List<String> getStringList(String path)
	{
		if (getConfig().contains(path))
		{
			ArrayList<String> lines = new ArrayList<>();
			for (String text : getConfig().getStringList(path))
			{
				lines.add(new ColorUtils().translateFromString(text));
			}
			return lines;
		}
		return Arrays.asList(new String[] {
				new ColorUtils().translateFromString("&cString list not found: '" + path + "'")
		});
	}

	private void setupFiles()
	{
		try
		{
			if (!getDataFolder().exists())
			{
				getDataFolder().mkdirs();
			}
			File file = new File(getDataFolder().getAbsolutePath(), "config.yml");
//			File file2 = new File(getDataFolder().getAbsolutePath(), "pin.yml");
//			File file3 = new File(getDataFolder().getAbsolutePath(), "fails.yml");
//			File file4 = new File(getDataFolder().getAbsoluteFile(), "notes.yml");
			if (!file.exists())
			{
				getFileConfigurationOptions().copyDefaults(true);
				saveConfig();
			}
			else
			{}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public void createPinFile()
	{
		try
		{
			FileConfiguration pin = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "pin.yml"));
			File pinFile = new File(instance.getDataFolder(), "pin.yml");
			pin.save(pinFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void createFailedFile()
	{
		try
		{
			FileConfiguration fail = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "fails.yml"));
			File failFile = new File(instance.getDataFolder(), "fails.yml");
			fail.save(failFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onMoveBeforeLoggedIn(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName()))
		{
			e.setTo(e.getFrom());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onChatBeforeLoggedIn(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if (LoginCommand.loggingin.contains(p.getName()))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}

	@EventHandler
	public void onCommandBeforeLoggedIn(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		if ((!e.getMessage().startsWith("/pin")) && (LoginCommand.loggingin.contains(p.getName())))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must enter your &6&lPIN&e. &7&o(/pin <pin>)"));
		}
	}
}
