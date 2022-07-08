package fr.kyloren3600.kingdoms.events.war.zone;

import fr.kyloren3600.kingdoms.events.BaseEvent;
import fr.kyloren3600.kingdoms.teams.Team;
import fr.kyloren3600.kingdoms.war.zone.Zone;

public class ZoneCaptureStartedEvent extends BaseEvent implements ZoneEvent {

	private final Zone zone;
	private final Team team;

	public ZoneCaptureStartedEvent(Zone zone, Team team) {
		this.zone = zone;
		this.team = team;
	}

	@Override
	public Zone getZone() {
		return zone;
	}

	public Team getTeam() {
		return team;
	}
}
