package fr.kyloren3600.kingdoms.events.kingdom;

import fr.kyloren3600.kingdoms.kingdom.Kingdom;
import fr.kyloren3600.kingdoms.kingdom.KingdomPlayer;

public final class KingdomLeftEvent extends KingdomPlayerEvent {
	public KingdomLeftEvent(Kingdom kingdom, KingdomPlayer kingdomPlayer) {
		super(kingdom, kingdomPlayer);
	}
}