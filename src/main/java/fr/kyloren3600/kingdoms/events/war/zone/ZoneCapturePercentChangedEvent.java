package fr.kyloren3600.kingdoms.events.war.zone;

import fr.kyloren3600.kingdoms.events.BaseEvent;
import fr.kyloren3600.kingdoms.war.zone.Zone;

public final class ZoneCapturePercentChangedEvent extends BaseEvent implements ZoneEvent {

	private final Zone zone;
	private final int oldValue;
	private final int newValue;

	public ZoneCapturePercentChangedEvent(Zone zone, int oldValue, int newValue) {
		this.zone = zone;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public Zone getZone() {
		return zone;
	}

	public int getOldValue() {
		return oldValue;
	}

	public int getNewValue() {
		return newValue;
	}
}
