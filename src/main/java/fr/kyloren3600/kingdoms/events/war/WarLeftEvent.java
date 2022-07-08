package fr.kyloren3600.kingdoms.events.war;

import fr.kyloren3600.kingdoms.war.WarPlayer;

public final class WarLeftEvent extends WarPlayerEvent {
	public WarLeftEvent(WarPlayer warPlayer) {
		super(warPlayer);
	}
}