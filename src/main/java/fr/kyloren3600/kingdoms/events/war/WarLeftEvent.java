package fr.kyloren3600.kingdoms.events.war;

import fr.kyloren3600.kingdoms.war.WarPlayer;

public class WarLeftEvent extends WarPlayerEvent {
	public WarLeftEvent(WarPlayer warPlayer) {
		super(warPlayer);
	}
}