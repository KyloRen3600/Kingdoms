package fr.kyloren3600.kingdoms.events.war.zone;

import fr.kyloren3600.kingdoms.war.WarPlayer;
import fr.kyloren3600.kingdoms.war.zone.Zone;

public final class ZoneEnteredEvent extends PlayerZoneEvent {
	public ZoneEnteredEvent(Zone zone, WarPlayer warPlayer) {
		super(zone, warPlayer);
	}
}