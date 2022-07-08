package fr.kyloren3600.kingdoms.events.kingdom;

import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.kingdom.KingdomPlayer;

public final class KingdomEnteredEvent extends KingdomPlayerEvent {
	public KingdomEnteredEvent(Kingdom kingdom, KingdomPlayer kingdomPlayer) {
		super(kingdom, kingdomPlayer);

	}
}