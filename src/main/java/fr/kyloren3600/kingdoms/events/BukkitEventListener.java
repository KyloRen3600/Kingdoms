package fr.kyloren3600.kingdoms.events;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.war.WarPlayer;
import fr.kyloren3600.kingdoms.war.zone.Zone;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionLeftEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class BukkitEventListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Kingdoms.getKingdomManager().registerPlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Kingdoms.getKingdomManager().unregisterPlayer(event.getPlayer());
	}

	@EventHandler
	public void onRegionEnter(RegionEnteredEvent event) {
		WarPlayer warPlayer = Kingdoms.getWarManager().getWarPlayer(event.getPlayer());
		if (warPlayer != null) {
			if (warPlayer.getZone() != null) {
				if (event.getRegion().equals(warPlayer.getZone().getRegion())) return;
			}
			Zone enteredZone = Kingdoms.getZoneManager().getZone(Zone.getZoneIdFromKingdomAndRegion(warPlayer.getKingdomPlayer().getKingdom(), event.getRegion()));
			if (enteredZone != null) {
				warPlayer.enterZone(enteredZone);
			}
		}
	}

	@EventHandler
	public void onRegionLeave(RegionLeftEvent event) {
		WarPlayer warPlayer = Kingdoms.getWarManager().getWarPlayer(event.getPlayer());
		if (warPlayer != null) {
			if (warPlayer.getZone() == null) return;
			if (warPlayer.getZone().getRegion().equals(event.getRegion())) {
				Zone leftZone = Kingdoms.getZoneManager().getZone(Zone.getZoneIdFromKingdomAndRegion(warPlayer.getKingdomPlayer().getKingdom(), event.getRegion()));
				if (leftZone != null) {
					warPlayer.leaveZone();
				}
			}
		}
	}
}
