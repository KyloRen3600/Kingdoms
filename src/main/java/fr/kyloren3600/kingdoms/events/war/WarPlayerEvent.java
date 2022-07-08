package fr.kyloren3600.kingdoms.events.war;

import fr.kyloren3600.kingdoms.events.kingdom.KingdomPlayerEvent;
import fr.kyloren3600.kingdoms.war.WarPlayer;

public abstract class WarPlayerEvent extends KingdomPlayerEvent {

	private final WarPlayer warPlayer;

	protected WarPlayerEvent(WarPlayer warPlayer) {
		super(warPlayer.getKingdomPlayer().getKingdom(), warPlayer.getKingdomPlayer());
		this.warPlayer = warPlayer;
	}

	public WarPlayer getWarPlayer() {
		return warPlayer;
	}
}
