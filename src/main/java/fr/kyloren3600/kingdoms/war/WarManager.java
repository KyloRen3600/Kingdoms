package fr.kyloren3600.kingdoms.war;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.ConfigReloadingEvent;
import fr.kyloren3600.kingdoms.events.kingdom.KingdomLeftEvent;
import fr.kyloren3600.kingdoms.events.war.WarEndedEvent;
import fr.kyloren3600.kingdoms.events.war.WarJoinedEvent;
import fr.kyloren3600.kingdoms.events.war.WarLeftEvent;
import fr.kyloren3600.kingdoms.events.war.WarStartedEvent;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.war.time.TimeInterval;
import fr.kyloren3600.kingdoms.war.zone.Zone;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WarManager implements Listener {

	private static final long CHECK_INTERVAL = 20 * 10;

	private final List<TimeInterval> warTimes;
	private final Map<Player, WarPlayer> warPlayers;

	private boolean warGoingOn;
	private BukkitTask warCheckerTask;

	private boolean warForced = false;

	public WarManager(List<TimeInterval> warTimes) {
		this.warTimes = List.copyOf(warTimes);
		warGoingOn = false;
		warPlayers = new HashMap<>();
		registerWarCheckerTask();
	}

	public void forceWar() {
		warForced = !warForced;
		if (warForced) {
			startWar();
		} else {
			endWar();
		}

	}

	public boolean isWarGoingOn() {
		return warGoingOn;
	}

	public WarPlayer getWarPlayer(Player bukkitPlayer) {
		return warPlayers.get(bukkitPlayer);
	}

	public boolean joinWar(Player bukkitPlayer) {
		if (!isWarGoingOn()) return false;
		if (warPlayers.containsKey(bukkitPlayer)) return false;
		WarPlayer warPlayer = new WarPlayer(Kingdoms.getKingdomManager().getKingdomPlayer(bukkitPlayer));
		warPlayers.put(bukkitPlayer, warPlayer);
		Kingdoms.callEvent(new WarJoinedEvent(warPlayer));
		return true;
	}

	public boolean leaveWar(Player bukkitPlayer) {
		if (!isWarGoingOn()) return false;
		if (!warPlayers.containsKey(bukkitPlayer)) return false;
		WarPlayer warPlayer = warPlayers.get(bukkitPlayer);
		warPlayers.remove(bukkitPlayer);
		Kingdoms.callEvent(new WarLeftEvent(warPlayer));
		return true;
	}

	private void startWar() {
		Kingdoms.getDisplayManager().warStarted();
		warGoingOn = true;
		Bukkit.getPluginManager().callEvent(new WarStartedEvent());
	}

	private void endWar() {
		Kingdoms.getDisplayManager().warEnded();
		warPlayers.forEach((k, v) -> {
			leaveWar(k);});
		warGoingOn = false;
		for (Kingdom kingdom : Kingdoms.getKingdomManager().getKingdoms()) {
			Team oldOwner = kingdom.getOwner();
			Team newOwner = null;
			for (Zone zone: Kingdoms.getZoneManager().getZonesOfKingdom(kingdom)) {
				if (newOwner == null) {
					newOwner = zone.getOwnerTeam();
				} else {
					if (!newOwner.equals(zone.getOwnerTeam())) {
						newOwner = null;
						break;
					}
				}
			}
			if (newOwner == null) {
				//kingdom not conquered !
			} else if(oldOwner.equals(newOwner)) {

			} else {
				//kingdom conquered !
				kingdom.conquer(newOwner);
			}
		}
		Bukkit.getPluginManager().callEvent(new WarEndedEvent());
	}

	private boolean isTimeToStartWar() {
		if (!warForced) return false;

		if (isWarGoingOn()) return false;
		for (TimeInterval warTime : warTimes) {
			if (warTime.isNow()) return true;
		}
		return false;
	}

	private boolean isTimeToEndWar() {
		if (warForced) return false;

		if (!isWarGoingOn()) return false;
		for (TimeInterval warTime : warTimes) {
			if (warTime.isNow()) return false;
		}
		return true;
	}

	private void manageWar() {
		if (isTimeToStartWar()) {
			startWar();
			return;
		}

		if (isTimeToEndWar()) {
			endWar();
			return;
		}
	}

	private void registerWarCheckerTask() {
		warCheckerTask = Bukkit.getScheduler().runTaskLater(Kingdoms.getInstance(), () -> {
			manageWar();
			registerWarCheckerTask();
		}, CHECK_INTERVAL);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onKingdomLeft(KingdomLeftEvent event) {
		leaveWar(event.getBukkitPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onConfigReloading(ConfigReloadingEvent event) {
		if (warCheckerTask != null) {
			warCheckerTask.cancel();
		}
	}
}
