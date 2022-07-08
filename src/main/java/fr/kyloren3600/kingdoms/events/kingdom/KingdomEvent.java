package fr.kyloren3600.kingdoms.events.kingdom;

import fr.kyloren3600.kingdoms.events.BaseEvent;
import fr.kyloren3600.kingdoms.kingdom.Kingdom;

public abstract class KingdomEvent extends BaseEvent {

	private final Kingdom kingdom;

	protected KingdomEvent(Kingdom kingdom) {
		this.kingdom = kingdom;
	}

	public Kingdom getKingdom() {
		return kingdom;
	}
}
