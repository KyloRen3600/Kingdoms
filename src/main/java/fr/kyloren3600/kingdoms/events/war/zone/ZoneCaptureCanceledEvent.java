package fr.kyloren3600.kingdoms.events.war.zone;

import fr.kyloren3600.kingdoms.events.BaseEvent;
import fr.kyloren3600.kingdoms.war.zone.Zone;

public final class ZoneCaptureCanceledEvent extends BaseEvent implements ZoneEvent{

	private final Zone zone;

	public ZoneCaptureCanceledEvent(Zone zone) {
		this.zone = zone;
	}

	@Override
	public Zone getZone() {
		return zone;
	}
}
