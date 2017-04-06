package org.hqpots.core.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class StaffModeListener implements Listener
{
	private final Core utilities = Core.getInstance();

	private final Set<UUID> staffMode = new HashSet<>();
	private final Set<UUID> vanished = new HashSet<>();

	private final HashMap<UUID, UUID> inspectedPlayer = new HashMap<>();
	private final HashMap<UUID, ItemStack[]> contents = new HashMap<>();
	private final HashMap<UUID, ItemStack[]> armorContents = new HashMap<>();

	public Player getInspectedPlayer(Player player)
	{
		return Bukkit.getServer().getPlayer(inspectedPlayer.get(player.getUniqueId()));
	}

	public boolean isVanished(Player player)
	{
		return vanished.contains(player.getUniqueId());
	}

	public boolean isStaffModeActive(Player player)
	{
		return staffMode.contains(player.getUniqueId());
	}

	public boolean hasPreviousInventory(Player player)
	{
		return contents.containsKey(player.getUniqueId()) && armorContents.containsKey(player.getUniqueId());
	}

	public void saveInventory(Player player)
	{
		contents.put(player.getUniqueId(), player.getInventory().getContents());
		armorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
	}

	public void loadInventory(Player player)
	{
		PlayerInventory playerInventory = player.getInventory();
		playerInventory.setContents((ItemStack[]) contents.get(player.getUniqueId()));
		playerInventory.setArmorContents((ItemStack[]) armorContents.get(player.getUniqueId()));
		contents.remove(player.getUniqueId());
		armorContents.remove(player.getUniqueId());
	}

	public void setStaffMode(Player player, boolean status)
	{
		if (status == true)
		{
			if (player.hasPermission("command.player.staff"))
			{
				staffMode.add(player.getUniqueId());
				saveInventory(player);
				PlayerInventory playerInventory = player.getInventory();
				playerInventory.setArmorContents(new ItemStack[] {
						new ItemStack(Material.AIR),
						new ItemStack(Material.AIR),
						new ItemStack(Material.AIR),
						new ItemStack(Material.AIR)
				});
				playerInventory.clear();
				setItems(player);
				setVanished(player, true);
				player.updateInventory();
				if (player.getGameMode() == GameMode.SURVIVAL)
				{
					player.setGameMode(GameMode.CREATIVE);
				}
			}
			else
			{
				player.sendMessage(StringUtil.colorize("&cYou do not have permissions to enable the staffmode."));
			}
		}
		else
		{
			staffMode.remove(player.getUniqueId());
			PlayerInventory playerInventory = player.getInventory();
			playerInventory.setArmorContents(new ItemStack[] {
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR)
			});
			playerInventory.clear();
			setVanished(player, false);
			if (hasPreviousInventory(player))
			{
				loadInventory(player);
			}
			player.updateInventory();
			if (!player.hasPermission("essentials.creative") && player.getGameMode() == GameMode.CREATIVE)
			{
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
	}

	public void setVanished(Player player, boolean status)
	{
		if (status == true)
		{
			vanished.add(player.getUniqueId());
			for (Player online : Bukkit.getServer().getOnlinePlayers())
			{
				if (!online.hasPermission("command.player.staff"))
				{
					online.hidePlayer(player);
				}
			}

			if (isStaffModeActive(player))
			{
				PlayerInventory playerInventory = player.getInventory();
				playerInventory.setItem(7, getVanishItemFor(player));
			}
		}
		else
		{
			vanished.remove(player.getUniqueId());
			for (Player online : Bukkit.getServer().getOnlinePlayers())
			{
				online.showPlayer(player);
			}

			if (isStaffModeActive(player))
			{
				PlayerInventory playerInventory = player.getInventory();
				playerInventory.setItem(7, getVanishItemFor(player));
			}
		}
	}

	public void setItems(Player player)
	{
		PlayerInventory playerInventory = player.getInventory();

		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta compassMeta = compass.getItemMeta();
		compassMeta.setDisplayName(StringUtil.colorize("&bTeleport Compass"));
		compassMeta.setLore(Arrays.asList(StringUtil.colorize("&7Right click block: Move through"), StringUtil.colorize("&7Left click: Move to block in line of sight")));
		compass.setItemMeta(compassMeta);

		ItemStack book = new ItemStack(Material.BOOK, 1);
		ItemMeta bookMeta = book.getItemMeta();
		bookMeta.setDisplayName(StringUtil.colorize("&bInspection Tool"));
		bookMeta.setLore(Arrays.asList(StringUtil.colorize("&7Right click player to inspect inventory")));
		book.setItemMeta(bookMeta);

		ItemStack woodAxe = new ItemStack(Material.WOOD_AXE, 1);
		ItemMeta woodAxeMeta = woodAxe.getItemMeta();
		woodAxeMeta.setDisplayName(StringUtil.colorize("&bWorldEdit Wand"));
		woodAxeMeta.setLore(Arrays.asList(StringUtil.colorize("&7Left click block: Select first position"), StringUtil.colorize("&7Right click block: Select second position")));
		woodAxe.setItemMeta(woodAxeMeta);

		ItemStack carpet = new ItemStack(Material.CARPET, 1, (short) 3);
		ItemMeta carpetMeta = carpet.getItemMeta();
		carpetMeta.setDisplayName(StringUtil.colorize("&bBetter Vision"));
		carpetMeta.setLore(Arrays.asList(StringUtil.colorize("&7Used to get a better vision to your target")));
		carpet.setItemMeta(carpetMeta);

		ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta diamondPickaxeMeta = diamondPickaxe.getItemMeta();
		diamondPickaxeMeta.setDisplayName(StringUtil.colorize("&bXrayer Finder"));
		diamondPickaxeMeta.setLore(Arrays.asList(StringUtil.colorize("&7Right click to open the xrayer finder menu")));
		diamondPickaxe.setItemMeta(diamondPickaxeMeta);

		ItemStack record10 = new ItemStack(Material.RECORD_10, 1);
		ItemMeta record10Meta = record10.getItemMeta();
		record10Meta.setDisplayName(StringUtil.colorize("&bRandom Teleportation"));
		record10Meta.setLore(Arrays.asList(StringUtil.colorize("&7Right click to teleport to a random player")));
		record10.setItemMeta(record10Meta);

		playerInventory.setItem(0, compass);
		playerInventory.setItem(1, book);
		playerInventory.setItem(2, woodAxe);
		playerInventory.setItem(3, carpet);
		playerInventory.setItem(6, diamondPickaxe);
		playerInventory.setItem(7, getVanishItemFor(player));
		playerInventory.setItem(8, record10);
	}

	private ItemStack getVanishItemFor(Player player)
	{
		ItemStack inkSack = null;

		if (isVanished(player))
		{
			inkSack = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta inkSackMeta = inkSack.getItemMeta();
			inkSackMeta.setDisplayName(StringUtil.colorize("&bVanished: &aTrue"));
			inkSackMeta.setLore(Arrays.asList(StringUtil.colorize("&7Right click to update your vanish status.")));
			inkSack.setItemMeta(inkSackMeta);
		}
		else
		{
			inkSack = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta inkSackMeta = inkSack.getItemMeta();
			inkSackMeta.setDisplayName(StringUtil.colorize("&bVanished: &cFalse"));
			inkSackMeta.setLore(Arrays.asList(StringUtil.colorize("&7Right click to update your vanish status.")));
			inkSack.setItemMeta(inkSackMeta);
		}

		return inkSack;
	}

	public void openInspectionMenu(Player player, Player target)
	{
		Inventory inventory = Bukkit.getServer().createInventory(null, 54, StringUtil.colorize("&eInspecting: &c" + target.getName()));

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				inspectedPlayer.put(player.getUniqueId(), target.getUniqueId());

				PlayerInventory playerInventory = target.getInventory();

				ItemStack speckledMelon = new ItemStack(Material.SPECKLED_MELON, (int) ((CraftPlayer) target).getHealth());
				ItemMeta speckledMelonMeta = speckledMelon.getItemMeta();
				speckledMelonMeta.setDisplayName(StringUtil.colorize("&aHealth"));
				speckledMelon.setItemMeta(speckledMelonMeta);

				ItemStack cookedBeef = new ItemStack(Material.COOKED_BEEF, target.getFoodLevel());
				ItemMeta cookedBeefMeta = cookedBeef.getItemMeta();
				cookedBeefMeta.setDisplayName(StringUtil.colorize("&aHunger"));
				cookedBeef.setItemMeta(cookedBeefMeta);

				ItemStack brewingStand = new ItemStack(Material.BREWING_STAND_ITEM, target.getPlayer().getActivePotionEffects().size());
				ItemMeta brewingStandMeta = brewingStand.getItemMeta();
				brewingStandMeta.setDisplayName(StringUtil.colorize("&aActive Potion Effects"));
				ArrayList<String> brewingStandLore = new ArrayList<>();
				// for (PotionEffect potionEffect :
				// target.getPlayer().getActivePotionEffects())
				// {
				// String effectName = potionEffect.getType().getName();
				// int effectLevel = potionEffect.getAmplifier();
				// effectLevel++;
				// // brewingStandLore.add(new
				// // StringUtil.colorize("&e" +
				// // WordUtils.capitalizeFully(effectName).replace("_", " ") +
				// // " " + effectLevel + "&7: &c" +
				// //
				// TimeUtils.IntegerCountdown.setFormat(potionEffect.getDuration()
				// // / 20)));
				// }
				brewingStandMeta.setLore(brewingStandLore);
				brewingStand.setItemMeta(brewingStandMeta);

				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				ItemMeta compassMeta = compass.getItemMeta();
				compassMeta.setDisplayName(StringUtil.colorize("&aPlayer Location"));
				compassMeta.setLore(Arrays.asList(StringUtil.colorize("&eWorld&7: &a" + player.getWorld().getName()), StringUtil.colorize("&eCoords"), StringUtil.colorize("  &eX&7: &c" + target.getLocation().getBlockX()), StringUtil.colorize("  &eY&7: &c" + target.getLocation().getBlockY()), StringUtil.colorize("  &eZ&7: &c" + target.getLocation().getBlockZ())));
				compass.setItemMeta(compassMeta);

				ItemStack magmaCream = new ItemStack(Material.MAGMA_CREAM, 1);
				ItemMeta magmaCreamMeta = magmaCream.getItemMeta();
				if (utilities.getFreezeListener().isFrozen(target))
				{
					magmaCreamMeta.setDisplayName(StringUtil.colorize("&aFrozen&7: &aTrue"));
				}
				else
				{
					magmaCreamMeta.setDisplayName(StringUtil.colorize("&aFrozen&7: &cFalse"));
				}
				magmaCreamMeta.setLore(Arrays.asList(StringUtil.colorize("&eClick to update freeze status")));
				magmaCream.setItemMeta(magmaCreamMeta);

				ItemStack orangeGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);

				inventory.setContents(playerInventory.getContents());
				inventory.setItem(36, playerInventory.getHelmet());
				inventory.setItem(37, playerInventory.getChestplate());
				inventory.setItem(38, playerInventory.getLeggings());
				inventory.setItem(39, playerInventory.getBoots());
				inventory.setItem(40, playerInventory.getItemInHand());
				for (int i = 41; i <= 46; i++)
				{
					inventory.setItem(i, orangeGlassPane);
				}
				inventory.setItem(47, speckledMelon);
				inventory.setItem(48, cookedBeef);
				inventory.setItem(49, brewingStand);
				inventory.setItem(50, compass);
				inventory.setItem(51, magmaCream);
				for (int i = 52; i <= 53; i++)
				{
					inventory.setItem(i, orangeGlassPane);
				}

				if (!player.getOpenInventory().getTitle().equals(StringUtil.colorize("&eInspecting: &c" + target.getName())))
				{
					cancel();
					inspectedPlayer.remove(player.getUniqueId());
				}
			}
		}.runTaskTimer(utilities, 0L, 5L);

		player.openInventory(inventory);

	}

	@EventHandler
	public void onClickInspectionMenu(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if (event.getInventory().getTitle().contains(StringUtil.colorize("&eInspecting: ")))
		{
			Player inspected = getInspectedPlayer(player);
			if (event.getRawSlot() == 51)
			{
				if (inspected != null)
				{
					if (utilities.getFreezeListener().isFrozen(inspected))
					{
						utilities.getFreezeListener().removeFreeze(player, inspected);
					}
					else
					{
						utilities.getFreezeListener().setFreeze(player, inspected);
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if (isStaffModeActive(player))
		{
			event.setCancelled(true);
		}
		if (event.getClickedInventory() != null && event.getClickedInventory().getTitle().contains(StringUtil.colorize("&eInspecting: ")))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteractBook(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() != null && event.getRightClicked() instanceof Player)
		{
			Player player = event.getPlayer();
			ItemStack itemStack = player.getItemInHand();
			if (itemStack.hasItemMeta())
			{
				if (itemStack.getItemMeta().getDisplayName().equals(StringUtil.colorize("&bInspection Tool")))
				{
					if (isStaffModeActive(player) && player.hasPermission("command.player.staff"))
					{
						Player target = (Player) event.getRightClicked();
						if (target != null && !player.getName().equals(target.getName()))
						{
							openInspectionMenu(player, target);
							player.sendMessage(StringUtil.colorize("&eYou are now inspecting the inventory of &c" + target.getName() + "&e."));
						}
					}
					else
					{
						setStaffMode(player, false);
						player.sendMessage(StringUtil.colorize("&cYour staffmode has been disabled cause you do not have permissions to use them."));
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractRandomTeleportItem(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			Player player = event.getPlayer();
			ItemStack itemStack = player.getItemInHand();
			if (itemStack.hasItemMeta())
			{
				if (itemStack.getItemMeta().getDisplayName().equals(StringUtil.colorize("&bRandom Teleportation")))
				{
					if (isStaffModeActive(player) && player.hasPermission("command.player.staff"))
					{
						if (Bukkit.getServer().getOnlinePlayers().size() > 1)
						{
							Random random = new Random();
							int size = random.nextInt(Bukkit.getServer().getOnlinePlayers().size());
							Player randomPlayer = null;
							for (Player each : Bukkit.getOnlinePlayers())
							{
								if (size > 0)
								{
									size--;
									continue;
								}
								randomPlayer = each;
							}
							player.teleport(randomPlayer);
							player.sendMessage(StringUtil.colorize("&cTeleporting you to &a" + randomPlayer.getName()));
							event.setCancelled(true);
						}
						else
						{
							player.sendMessage(StringUtil.colorize("&cCould not find players to teleport."));
						}
					}
					else
					{
						setStaffMode(player, false);
						player.sendMessage(StringUtil.colorize("&cYour staffmode has been disabled cause you do not have permissions to use them."));
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractVanishItem(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			Player player = event.getPlayer();
			ItemStack itemStack = player.getItemInHand();
			if (itemStack.hasItemMeta())
			{
				if (itemStack.getItemMeta().getDisplayName().contains(StringUtil.colorize("&bVanished:")))
				{
					if (isStaffModeActive(player) && player.hasPermission("command.player.staff"))
					{
						if (isVanished(player))
						{
							setVanished(player, false);
						}
						else
						{
							setVanished(player, true);
						}
					}
					else
					{
						setStaffMode(player, false);
						player.sendMessage(StringUtil.colorize("&cYour staffmode has been disabled cause you do not have permissions to use them."));
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (!player.hasPermission("command.player.staff") && vanished.size() > 0)
		{
			for (UUID uuid : vanished)
			{
				Player vanishedPlayer = Bukkit.getServer().getPlayer(uuid);
				if (vanishedPlayer != null)
				{
					player.hidePlayer(vanishedPlayer);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if (isStaffModeActive(player))
		{
			staffMode.remove(player.getUniqueId());
			PlayerInventory playerInventory = player.getInventory();
			playerInventory.setArmorContents(new ItemStack[] {
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR)
			});
			playerInventory.clear();
			setVanished(player, false);
			if (hasPreviousInventory(player))
			{
				loadInventory(player);
			}
			if (!player.hasPermission("essentials.creative") && player.getGameMode() == GameMode.CREATIVE)
			{
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player player = event.getPlayer();
		if (isStaffModeActive(player))
		{
			staffMode.remove(player.getUniqueId());
			PlayerInventory playerInventory = player.getInventory();
			playerInventory.setArmorContents(new ItemStack[] {
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR)
			});
			playerInventory.clear();
			setVanished(player, false);
			if (hasPreviousInventory(player))
			{
				loadInventory(player);
			}
			if (!player.hasPermission("essentials.creative") && player.getGameMode() == GameMode.CREATIVE)
			{
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		if (isStaffModeActive(player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		Player player = event.getPlayer();
		if (isStaffModeActive(player))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (isStaffModeActive(player))
		{
			if (block != null)
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (isStaffModeActive(player))
		{
			if (block != null)
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
		{
			Player player = (Player) event.getDamager();
			if (isStaffModeActive(player) && player.hasPermission("command.player.staff"))
			{
				event.setCancelled(true);
			}
			else
			{
				if (isStaffModeActive(player))
				{
					setStaffMode(player, false);
					player.sendMessage(StringUtil.colorize("&cYour staffmode has been disabled cause you do not have permissions to use them."));
				}
			}
		}
		else if (event.getEntity() instanceof LivingEntity && event.getDamager() instanceof Player)
		{
			Player player = (Player) event.getDamager();
			if (isStaffModeActive(player) && player.hasPermission("command.player.staff"))
			{
				event.setCancelled(true);
			}
			else
			{
				if (isStaffModeActive(player))
				{
					setStaffMode(player, false);
					player.sendMessage(StringUtil.colorize("&cYour staffmode has been disabled cause you do not have permissions to use them."));
				}
			}
		}
	}
}