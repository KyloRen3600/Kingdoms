package fr.kyloren3600.kingdoms.events.kingdom;

import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.teams.Team;

public final class KingdomConqueredEvent extends KingdomEvent {

	private final Team oldOwner;
	private final Team newOwner;

	public KingdomConqueredEvent(Kingdom kingdom, Team oldOwner, Team newOwner) {
		super(kingdom);
		this.oldOwner = oldOwner;
		this.newOwner = newOwner;
	}

	public Team getOldOwner() {
		return oldOwner;
	}

	public Team getNewOwner() {
		return newOwner;
	}
}