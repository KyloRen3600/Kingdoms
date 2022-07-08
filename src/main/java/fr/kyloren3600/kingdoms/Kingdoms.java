package fr.kyloren3600.kingdoms;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.kyloren3600.kingdoms.admin.AdminCommand;
import fr.kyloren3600.kingdoms.display.DisplayManager;
import fr.kyloren3600.kingdoms.events.BukkitEventListener;
import fr.kyloren3600.kingdoms.events.ConfigReloadedEvent;
import fr.kyloren3600.kingdoms.events.ConfigReloadingEvent;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.kingdom.KingdomManager;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.teams.TeamManager;
import fr.kyloren3600.kingdoms.war.WarManager;
import fr.kyloren3600.kingdoms.war.time.HourIntervalOfDayOfWeek;
import fr.kyloren3600.kingdoms.war.time.TimeInterval;
import fr.kyloren3600.kingdoms.war.zone.Zone;
import fr.kyloren3600.kingdoms.war.zone.ZoneManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Kingdoms extends JavaPlugin {

	private static KingdomManager kingdomManager;
	private static TeamManager teamManager;
	private static WarManager warManager;
	private static ZoneManager zoneManager;
	private static DisplayManager displayManager;

	@Override
	public void onEnable() {

		teamManager = new TeamManager();
		displayManager = new DisplayManager("FR");

		saveDefaultConfig();
		loadConfig();

		getServer().getPluginManager().registerEvents(new BukkitEventListener(), this);

		AdminCommand.registerAdminCommand(this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static Kingdoms getInstance() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Kingdoms");
		if (plugin == null || !(plugin instanceof Kingdoms)) {
			throw new RuntimeException("'Kingdoms' not found. 'Kingdoms' plugin disabled?");
		}
		return ((Kingdoms) plugin);
	}

	public static KingdomManager getKingdomManager() {
		return kingdomManager;
	}

	public static TeamManager getTeamManager() {
		return teamManager;
	}

	public static WarManager getWarManager() {
		return warManager;
	}

	public static ZoneManager getZoneManager() {
		return zoneManager;
	}

	public static DisplayManager getDisplayManager() {
		return displayManager;
	}

	public static void callEvent(Event event) {
		Bukkit.getPluginManager().callEvent(event);
	}

	public void loadConfig() {
		callEvent(new ConfigReloadingEvent());

		if (kingdomManager != null) {
			HandlerList.unregisterAll(kingdomManager);
		}
		if (warManager != null) {
			HandlerList.unregisterAll(warManager);
		}
		if (zoneManager != null){
			HandlerList.unregisterAll(zoneManager);
		}

		displayManager.loadingConfig();
		reloadConfig();
		FileConfiguration fileConfiguration = getConfig();

		Map<String, Kingdom> kingdoms = new HashMap<>();
		Map<String, Zone> zones = new HashMap<>();
		ConfigurationSection kingdomsConfiguration = fileConfiguration.getConfigurationSection("kingdoms");
		if (kingdomsConfiguration != null) {
			kingdomsConfiguration.getKeys(false).forEach(kingdomKey -> {
				ConfigurationSection kingdomConfiguration = kingdomsConfiguration.getConfigurationSection(kingdomKey);
				String kingdomName = kingdomConfiguration.getString("name");
				String worldName = kingdomConfiguration.getString("world");
				String teamId = kingdomConfiguration.getString("owner");

				World world = null;
				if (worldName != null) {
					world = Bukkit.getWorld(worldName);
					if (world == null) displayManager.configIncorrectWorldSpecifiedForKingdom(kingdomName);
				} else {
					displayManager.configIncorrectWorldSpecifiedForKingdom(kingdomName);
				}
				Team team = Team.WILDERNESS;
				if (teamId == null) {
					displayManager.configKingdomOwnerNotSpecified(kingdomName);
				} else {
					team = teamManager.getTeamFromId(teamId);
					if (team == null) {
						team = Team.WILDERNESS;
						displayManager.configKingdomOwnerNotFound(kingdomName, teamId);
					}
				}
				Kingdom kingdom = new Kingdom(kingdomKey, kingdomName, world, team);
				kingdoms.put(kingdomKey, kingdom);

				if (world != null) {
					ConfigurationSection zonesConfiguration = kingdomConfiguration.getConfigurationSection("zones");
					if (zonesConfiguration != null) {
						World finalWorld = world;
						zonesConfiguration.getKeys(false).forEach(zoneKey -> {
							ConfigurationSection zoneConfiguration = zonesConfiguration.getConfigurationSection(zoneKey);
							String zoneName = zoneConfiguration.getString("name");
							String regionId = zoneConfiguration.getString("region");
							if (regionId != null) {
								RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(finalWorld));
								if (regionManager != null) {
									ProtectedRegion region = regionManager.getRegion(regionId);
									if (region != null) {
										Zone zone = new Zone(zoneName, kingdom, region, kingdom.getOwner());
										zones.put(zone.getId(), zone);
									} else {
										displayManager.configNoWorldGuardRegionWithSpecifiedId(zoneName, regionId);
									}
								} else {
									displayManager.configNoWorldGuardRegionWithSpecifiedId(zoneName, regionId);
								}
							} else {
								displayManager.configNoWorldGuardRegionSpecified(zoneName);
							}
						});
					} else {
						displayManager.configNoZoneSpecifiedForKingdom(kingdomName);
					}
				}
			});
		} else {
			displayManager.configNoKingdomSpecified();
		}

		List<TimeInterval> warTimes = new ArrayList<>();
		ConfigurationSection warsConfiguration = fileConfiguration.getConfigurationSection("wars");
		if (warsConfiguration != null) {
			warsConfiguration.getKeys(false).forEach(warKey -> {
				ConfigurationSection warConfiguration = warsConfiguration.getConfigurationSection(warKey);
				int day = warConfiguration.getInt("day");
				int startHour = warConfiguration.getInt("startHour");
				int startMinute = warConfiguration.getInt("startMinute");
				int endHour = warConfiguration.getInt("endHour");
				int endMinute = warConfiguration.getInt("endMinute");
				try {
					warTimes.add(new HourIntervalOfDayOfWeek(day, startHour, startMinute, endHour, endMinute));
				} catch (IllegalArgumentException e) {
					displayManager.configWrongWarTime(warKey);
				}
			});
		} else {
			displayManager.configNoWarTimeSpecified();
		}

		kingdomManager = new KingdomManager(kingdoms);
		zoneManager = new ZoneManager(zones);
		warManager = new WarManager(warTimes);

		String lang = "EN";
		Map<Kingdom, List<String>> kingdomHolograms = new HashMap<>();
		Map<Zone, List<String>> zoneHolograms = new HashMap<>();
		ConfigurationSection displayConfiguration = fileConfiguration.getConfigurationSection("display");
		if (displayConfiguration != null) {
			lang = displayConfiguration.getString("lang", "EN");
			ConfigurationSection kingdomsDisplayConfiguration = displayConfiguration.getConfigurationSection("kingdoms");
			if (kingdomsDisplayConfiguration != null) {
				kingdomsDisplayConfiguration.getKeys(false).forEach(kingdomId -> {
					Kingdom kingdom = kingdomManager.getKingdom(kingdomId);
					if (kingdom != null) {
						List<String> holograms = kingdomsDisplayConfiguration.getStringList(kingdomId);
						kingdomHolograms.put(kingdom, holograms);
					}
				});
			}
			ConfigurationSection zonesDisplayConfiguration = displayConfiguration.getConfigurationSection("zones");
			if (zonesDisplayConfiguration != null) {
				zonesDisplayConfiguration.getKeys(false).forEach(zoneId -> {
					Zone zone = zoneManager.getZone(zoneId);
					if (zone != null) {
						List<String> holograms = zonesDisplayConfiguration.getStringList(zoneId);
						zoneHolograms.put(zone, holograms);
					}
				});
			}
		} else  {
			displayManager.configNoDisplaySpecified();
		}

		displayManager = new DisplayManager(lang, kingdomHolograms, zoneHolograms);

		Bukkit.getServer().getOnlinePlayers().forEach(player -> {
			Kingdoms.getKingdomManager().registerPlayer(player);
		});

		getServer().getPluginManager().registerEvents(kingdomManager, this);
		getServer().getPluginManager().registerEvents(warManager, this);
		getServer().getPluginManager().registerEvents(zoneManager, this);

		displayManager.configLoaded();
		callEvent(new ConfigReloadedEvent());
	}
}
