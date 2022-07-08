package fr.kyloren3600.kingdoms.events.war.zone;

import fr.kyloren3600.kingdoms.events.war.WarPlayerEvent;
import fr.kyloren3600.kingdoms.war.WarPlayer;
import fr.kyloren3600.kingdoms.war.zone.Zone;

public abstract class PlayerZoneEvent extends WarPlayerEvent implements ZoneEvent {

	private final Zone zone;

	protected PlayerZoneEvent(Zone zone, WarPlayer warPlayer) {
		super(warPlayer);
		this.zone = zone;
	}

	@Override
	public Zone getZone() {
		return zone;
	}
}
