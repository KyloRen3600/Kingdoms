package fr.kyloren3600.kingdoms.war;

import fr.kyloren3600.kingdoms.Kingdoms;
import fr.kyloren3600.kingdoms.events.war.zone.ZoneEnteredEvent;
import fr.kyloren3600.kingdoms.events.war.zone.ZoneLeftEvent;
import fr.kyloren3600.kingdoms.kingdom.KingdomPlayer;
import fr.kyloren3600.kingdoms.war.zone.Zone;

public final class WarPlayer {

	private final KingdomPlayer kingdomPlayer;

	private Zone zone;

	WarPlayer(KingdomPlayer kingdomPlayer) {
		this.kingdomPlayer = kingdomPlayer;
		this.zone = null;
	}

	public KingdomPlayer getKingdomPlayer() {
		return kingdomPlayer;
	}

	public Zone getZone() {
		return zone;
	}

	public void enterZone(Zone zone) {
		this.zone = zone;
		Kingdoms.callEvent(new ZoneEnteredEvent(zone, this));
	}

	public void leaveZone() {
		if (this.zone != null) {
			Zone leftZone = zone;
			this.zone = null;
			Kingdoms.callEvent(new ZoneLeftEvent(leftZone, this));
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof WarPlayer that) {
			return this.kingdomPlayer.getBukkitPlayer().getUniqueId().equals(that.kingdomPlayer.getBukkitPlayer().getUniqueId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return kingdomPlayer.getBukkitPlayer().hashCode();
	}
}
