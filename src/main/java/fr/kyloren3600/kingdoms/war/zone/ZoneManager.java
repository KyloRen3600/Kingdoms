package fr.kyloren3600.kingdoms.war.zone;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.ConfigReloadingEvent;
import fr.kyloren3600.kingdoms.events.war.WarEndedEvent;
import fr.kyloren3600.kingdoms.events.war.WarLeftEvent;
import fr.kyloren3600.kingdoms.events.war.WarStartedEvent;
import fr.kyloren3600.kingdoms.events.war.zone.*;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.war.WarPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneManager implements Listener {

	private static final long ZONE_CHECK_INTERVAL = 20;

	private final Map<String, Zone> zones;
	private final Map<WarPlayer, BukkitTask> zoneCheckers;

	public ZoneManager(Map<String, Zone> zones) {
		this.zones = new HashMap<>(zones);
		zoneCheckers = new HashMap<>();
	}

	public Zone getZone(String id) {
		return zones.get(id);
	}

	public List<Zone> getZonesOfKingdom(Kingdom kingdom) {
		return zones.values().stream().filter(zone -> kingdom.equals(zone.getKingdom())).toList();
	}

	private void registerZoneChecker(Zone zone, WarPlayer warPlayer) {
		zoneCheckers.put(warPlayer,
			Bukkit.getScheduler().runTaskLater(Kingdoms.getInstance(), () -> {
				if (warPlayer.getZone().equals(zone)) {
					Team warPlayerTeam = warPlayer.getKingdomPlayer().getTeam();
					if (warPlayerTeam.equals(zone.getCapturingTeam())) {
						if (zone.addToCapturePercent(100)) {
							zone.capture(warPlayer.getKingdomPlayer().getTeam());
							return;
						}
					} else if (canZoneBeCapturedByTeam(zone, warPlayerTeam)) {
						zone.startCapture(warPlayerTeam);
					}
					registerZoneChecker(zone, warPlayer);
				}
			}, ZONE_CHECK_INTERVAL)
		);
	}

	private boolean canZoneBeCapturedByTeam(Zone zone, Team team) {
		if (zone.getCapturingTeam() == null && !team.equals(Team.WILDERNESS) && !team.equals(zone.getKingdom().getOwner()) && !team.equals(zone.getOwnerTeam())) {
			if (zone.getPlayers().size() == 0) return false;
			for (WarPlayer warPlayer : zone.getPlayers()) {
				if (!warPlayer.getKingdomPlayer().getTeam().equals(team)) return false;
			}
			return true;
		}
		return false;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onZoneEntered(ZoneEnteredEvent event) {
		event.getZone().registerPlayer(event.getWarPlayer());
		registerZoneChecker(event.getZone(), event.getWarPlayer());
		Kingdoms.getDisplayManager().zoneEntered(event.getZone(), event.getWarPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onZoneLeft(ZoneLeftEvent event) {
		event.getZone().unregisterPlayer(event.getWarPlayer());
		BukkitTask task = zoneCheckers.get(event.getWarPlayer());
		if (task != null) task.cancel();
		Kingdoms.getDisplayManager().zoneLeft(event.getZone(), event.getWarPlayer());
		if (event.getWarPlayer().getKingdomPlayer().getTeam().equals(event.getZone().getCapturingTeam())) {
			boolean captureCanceled = true;
			for (WarPlayer warPlayer : event.getZone().getPlayers()) {
				if (warPlayer.getKingdomPlayer().getTeam().equals(event.getZone().getCapturingTeam())) captureCanceled = false;
			}
			if (captureCanceled) event.getZone().cancelCapture();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onZoneCaptureStarted(ZoneCaptureStartedEvent event) {
		Kingdoms.getDisplayManager().zoneCaptureStarted(event.getZone(), event.getTeam());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onZoneCaptured(ZoneCapturedEvent event) {
		Kingdoms.getDisplayManager().zoneCaptured(event.getZone(), event.getTeam());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onZoneCaptureCanceled(ZoneCaptureCanceledEvent event) {
		Kingdoms.getDisplayManager().zoneCaptureCanceled(event.getZone());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onZoneCapturePercentChanged(ZoneCapturePercentChangedEvent event) {
		Kingdoms.getDisplayManager().zoneCapturePercentChanged(event.getZone(), event.getNewValue());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWarStarted(WarStartedEvent event) {
		zones.forEach((k, v) -> v.resetCapture());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWarEnded(WarEndedEvent event) {
		zones.forEach((k, v) -> v.resetCapture());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWarLeft(WarLeftEvent event) {
		event.getWarPlayer().leaveZone();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onConfigReloading(ConfigReloadingEvent event) {
		zoneCheckers.forEach((k, v) -> v.cancel());
	}
}
